package core.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import core.Metric;
import core.campaigns.Campaign;
import core.records.Click;
import core.records.Impression;
import core.records.Record;
import util.DateProcessor;

// TODO should we rename this to DataProcessor instead?
// Chart sounds like something the view should be handling
public class DataProcessor {
	
	// ==== Constants ====
	
	// number of nodes used to compute time granularity bounds
	public final static int MINIMUM_NUMBER_OF_NODES = 5;
	public final static int MAXIMUM_NUMBER_OF_NODES = 20;
	
	// ==== Properties ====
	
	// the campaign we are working on for this chart
	private Campaign campaign;
	
	// the start and end dates of this dataprocessor
	private LocalDateTime dataStartDate;
	private LocalDateTime dataEndDate;
	
	// the filter to filter the metrics by
	private final DataFilter dataFilter = new DataFilter();
	
	// the metric that the chart is handling
	private Metric metric;
	
	// the time granularity of this dataprocessor
	private int timeGranularityInSeconds = 60 * 60 * 24;
	
	// bounce logic
	private int bounceMinimumPagesViewed;
	private int bounceMinimumSecondsOnPage;
	
	
	// ==== Constructor ====
	
	public DataProcessor(Campaign campaign) {
		setCampaign(campaign);
		numberOfImpressions();
		numberOfClicks();
	}
	

	// ==== Accessors ====
	
	public final Campaign getCampaign() {
		return campaign;
	}
	
	public final void setCampaign(Campaign campaign) {
		final LocalDateTime campaignStartDate = campaign.getStartDateTime();
		final LocalDateTime campaignEndDate = campaign.getEndDateTime();		
		
		if (dataStartDate == null || dataStartDate.isBefore(campaignStartDate) || !dataStartDate.isBefore(campaignEndDate))
			dataStartDate = campaignStartDate;
		
		if (dataEndDate == null || dataEndDate.isAfter(campaignEndDate))
			dataEndDate = campaignEndDate;
		
		this.campaign = campaign;
	}
	
	
	public final Metric getMetric() {
		return metric;
	}
	
	public final void setMetric(Metric metric) {
		if (this.metric == metric)
			return;
		
		this.metric = metric;
	}
	
	
	public final List<? extends Number> getData() {
		switch (metric) {
		case NUMBER_OF_IMPRESSIONS:
			return numberOfImpressions();
		case NUMBER_OF_CLICKS:
			return numberOfClicks();
		case NUMBER_OF_UNIQUES:
			return numberOfUniques();
		case NUMBER_OF_BOUNCES:
			return numberOfBounces();
		case TOTAL_COST:
			return totalCost();
		case CLICK_THROUGH_RATE:
			return CTR();
		case COST_PER_ACQUISITION:
			return null;
		default:
			return null;
		}
	}
	
	
	public final LocalDateTime getDataStartDateTime() {
		return dataStartDate;
	}
	
	public final void setDataStartDate(LocalDateTime dataStartDate) {
		// check if the input start date happens before the campaign start date
		if (dataStartDate.isBefore(campaign.getStartDateTime()))
			throw new IllegalArgumentException("cannot set data start date before campaign starts");
		
		// check input happens before the campaign ends
		if (!dataStartDate.isBefore(campaign.getEndDateTime()))
			throw new IllegalArgumentException("cannot set data start date to after campaign ends");
		
		// start date must be before end date
		if (!dataStartDate.isBefore(dataEndDate))
			throw new IllegalArgumentException("cannot set data start date to after the end date");
		
		// update the values if is valid
		this.dataStartDate = dataStartDate;
	}
	
	
	public final LocalDateTime getDataEndDate() {
		return dataEndDate;
	}
	
	public final void setDataEndDate(LocalDateTime dataEndDate) {
		// check input is not after campaign end date
		if (dataEndDate.isAfter(campaign.getEndDateTime()))
			throw new IllegalArgumentException("cannot set data end date to after campaign ends");
		
		// end date must be after the start date
		if (!dataEndDate.isAfter(campaign.getStartDateTime()))
			throw new IllegalArgumentException("cannot set data end date to before campaign starts");
		
		// end date must be after start date
		if (!dataEndDate.isAfter(dataStartDate))
			throw new IllegalArgumentException("cannot set data end date to before the start date");
		
		// update if valid
		this.dataEndDate = dataEndDate;
	}
	
	
	public final int getTimeGranularityInSeconds() {
		return timeGranularityInSeconds;
	}
	
