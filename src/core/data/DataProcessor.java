package core.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
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
import core.records.CostRecord;
import core.records.Impression;
import core.records.Record;
import core.records.Server;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;
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
	private Metric metric = Metric.NUMBER_OF_IMPRESSIONS;
	
	// the time granularity of this dataprocessor
	private int timeGranularityInSeconds = 60 * 60 * 24;
	
	// bounce logic
	private int bounceMinimumPagesViewed = 1;
	private int bounceMinimumSecondsOnPage = 30;
	
	
	// ==== Constructor ====
	
	public DataProcessor() {
	}
	

	// ==== Accessors ====
	
	public final Campaign getCampaign() {
		return campaign;
	}
	
	public final void setCampaign(Campaign campaign) {
		// thanks @csjames! :)
		if (this.campaign != null && this.campaign.equals(campaign))
			return;
		
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
		List<? extends Number> returnList;

		final long time = System.currentTimeMillis();
		
		switch (metric) {
		case NUMBER_OF_IMPRESSIONS:
			returnList = numberOfImpressions();
			break;
		case NUMBER_OF_CLICKS:
			returnList = numberOfClicks();
			break;
		case NUMBER_OF_UNIQUES:
			returnList = numberOfUniques();
			break;
		case NUMBER_OF_BOUNCES:
			returnList = numberOfBounces();
			break;
		case NUMBER_OF_CONVERSIONS:
			returnList = numberOfConversions();
			break;
		case TOTAL_COST:
			returnList = totalCost();
			break;
		case CLICK_THROUGH_RATE:
			returnList = clickThroughRate();
			break;
		case COST_PER_ACQUISITION:
			returnList = costPerAcquisition();
			break;
		case COST_PER_CLICK:
			returnList = costPerClick();
			break;
		case COST_PER_THOUSAND_IMPRESSIONS:
			returnList = costPerThousandImpressions();
			break;
		case BOUNCE_RATE:
			returnList = bounceRate();
			break;
		default:
			returnList = null;
		}
		
		if (returnList != null) {
			System.out.println("Metric Type:\t" + metric.toString());
			System.out.println("Data Size:\t" + returnList.size());
			System.out.println("Time Taken:\t" + (System.currentTimeMillis() - time));
			System.out.println("--------------------------------------");
		}
		
		return returnList;
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
		
//		// store the time difference to compute min/max bounds
//		final long timeDifference = ChronoUnit.SECONDS.between(dataStartDate, dataEndDate);
//		
//		// we want a minimum number of data points
//		// if time granularity is larger than this number, then we won't have enough nodes
//		if (timeGranularityInSeconds  > timeDifference / MINIMUM_NUMBER_OF_NODES)
//			timeGranularityInSeconds = (int) (timeDifference / MINIMUM_NUMBER_OF_NODES);
//		
//		// converse to above logic
//		if (timeGranularityInSeconds < timeDifference / MAXIMUM_NUMBER_OF_NODES)
//			timeGranularityInSeconds = (int) (timeDifference / MAXIMUM_NUMBER_OF_NODES);
//		
//		// TODO remove, just throwing error if we reach this unreachable state
//		if (timeGranularityInSeconds < 1 || timeGranularityInSeconds >=  timeDifference)
//			throw new IllegalArgumentException("something happened in DataProcessor setTimeG...");
		
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
	
	private final List<Integer> numberOfImpressions() {
		final ArrayList<Integer> impressionsList = new ArrayList<Integer>();
		
		int numberOfImpressions = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
		
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

		// pack
		impressionsList.trimToSize();
				
		return impressionsList;
	}
	
	private final List<Integer> numberOfClicks() {		
		final ArrayList<Integer> clicksList = new ArrayList<Integer>();
		
		int numberOfClicks = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
		
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
		
		// add last entry
		clicksList.add(numberOfClicks);
		
		// pack
		clicksList.trimToSize();
		
		return clicksList;
	}
	
	// The number of unique users that click on an ad during the course of a campaign.
	private final List<Integer> numberOfUniques() {
		final ArrayList<Integer> uniquesList = new ArrayList<Integer>();
		
//		TLongSet usersSet = new TLongHashSet();
		HashSet<Long> usersSet = new HashSet<Long>();
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
				
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
				
				uniquesList.add(usersSet.size());
				
				usersSet.clear();
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > finalDate)
					nextDate = finalDate;
			}
			
			if (dataFilter.test(impression.getUserData()))
				usersSet.add(impression.getUserID());
		}
		
		// add last entry
		uniquesList.add(usersSet.size());

		// pack
		uniquesList.trimToSize();
				
		return uniquesList;
	}
	
	/*
	 * A user clicks on an ad, but then fails to interact with the website
	 * (typically detected when a user navigates away from the website after a
	 * short time, or when only a single page has been viewed).
	 */
	private final List<Integer> numberOfBounces() {
		final ArrayList<Integer> bouncesList = new ArrayList<Integer>();
		
		int numberOfBounces = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
				
		outerLoop:
		for (Server server : campaign.getServers()) {
			final long dateTime = server.getEpochSeconds();
						
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == finalDate)
					break outerLoop;
				
				bouncesList.add(numberOfBounces);
				
				numberOfBounces = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > finalDate)
					nextDate = finalDate;
			}
			
			if (dataFilter.test(server.getUserData())) {				
				if (server.getPagesViewed() > bounceMinimumPagesViewed)
					continue;
				
				if (server.getExitEpochSeconds() == DateProcessor.DATE_NULL)
					continue;
				
				if (server.getExitEpochSeconds() - server.getEpochSeconds() > bounceMinimumSecondsOnPage)
					continue;
				
				numberOfBounces++;
			}
		}
		
		// add last entry
		bouncesList.add(numberOfBounces);

		// pack
		bouncesList.trimToSize();
		
		return bouncesList;
	}
	
	private final List<Integer> numberOfConversions() {
		final ArrayList<Integer> conversionsList = new ArrayList<Integer>();
		
		int numberOfConversions = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
				
		outerLoop:
		for (Server server : campaign.getServers()) {
			final long dateTime = server.getEpochSeconds();
						
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == finalDate)
					break outerLoop;
				
				conversionsList.add(numberOfConversions);
				
				numberOfConversions = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > finalDate)
					nextDate = finalDate;
			}
			
			if (dataFilter.test(server.getUserData())) {
				if (server.getConversion())
					numberOfConversions++;
			}
		}
		
		// add last entry
		conversionsList.add(numberOfConversions);

		// pack
		conversionsList.trimToSize();
		
		return conversionsList;
	}
	
	private final <T extends CostRecord> List<Double> costOfRecord(Collection<T> records) {
		final ArrayList<Double> costList = new ArrayList<Double>();
		
		double costOfImpressions = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate.toEpochSecond(ZoneOffset.UTC);
		long nextDate = currentDate + timeGranularityInSeconds;
		long finalDate = dataEndDate.toEpochSecond(ZoneOffset.UTC);
		
		outerLoop:
		for (CostRecord costRecord : records) {
			final long dateTime = costRecord.getEpochSeconds();
						
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == finalDate)
					break outerLoop;
				
				costList.add(costOfImpressions);
				
				costOfImpressions = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > finalDate)
					nextDate = finalDate;
			}
			
			if (dataFilter.test(costRecord.getUserData())) {
				costOfImpressions += costRecord.getCost();
			}
		}
		
		// add last entry
		costList.add(costOfImpressions);

		// pack
		costList.trimToSize();
				
		return costList;
	}
	
	// I'm assuming this is cost of impression and click
	private final List<Double> totalCost() {
		final List<Double> impressionsCost = costOfRecord(campaign.getImpressions());
		final List<Double> clicksCost = costOfRecord(campaign.getClicks());
		
		if (impressionsCost.size() != clicksCost.size())
			throw new IllegalArgumentException("totalCost: impressions and clicks not equal");
		
		final ArrayList<Double> costList = new ArrayList<Double>(impressionsCost.size());
		
		for (int i = 0; i < impressionsCost.size(); i++)
			costList.add(impressionsCost.get(i) + clicksCost.get(i));

		return costList;
	}
	
	// average clicks per impression
	private final List<Double> clickThroughRate() {
		final List<Integer> impressionsList = numberOfImpressions();
		final List<Integer> clicksList = numberOfClicks();
		
		if (impressionsList.size() != clicksList.size())
			System.err.println("CTR: this shouldn't happen");
		
		final ArrayList<Double> clickThroughRate = new ArrayList<Double>(impressionsList.size());
		
		for (int i = 0; i < impressionsList.size(); i++)
			clickThroughRate.add((double) clicksList.get(i) / (double) impressionsList.get(i));
		
		return clickThroughRate;
	}
	
	
	// The average amount of money spent on an advertising campaign
	// for each acquisition (i.e., conversion).
	private final List<Double> costPerAcquisition() {
		final List<Integer> conversionList = numberOfConversions();
		final List<Double> costList = totalCost();

		if (conversionList.size() != costList.size())
			System.err.println("CPA: this shouldn't happen");
		
		final ArrayList<Double> costPerAcquisition = new ArrayList<Double>(conversionList.size());
		
		for (int i = 0; i < conversionList.size(); i++)
			costPerAcquisition.add(costList.get(i) / conversionList.get(i));
		
		return costPerAcquisition;
	}
	
	// The average amount of money spent on an advertising campaign for each click.
	private final List<Double> costPerClick() {
		final List<Integer> clickList = numberOfClicks();
		final List<Double> costList = totalCost();
		
		if (clickList.size() != costList.size())
			System.err.println("CPC: this shouldn't happen");
		
		final ArrayList<Double> costPerAcquisition = new ArrayList<Double>(clickList.size());
		
		for (int i = 0; i < clickList.size(); i++)
			costPerAcquisition.add(costList.get(i) / clickList.get(i));
		
		return costPerAcquisition;
	}
	
	// The average amount of money spent on an advertising campaign for every one thousand impressions.
	private final List<Double> costPerThousandImpressions() {
		final List<Integer> impressionsList = numberOfImpressions();
		final List<Double> costsList = totalCost();
		
		if (impressionsList.size() != costsList.size())
			System.err.println("CPM: this shouldn't happen");
		
		final ArrayList<Double> costPerThousandImpressions = new ArrayList<Double>(impressionsList.size());
		
		for (int i = 0; i < impressionsList.size(); i++)
			costPerThousandImpressions.add(costsList.get(i) * 1000 / impressionsList.get(i));
		
		return costPerThousandImpressions;
	}
	
	// The average number of bounces per click.
	private final List<Double> bounceRate() {
		final List<Integer> bouncesList = numberOfBounces();
		final List<Integer> clicksList = numberOfClicks();
		
		if (bouncesList.size() != clicksList.size())
			System.err.println("Bounce Rate: this shoudn't happen");
		
		final ArrayList<Double> bounceRates = new ArrayList<Double>(bouncesList.size());
		
		for (int i = 0; i < bouncesList.size(); i++)
			bounceRates.add((double) bouncesList.get(i) / (double) clicksList.get(i));
		
		return bounceRates;
	}
}
