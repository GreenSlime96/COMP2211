package core.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import core.campaigns.Campaign;
import core.tables.CostTable;
import core.tables.LogTable;
import core.users.User;
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
	private long dataStartDate;
	private long dataEndDate;
	
	// the filter to filter the metrics by
	private final DataFilter dataFilter;
	
	// the metric that the chart is handling
	private Metric metric;
	
	// the time granularity of this dataprocessor
	private int timeGranularityInSeconds;
	
	// bounce logic
	private int bounceMinimumPagesViewed;
	private int bounceMinimumSecondsOnPage;
	
	// misc stats (useful for checks)
	private int dataReturnSize;
	
	
	// ==== Constructor ====
	
	/**
	 * Constructor taking another DataProcessor, this will clone
	 * the properties of the DataProcessor
	 * 
	 * @param dataProcessor - the DataProcessor to clone by
	 */
	public DataProcessor(DataProcessor dataProcessor) {
		// sets the campaign
		campaign = dataProcessor.campaign;
		
		// assume validated dates
		dataStartDate = dataProcessor.dataStartDate;
		dataEndDate = dataProcessor.dataEndDate;
		
		// new DataFilter with same parameters, different objects
		// as user can modify this filter
		dataFilter = new DataFilter(dataProcessor.dataFilter);
		
		// same metrics
		metric = dataProcessor.metric;
		
		// time granularity
		timeGranularityInSeconds = dataProcessor.timeGranularityInSeconds;
		
		// bounce criteria
		bounceMinimumPagesViewed = dataProcessor.bounceMinimumPagesViewed;
		bounceMinimumSecondsOnPage = dataProcessor.bounceMinimumSecondsOnPage;		
	}
	
	public DataProcessor(Campaign campaign) {
		// set campaign SHOULD compute start and end dates
		setCampaign(campaign);	
		
		// create a new DataFilter (all enabled)
		dataFilter = new DataFilter();
		
		// set default metric
		metric = Metric.NUMBER_OF_IMPRESSIONS;
		
		// default time granularity - daily
		timeGranularityInSeconds = 60 * 60 * 24;
		
		// set bounce criteria
		bounceMinimumPagesViewed = 1;
		bounceMinimumSecondsOnPage = 30;	
	}
	

	// ==== Accessors ====
	
	public final Campaign getCampaign() {
		return campaign;
	}
	
	// TODO: adjust for time granularity here as well
	public final void setCampaign(Campaign campaign) {
		this.campaign = Objects.requireNonNull(campaign);
		
		final long campaignStartDate = DateProcessor.toEpochSeconds(campaign.getStartDateTime());
		final long campaignEndDate = DateProcessor.toEpochSeconds(campaign.getEndDateTime());
		
		if (dataStartDate < campaignStartDate || dataStartDate > campaignEndDate)
			dataStartDate = campaignStartDate;
		
		if (dataEndDate < campaignStartDate || dataEndDate > campaignEndDate)
			dataEndDate = campaignEndDate;
		
		System.out.println(DateProcessor.toLocalDateTime(dataStartDate));
	}
	
	public final Metric getMetric() {
		return metric;
	}
	
	/**
	 * sets the current dataProcessor's metric
	 * 
	 * @param metric - the metric to process on
	 */
	public final void setMetric(Metric metric) {
		this.metric = Objects.requireNonNull(metric);
	}
	
	/**
	 * method to get DataProcessor's Data depending on
	 * the metric selected
	 * 
	 * @return a list of numbers with data
	 */
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
		return DateProcessor.toLocalDateTime(dataStartDate);
	}
	
	public final void setDataStartDate(LocalDateTime dateTime) {
		final long campaignStartDate = DateProcessor.toEpochSeconds(campaign.getStartDateTime());
		final long campaignEndDate = DateProcessor.toEpochSeconds(campaign.getEndDateTime());
		
		final long newDateTime = DateProcessor.toEpochSeconds(dateTime);
		
		if (newDateTime < campaignStartDate)
			return;

		if (newDateTime >= campaignEndDate)
			return;
		
		if (newDateTime >= dataEndDate)
			return;
		
		// update the values if is valid
		dataStartDate = newDateTime;
	}
	
	
	public final LocalDateTime getDataEndDate() {
		return DateProcessor.toLocalDateTime(dataEndDate);
	}
	
	public final void setDataEndDate(LocalDateTime dateTime) {
		final long campaignStartDate = DateProcessor.toEpochSeconds(campaign.getStartDateTime());
		final long campaignEndDate = DateProcessor.toEpochSeconds(campaign.getEndDateTime());
		
		final long newDateTime = DateProcessor.toEpochSeconds(dateTime);
		
		if (newDateTime > campaignEndDate)
			return;
		
		if (newDateTime <= campaignStartDate)
			return;
		
		if (newDateTime <= dataStartDate)
			return;
		
		// update if valid
		dataEndDate = newDateTime;
	}
	
	
	public final int getTimeGranularityInSeconds() {
		return timeGranularityInSeconds;
	}
	
	public final void setTimeGranularityInSeconds(int timeGranularityInSeconds) {
		// check time granularity is at least 1
		if (timeGranularityInSeconds < 1)
			throw new IllegalArgumentException("cannot have time granularity below 1 second");
		
		// store the time difference to compute min/max bounds
		final long timeDifference = dataEndDate - dataStartDate;
		
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
	
	public final boolean getFilterValue(User field) {
		return dataFilter.getField(field);
	}
	public final void setFilterValue(User field, boolean value) {
		dataFilter.setField(field, value);
	}
	
	
	// ==== Compute Metrics ====	
	
	private final List<Integer> numberOfImpressions() {
		return numberOfRecord(campaign.getImpressions());
	}
	
	private final List<Integer> numberOfClicks() {
		return numberOfRecord(campaign.getClicks());
	}
	
	private final List<Integer> numberOfRecord(CostTable costTable) {
		final ArrayList<Integer> recordList = new ArrayList<Integer>();
		
		// initialise current date as startDate
		long currentDate = dataStartDate;
		long nextDate = currentDate + timeGranularityInSeconds;
		
		// reset counter
		int counter = 0;
		
		outerLoop:
		for (int i = 0; i < costTable.size(); i++) {
			final long dateTime = costTable.getDateTime(i);
						
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == dataEndDate)
					break outerLoop;
				
				recordList.add(counter);
				
				counter = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > dataEndDate)
					nextDate = dataEndDate;
			}
			
			if (dataFilter.test(costTable.getUserData(i))) {
				counter++;
			}
		}
		
		// add last entry
		recordList.add(counter);

		// pack
		recordList.trimToSize();
				
		return recordList;
	}
	
	// The number of unique users that click on an ad during the course of a campaign.
	private final List<Integer> numberOfUniques() {
		final ArrayList<Integer> uniquesList = new ArrayList<Integer>();
		final CostTable costTable = campaign.getClicks();
		
		// 330ms vs 668ms
		final TLongSet usersSet = new TLongHashSet();
		
		// initialise current date as startDate
		long currentDate = dataStartDate;
		long nextDate = currentDate + timeGranularityInSeconds;
				
		outerLoop:
		for (int i = 0; i < costTable.size(); i++) {
			final long dateTime = costTable.getDateTime(i);

			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;

			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == dataEndDate)
					break outerLoop;

				uniquesList.add(usersSet.size());

				usersSet.clear();

				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;

				if (nextDate > dataEndDate)
					nextDate = dataEndDate;
			}

			if (dataFilter.test(costTable.getUserData(i))) {
				usersSet.add(costTable.getUserID(i));
			}
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
		final LogTable logTable = campaign.getServers();
		
		int numberOfBounces = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate;
		long nextDate = currentDate + timeGranularityInSeconds;
				
		outerLoop:
		for (int i = 0; i < logTable.size(); i++) {
			final long dateTime = logTable.getDateTime(i);
						
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == dataEndDate)
					break outerLoop;
				
				bouncesList.add(numberOfBounces);
				
				numberOfBounces = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > dataEndDate)
					nextDate = dataEndDate;
			}
			
			if (dataFilter.test(logTable.getUserData(i))) {				
				if (logTable.getPagesViewed(i) > bounceMinimumPagesViewed)
					continue;
				
				final long exitDateTime = logTable.getExitDateTime(i);
				
				if (exitDateTime == DateProcessor.DATE_NULL)
					continue;
				
				if (exitDateTime - logTable.getDateTime(i) > bounceMinimumSecondsOnPage)
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
		final LogTable logTable = campaign.getServers();
		final ArrayList<Integer> conversionsList = new ArrayList<Integer>();
		
		int numberOfConversions = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate;
		long nextDate = currentDate + timeGranularityInSeconds;
				
		outerLoop:
		for (int i = 0; i < logTable.size(); i++) {
			final long dateTime = logTable.getDateTime(i);
						
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == dataEndDate)
					break outerLoop;
				
				conversionsList.add(numberOfConversions);
				
				numberOfConversions = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > dataEndDate)
					nextDate = dataEndDate;
			}
			
			// try to short circuit as expr1 is a direct boolean evaluation
			if (logTable.getConversion(i) && dataFilter.test(logTable.getUserData(i))) {
				numberOfConversions++;
			}
		}
		
		// add last entry
		conversionsList.add(numberOfConversions);

		// pack
		conversionsList.trimToSize();
		
		return conversionsList;
	}
	
	private final List<Double> costOfRecord(CostTable table) {
		final ArrayList<Double> costList = new ArrayList<Double>();
		
		double costOfImpressions = 0;
		
		// initialise current date as startDate
		long currentDate = dataStartDate;
		long nextDate = currentDate + timeGranularityInSeconds;
		
		outerLoop:
		for (int i = 0; i < table.size(); i++) {
			final long dateTime = table.getDateTime(i);
			
			// we ignore the impression if the date is before the current date
			if (dateTime < currentDate)
				continue;
			
			// add new mapping if after time granularity separator
			while (dateTime > nextDate) {
				if (nextDate == dataEndDate)
					break outerLoop;
				
				costList.add(costOfImpressions);
				
				costOfImpressions = 0;
				
				currentDate = nextDate;
				nextDate = currentDate + timeGranularityInSeconds;
				
				if (nextDate > dataEndDate)
					nextDate = dataEndDate;
			}
			
			if (dataFilter.test(table.getUserData(i))) {
				costOfImpressions += table.getCost(i);
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
				
		final ArrayList<Double> costList = new ArrayList<Double>(impressionsCost.size());
		
		for (int i = 0; i < impressionsCost.size(); i++)
			costList.add(impressionsCost.get(i) + clicksCost.get(i));

		return costList;
	}
	
	// average clicks per impression
	private final List<Double> clickThroughRate() {
		final List<Integer> impressionsList = numberOfImpressions();
		final List<Integer> clicksList = numberOfClicks();
		
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
		
		final ArrayList<Double> costPerAcquisition = new ArrayList<Double>(conversionList.size());
		
		for (int i = 0; i < conversionList.size(); i++)
			costPerAcquisition.add(costList.get(i) / conversionList.get(i));
		
		return costPerAcquisition;
	}
	
	// The average amount of money spent on an advertising campaign for each click.
	private final List<Double> costPerClick() {
		final List<Integer> clickList = numberOfClicks();
		final List<Double> costList = totalCost();
				
		final ArrayList<Double> costPerAcquisition = new ArrayList<Double>(clickList.size());
		
		for (int i = 0; i < clickList.size(); i++)
			costPerAcquisition.add(costList.get(i) / clickList.get(i));
		
		return costPerAcquisition;
	}
	
	// The average amount of money spent on an advertising campaign for every one thousand impressions.
	private final List<Double> costPerThousandImpressions() {
		final List<Integer> impressionsList = numberOfImpressions();
		final List<Double> costsList = totalCost();
		
		final ArrayList<Double> costPerThousandImpressions = new ArrayList<Double>(impressionsList.size());
		
		for (int i = 0; i < impressionsList.size(); i++)
			costPerThousandImpressions.add(costsList.get(i) * 1000 / impressionsList.get(i));
		
		return costPerThousandImpressions;
	}
	
	// The average number of bounces per click.
	private final List<Double> bounceRate() {
		final List<Integer> bouncesList = numberOfBounces();
		final List<Integer> clicksList = numberOfClicks();
		
		final ArrayList<Double> bounceRates = new ArrayList<Double>(bouncesList.size());
		
		for (int i = 0; i < bouncesList.size(); i++)
			bounceRates.add((double) bouncesList.get(i) / (double) clicksList.get(i));
		
		return bounceRates;
	}
	
	// Clicks are more valuable than impressions, always
	public final EnumMap<User, Integer> getContextData() {
		final EnumMap<User, Integer> enumMap = new EnumMap<User, Integer>(User.class);
		final CostTable costTable = campaign.getClicks();
		
		final int[] values = new int[User.values().length];
		
		for (int i = 0; i < costTable.size(); i++) {
			final short userData = costTable.getUserData(i);
			
			if (!dataFilter.test(userData))
				continue;
			
			for (User u : User.values()) {
				if (User.checkFlag(userData, u)) {
					values[u.ordinal()]++;
				}
			}
		}
		
		return enumMap;
	}
}