	public final void setTimeGranularityInSeconds(int timeGranularityInSeconds) {
		// check time granularity is at least 1
		if (timeGranularityInSeconds < 1)
			throw new IllegalArgumentException("cannot have time granularity below 1 second");
		
		// store the time difference to compute min/max bounds
		final long timeDifference = ChronoUnit.SECONDS.between(dataStartDate, dataEndDate);
		
		// we want a minimum number of data points
		// if time granularity is larger than this number, then we won't have enough nodes
		if (timeGranularityInSeconds  > timeDifference / MINIMUM_NUMBER_OF_NODES)
			timeGranularityInSeconds = (int) (timeDifference / MINIMUM_NUMBER_OF_NODES);
		
		// converse to above logic
		if (timeGranularityInSeconds < timeDifference / MAXIMUM_NUMBER_OF_NODES)
			timeGranularityInSeconds = (int) (timeDifference / MAXIMUM_NUMBER_OF_NODES);
		
		// TODO remove, just throwing error if we reach this unreachable state
		if (timeGranularityInSeconds < 1 || timeGranularityInSeconds >=  timeDifference)
			throw new IllegalArgumentException("something happened in DataProcessor setTimeG...");
		
		// time granularity 		
		this.timeGranularityInSeconds = timeGranularityInSeconds;
	}
	
	
	public final int getBounceMinimumPagesViewed() {
		return bounceMinimumPagesViewed;
	}
	
	public final void setBounceMinimumPagesViewed(int bounceMinimumPagesViewed) {
		if (bounceMinimumPagesViewed < 1)
			throw new IllegalArgumentException("cannot have less than 1 page view");
		
		this.bounceMinimumPagesViewed = bounceMinimumPagesViewed;
	}
	
	public final int getBounceMinimumSecondsOnPage() {
		return bounceMinimumSecondsOnPage;
	}
	
	public final void setBounceMinimumSecondsOnPage(int bounceMinimumSecondsOnPage) {
		if (bounceMinimumSecondsOnPage < 1)
			throw new IllegalArgumentException("cannot spend less than 1 second on page");
		
		this.bounceMinimumSecondsOnPage = bounceMinimumSecondsOnPage;
	}
	
	public final boolean getFieldFilteredValue(User field) {
		return dataFilter.getField(field);
	}
	public final void setFieldFilterValue(User field, boolean value) {
		dataFilter.setField(field, value);
	}
	
	
	// ==== Compute Metrics ====	
	// 75ms without parallelstream
	
