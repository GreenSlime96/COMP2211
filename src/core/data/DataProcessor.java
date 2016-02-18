package core.data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import core.campaigns.Campaign;
import core.records.Click;
import core.records.Impression;
import core.records.Server;
import core.records.User;

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
	
	// the time granularity of this dataprocessor
	private int timeGranularityInSeconds = 60;
	
	// bounce logic
	private int bounceMinimumPagesViewed;
	private int bounceMinimumSecondsOnPage;
	
	// the list of filters to work with
	private DataFilter dataFilter;
	
	
	// ==== Constructor ====
	
	public DataProcessor(Campaign campaign) {
		setCampaign(campaign);
	}
	

	// ==== Accessors ====
	
	public final Campaign getCampaign() {
		return campaign;
	}
	
	public final void setCampaign(Campaign campaign) {
		final LocalDateTime campaignStartDate = campaign.getStartDate();
		final LocalDateTime campaignEndDate = campaign.getEndDate();		
		
		if (dataStartDate == null || dataStartDate.isBefore(campaignStartDate) || !dataStartDate.isBefore(campaignEndDate))
			dataStartDate = campaignStartDate;
		
		if (dataEndDate == null || dataEndDate.isAfter(campaignEndDate))
			dataEndDate = campaignEndDate;
		
		this.campaign = campaign;
	}
	
	
	public final LocalDateTime getDataStartDate() {
		return dataStartDate;
	}
	
	public final void setDataStartDate(LocalDateTime dataStartDate) {
		// check if the input start date happens before the campaign start date
		if (dataStartDate.isBefore(campaign.getStartDate()))
			throw new IllegalArgumentException("cannot set data start date before campaign starts");
		
		// check input happens before the campaign ends
		if (!dataStartDate.isBefore(campaign.getEndDate()))
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
		if (dataEndDate.isAfter(campaign.getEndDate()))
			throw new IllegalArgumentException("cannot set data end date to after campaign ends");
		
		// end date must be after the start date
		if (!dataEndDate.isAfter(campaign.getStartDate()))
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
			throw new IllegalArgumentException("something happened in " + this.getClass().getName());
		
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
	
	
	public final List<Integer> numberOfImpressions() {
		final List<Integer> impressionsList = new ArrayList<Integer>();
		
		int numberOfImpressions = 0;
		
		// initialise current date as startDate
		LocalDateTime currentDate = dataStartDate;
		LocalDateTime nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
		
		for (Impression impression : campaign.getImpressions()) {
			// we ignore the impression if the date is before the current date
			if (impression.date.isBefore(currentDate))
				continue;
			
			// add new mapping if after time granularity separator
			if (impression.date.isAfter(nextDate)) {
				
				// add to list
				impressionsList.add(numberOfImpressions);
				
				// stop processing, we have reached the end date
				if (nextDate.equals(dataEndDate))
					break;
				
				// reset and increment
				numberOfImpressions = 0;
				currentDate = nextDate;
				nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
				
				// normalise the date if exceeded
				if (nextDate.isAfter(dataEndDate))
					nextDate = dataEndDate;
			}
			
			if (dataFilter.apply(campaign.getUserFromID(impression.userID)))
				numberOfImpressions++;
		}
		
		return impressionsList;
	}
	
	public final List<Integer> numberOfClicks() {
		final List<Integer> clicksList = new ArrayList<Integer>();
		
		int numberOfClicks = 0;
		
		// initialise current date as startDate
		LocalDateTime currentDate = dataStartDate;
		LocalDateTime nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
		
		for (Click click : campaign.getClicks()) {
			// ignore if before current date -- in reality we need to throw errors
			if (click.date.isBefore(currentDate))
				continue;
			
			// add new entry if bounds exceeded
			if (click.date.isAfter(nextDate)) {
				clicksList.add(numberOfClicks);
				
				// break off if at the end
				if (nextDate.equals(dataEndDate))
					break;
				
				// reset
				numberOfClicks = 0;
				currentDate = nextDate;
				nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
				
				// normalise
				if (nextDate.isAfter(dataEndDate))
					nextDate = dataEndDate;
			}
			
			// apply filter
			if (dataFilter.apply(campaign.getUserFromID(click.userID)))
				numberOfClicks++;
		}
		
		return clicksList;
	}
	
	// The number of unique users that click on an ad during the course of a campaign.
	public final List<Integer> numberOfUniques() {
		final List<Integer> uniquesList = new ArrayList<Integer>();
		
		// initialise current date as startDate
		LocalDateTime currentDate = dataStartDate;
		LocalDateTime nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
		
		// store unique users here
		// TODO: decide hash user or hash Long,
		// kbp2g14 - I think User is better as we don't need to instantiate a new Long object
		Set<User> userSet = new HashSet<User>();
		
		for (Click click : campaign.getClicks()) {
			// ignore if before current date -- in reality we need to throw errors
			if (click.date.isBefore(currentDate))
				continue;
			
			// add new entry if bounds exceeded
			if (click.date.isAfter(nextDate)) {
				uniquesList.add(userSet.size());
				
				// break off if at the end
				if (nextDate.equals(dataEndDate))
					break;
				
				// resets variables
				currentDate = nextDate;
				nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
				
				userSet.clear();
				
				// normalise
				if (nextDate.isAfter(dataEndDate))
					nextDate = dataEndDate;
			}
			
			final User user = campaign.getUserFromID(click.userID);
			
			// apply filter
			if (dataFilter.apply(user))
				userSet.add(user);
		}
		
		return uniquesList;
	}
	
	/*
	 * A user clicks on an ad, but then fails to interact with the website
	 * (typically detected when a user navigates away from the website after a
	 * short time, or when only a single page has been viewed).
	 */
	public final List<Integer> numberOfBounces() {
		final List<Integer> bouncesList = new ArrayList<Integer>();
		
		// initialise current date as startDate
		LocalDateTime currentDate = dataStartDate;
		LocalDateTime nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
		
		int numberOfBounces = 0;
		
		for (Server server : campaign.getServer()) {
			
			// ignore if before current date
			if (server.entryDate.isBefore(currentDate))
				continue;
			
			// partition if new segment
			if (server.entryDate.isAfter(nextDate)) {
				
				// add to arraylist
				bouncesList.add(numberOfBounces);
				
				// break if at the end
				if (nextDate.equals(dataEndDate))
					break;
				
				// reset
				numberOfBounces = 0;
				
				// normalise
				if (nextDate.isAfter(dataEndDate))
					nextDate = dataEndDate;
			}
			
			if (dataFilter.apply(campaign.getUserFromID(server.userID))) {
				// register bounce if pages Viewed is less than or equal to threshold
				if (server.pagesViewed > bounceMinimumPagesViewed)
					continue;
				
				// if exitDate is null, we cannot compare so we continue
				if (server.exitDate == null)
					continue;
				
				// if time between entry and exit is greater than threshold
				if (ChronoUnit.SECONDS.between(server.entryDate, server.exitDate) > bounceMinimumSecondsOnPage)
					continue;
				
				numberOfBounces++;
			}
		}
		
		return bouncesList;
	}
	
	public final List<Integer> numberOfConversions() {
		final List<Integer> conversionsList = new ArrayList<Integer>();
		
		// initialise current date as startDate
		LocalDateTime currentDate = dataStartDate;
		LocalDateTime nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
		
		int numberOfConversions = 0;
		
		for (Server server : campaign.getServer()) {
			
			// ignore if before current date
			if (server.entryDate.isBefore(currentDate))
				continue;
			
			// partition if new segment
			if (server.entryDate.isAfter(nextDate)) {
				
				// add to arraylist
				conversionsList.add(numberOfConversions);
				
				// break if at the end
				if (nextDate.equals(dataEndDate))
					break;
				
				// reset
				numberOfConversions = 0;
				
				// normalise
				if (nextDate.isAfter(dataEndDate))
					nextDate = dataEndDate;
			}
			
			// TODO: conversion check, THEN apply?
			if (dataFilter.apply(campaign.getUserFromID(server.userID))) {
				if (server.conversion)
					numberOfConversions++;
			}
			
		}
		
		return conversionsList;
	}
	
	// I'm assuming this is cost of impression and click
	public final List<Double> totalCost() {
		final List<Double> impressionsCost = new ArrayList<Double>();
		final List<Double> clicksCost = new ArrayList<Double>();
		final List<Double> costList = new ArrayList<Double>();
		
		// initialise current date as startDate
		LocalDateTime currentDate = dataStartDate;
		LocalDateTime nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
		
		// store costs in temporary variable
		double cost = 0;
		
		// compute impressions cost
		for (Impression impression : campaign.getImpressions()) {
			if (impression.date.isBefore(currentDate))
				continue;
			
			if (impression.date.isAfter(nextDate)) {
				impressionsCost.add(cost);
				
				if (nextDate.equals(dataEndDate))
					break;
				
				cost = 0;
				currentDate = nextDate;
				nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
				
				if (nextDate.isAfter(dataEndDate))
					nextDate = dataEndDate;
			}
			
			if (dataFilter.apply(campaign.getUserFromID(impression.userID)))
				cost += impression.cost;
		}
		
		// reset variables for next compute
		cost = 0;
		currentDate = dataStartDate;
		nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
		
		// compute click cost
		for (Click click : campaign.getClicks()) {
			if (click.date.isBefore(currentDate))
				continue;
			
			if (click.date.isAfter(nextDate)) {
				clicksCost.add(cost);
				
				if (nextDate.equals(dataEndDate))
					break;
				
				cost = 0;
				currentDate = nextDate;
				nextDate = currentDate.plusSeconds(timeGranularityInSeconds);
				
				if (nextDate.isAfter(dataEndDate))
					nextDate = dataEndDate;
			}
			
			if (dataFilter.apply(campaign.getUserFromID(click.userID)))
				cost += click.cost;
		}
		
		// sum two costs, I tried to use streams but it won't work
		// reason being two lists could be of unequal length
		// TODO: unchecked/checked exceptions
		if (impressionsCost.size() != clicksCost.size())			
			System.err.println("totalCost error on impressionsCost and clicksCost size");
		
		
		
		
		return costList;
	}
	
	public final Map<LocalDateTime, Double> CTR() {
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
}
