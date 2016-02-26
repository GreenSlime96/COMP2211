package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

import javax.swing.Timer;

import core.campaigns.Campaign;
import core.campaigns.InvalidCampaignException;
import core.data.DataProcessor;
import core.data.Metric;
import core.users.User;

public class Model extends Observable implements ActionListener {

	// ==== Properties ====

	// We may use this Timer to fire events as they occur
	// Use this Timer to update the Controller/View about the current query
	// status
	private final Timer timer = new Timer(1000, this);

	// The list of Campaigns registered with this model
	private final List<Campaign> campaigns = new ArrayList<Campaign>();

	// The list of Charts stored in this model
	private final List<DataProcessor> dataProcessors = new ArrayList<DataProcessor>();

	private DataProcessor currentProcessor = null;

	// ==== Constructor ====

	public Model() {
		super();

		// FOR TESTING ONLY
		final String username = System.getProperty("user.name");
		
		if (username.equals("khengboonpek") || username.equals("kbp2g14"))
			try {
				addCampaign(new File("/Users/" + username + "/Downloads/2_month_campaign"));
				currentProcessor = new DataProcessor();
				currentProcessor.setCampaign(campaigns.get(0));
				
				for (Metric m : Metric.values()) {
					currentProcessor.setMetric(m);
					currentProcessor.getData();
				}
			} catch (InvalidCampaignException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// ==== Accessors ====

	// ==== Chart Stuff ====
	
	/**
	 * index charts so that we are modular
	 * @return
	 */

	public synchronized final List<? extends Number> getChartData() {
		return currentProcessor.getData();
	}
	
	/**
	 * gets the index of the current dataProcessor
	 * 
	 * @return
	 */
	public synchronized final int getChart() {
		return dataProcessors.indexOf(currentProcessor);
	}
	
	public synchronized final void setChart(int index) {
		currentProcessor = dataProcessors.get(index);
	}
	
	/**
	 * adds a new chart to the system and returns the index to
	 * that chart reference
	 * 
	 * @return
	 */
	public synchronized final int addChart() {
		return -1;
	}

	// ==== General Tab ====

	/**
	 * Returns a list of Strings representing the campaigns in memory
	 * This feels unsafe as the VC can modify the list
	 * 
	 * @return a list of campaigns loaded in the system
	 */
	public synchronized final List<String> getListOfCampaigns() {
		return campaigns.stream().map(x -> x.toString()).collect(Collectors.toList());
	}

	/**
	 * @throws InvalidCampaignException 
	 * @throws IOException 
	 * adds a campaign to the list of campaigns and loads it, doesn't add if one
	 * alredy exists
	 * 
	 * @param campaignDirectory
	 *            the directory the campaign resides on
	 * @throws  
	 */
	public synchronized final void addCampaign(File campaignDirectory) throws InvalidCampaignException {
		final Campaign campaign = new Campaign(campaignDirectory);

		// skip if it already exists
		if (campaigns.contains(campaign)) {
			System.out.println("DEBUG: campaign exist, skipping");
			return;
		}

		// add and load data
		campaign.loadData();
		campaigns.add(campaign);

		setChanged();
		notifyObservers();
	}

	/**
	 * TODO: throw exception, need to notify user
	 * 
	 * @param campaign
	 */
	public synchronized final void removeCampaign(int index) {
		removeCampaign(campaigns.get(index));
	}
	
	public synchronized final void removeCampaign(Campaign campaign) {
		// iterate over DataProcessors to make sure there are no dependencies
		for (DataProcessor dataProcessor : dataProcessors) {
			if (dataProcessor.getCampaign().equals(campaign)) {
				System.out.println("DEBUG: campaign in use, skipping");
				return;
			}
		}

		// remove if naked
		campaigns.remove(campaign);

		setChanged();
		notifyObservers();
	}
	
	/**
	 * returns the campaign at that particular index
	 * 
	 * @param index - index of Campaign reference
	 * @return - campaign corresponding to the index
	 */
	public synchronized final Campaign getCampaign(int index) {
		return campaigns.get(index);
	}

	// ==== Chart Tab ====

	public synchronized final Campaign getCurrentCampaign() {
		return currentProcessor.getCampaign();
	}

	public synchronized final void setCurrentCampaign(int index) {
		final Campaign newCampaign = campaigns.get(index);
		
		// if campaign doesn't change just return
		if (currentProcessor.getCampaign().equals(newCampaign))
			return;
		
		currentProcessor.setCampaign(newCampaign);
		
		setChanged();
		notifyObservers();
	}

	public synchronized final Metric getCurrentMetric() {
		return currentProcessor.getMetric();
	}

	public synchronized final void setCurrentMetric(Metric metric) {
		if (currentProcessor.getMetric().equals(metric))
			return;
		
		currentProcessor.setMetric(metric);
		
		setChanged();
		notifyObservers();
	}

	public synchronized final LocalDateTime getStartDateTime() {
		return currentProcessor.getDataStartDateTime();
	}

	/**
	 * Sets the Start Date and Time for this Data Processor
	 * DateTime cannot be below or over Campaign
	 * 
	 * TODO checks
	 * 
	 * @param dataStartDateTime the dataStartDateTime of the processor
	 */
	public synchronized final void setStartDateTime(LocalDateTime dataStartDateTime) {
		currentProcessor.setDataStartDate(dataStartDateTime);
		
		setChanged();
		notifyObservers();
	}

	public synchronized final LocalDateTime getEndDateTime() {
		return currentProcessor.getDataEndDate();
	}

	/**
	 * 
	 * @param dataEndDateTime
	 */
	public synchronized final void setEndDateTime(LocalDateTime dataEndDateTime) {
		currentProcessor.setDataEndDate(dataEndDateTime);
		
		setChanged();
		notifyObservers();
	}

	public synchronized final int getTimeGranularityInSeconds() {
		return currentProcessor.getTimeGranularityInSeconds();
	}

	public synchronized final void setTimeGranularityInSeconds(int timeGranularityInSeconds) {
		currentProcessor.setTimeGranularityInSeconds(timeGranularityInSeconds);
		
		setChanged();
		notifyObservers();
	}

	public synchronized final int getBounceMinimumPagesViewed() {
		return currentProcessor.getBounceMinimumPagesViewed();
	}

	public synchronized final void setBounceMinimumPagesViewed(int bounceMinimumPagesViewed) {
		currentProcessor.setBounceMinimumPagesViewed(bounceMinimumPagesViewed);
		
		setChanged();
		notifyObservers();
	}

	public synchronized final int getBounceMinimumSecondsOnPage() {
		return currentProcessor.getBounceMinimumSecondsOnPage();
	}

	public synchronized final void setBounceMinimumSecondsOnPage(int bounceMinimumSecondsOnPage) {
		currentProcessor.setBounceMinimumSecondsOnPage(bounceMinimumSecondsOnPage);
		
		setChanged();
		notifyObservers();
	}

	// ==== Filter Tab ====

	public synchronized final boolean getFieldFilteredValue(User field) {
		return currentProcessor.getFieldFilteredValue(field);
	}

	public synchronized final void setFieldFilteredValue(User field, boolean value) {
		currentProcessor.setFieldFilterValue(field, value);
		
		setChanged();
		notifyObservers();
	}

	// ==== Private Helper Methods ====

	private void update() {
		setChanged();
		notifyObservers();
	}

	// ==== ActionListener Implementation ====

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			setChanged();
			notifyObservers();
		}
	}

}