	public final List<Integer> numberOfImpressions() {
		final ArrayList<Integer> impressionsList = new ArrayList<Integer>();
		
		int numberOfImpressions = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
		
		long time = System.currentTimeMillis();	
		
		outerLoop:
		for (Impression impression : campaign.getImpressions()) {
			final long dateTime = impression.getEpochSeconds();
						
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == finalDate)
					break outerLoop;
				
				impressionsList.add(numberOfImpressions);
				
				numberOfImpressions = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > finalDate)
					nextDate = finalDate;
			}
			
			if (dataFilter.test(impression.getUserData())) {
				numberOfImpressions++;
			}
		}
		
		// add last entry
		impressionsList.add(numberOfImpressions);
		
		System.out.println("Processing: \t" + (System.currentTimeMillis() - time));
		System.out.println("Size of Query: \t" + impressionsList.size());

		// pack
		impressionsList.trimToSize();
				
		return impressionsList;
	}
	
	public final List<Integer> numberOfClicks() {		
		final ArrayList<Integer> clicksList = new ArrayList<Integer>();
		
		int numberOfClicks = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
		
		final long time = System.currentTimeMillis();	
		
		outerLoop:
		for (Impression impression : campaign.getImpressions()) {
			final long dateTime = impression.getEpochSeconds();
			
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == finalDate)
					break outerLoop;
				
				clicksList.add(numberOfClicks);
				
				numberOfClicks = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > finalDate)
					nextDate = finalDate;
			}
			
			if (dataFilter.test(impression.getUserData()))
				numberOfClicks++;
		}
		
		clicksList.add(numberOfClicks);
		
		System.out.println("Processing: \t" + (System.currentTimeMillis() - time));
		System.out.println("Size of Query: \t" + clicksList.size());
		
		// pack
		clicksList.trimToSize();
		
		return clicksList;
	}
	
	// The number of unique users that click on an ad during the course of a campaign.
	public final List<Integer> numberOfUniques() {
		final List<Integer> uniquesList = new ArrayList<Integer>();
		
		return uniquesList;
	}
	
	/*
	 * A user clicks on an ad, but then fails to interact with the website
	 * (typically detected when a user navigates away from the website after a
	 * short time, or when only a single page has been viewed).
	 */
	public final List<Integer> numberOfBounces() {
		final List<Integer> bouncesList = new ArrayList<Integer>();
		
		return bouncesList;
	}
	
	public final List<Integer> numberOfConversions() {
		final List<Integer> conversionsList = new ArrayList<Integer>();
		
		return conversionsList;
	}
	
	// I'm assuming this is cost of impression and click
	public final List<Double> totalCost() {
		final List<Double> impressionsCost = new ArrayList<Double>();
		final List<Double> clicksCost = new ArrayList<Double>();
		final List<Double> costList = new ArrayList<Double>();

		
		return costList;
	}
	
	// average clicks per impression
	public final List<Double> CTR() {
		
		return null;
	}
	
	public final Map<LocalDateTime, Double> CPA() {
		return null;
	}
	
	public final Map<LocalDateTime, Double> CPC() {
		return null;
	}
	
	public final Map<LocalDateTime, Double> CPM() {
		return null;
	}
	
	public final Map<LocalDateTime, Integer> bounceRate() {
		return null;
	}
	
	public final List<? extends Number> computeCurrentMetric() {
		return null;
	}
	
	public final List<Double> newNumberOfImpressions() {
		int idx = 0;
		Set<Long> niceSet = new HashSet<Long>();
		
		Iterable<Impression> iterable = campaign.getImpressions();
		Predicate<Impression> predicate = x -> true;
		Function<Impression, Double> function = x -> 1d;
		Consumer<Impression> doStuffHere = x -> niceSet.add(x.getUserID());
		Consumer<Impression> clearStuffHere = x -> niceSet.clear();
//		Consumer<Impression> accumulator = x -> idx++;
		
		return metricsCalculator(iterable, predicate, function);
	}
	
	public int whatever(long epochSeconds) {
		long startDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
		
		for (int i = 0;; i++) {
			final long currentDate = startDate + i * timeGranularityInSeconds;
			
			if (epochSeconds < currentDate)
				return i - 1;
		}
	}
	
	// ==== Private Helper Methods ====
	
	private final <T extends Record> List<Double> metricsCalculator(Iterable<T> iterable, Predicate<T> predicate, Function<T, Double> function) {
		final ArrayList<Double> results = new ArrayList<Double>();
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		
		double accumulator = 0;
		
		outerLoop:
		for (T row : iterable) {
			final long epochSeconds = row.getEpochSeconds();
			final int userData = row.getUserData();
			
			// we ignore the impression if the date is before the current date
			if (epochSeconds < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (epochSeconds > nextDate) {				
				if (nextDate == finalDate)
					break outerLoop;
				
				results.add(accumulator);
				
				accumulator = 0;
				
				currentDate = nextDate;
				nextDate = nextDate + timeGranularityInSeconds;
				
				if (nextDate > finalDate)
					nextDate = finalDate;
			}
			
			// filter for users and execute process
			if (dataFilter.test(userData)) {
				if (predicate.test(row))
					accumulator += function.apply(row);
			}			
		}
		
		// add final value
		results.add(accumulator);
		
		// reduce memory usage, necessary?
		results.trimToSize();
		
		return results;
	}
}
