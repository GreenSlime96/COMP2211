package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

import javax.swing.Timer;

import core.campaigns.Campaign;
import core.data.DataProcessor;
import core.data.User;

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
	private Campaign currentCampaign = null;

	// ==== Constructor ====

	public Model() {
		super();

		// FOR TESTING ONLY
		final String username = System.getProperty("user.name");
		
		if (username.equals("khengboonpek") || username.equals("kbp2g14"))
			try {
				addCampaign(new File("/Users/khengboonpek/Downloads/2_month_campaign"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// ==== Accessors ====

	// ==== Chart Stuff ====
	
//	public synchronized final void 

	public synchronized final List<? extends Number> getChartData() {
		return currentProcessor.getData();
	}

	// ==== General Tab ====

	/**
	 * Returns a list of Strings representing the campaigns in memory
	 * 
	 * @return a list of campaigns loaded in the system
	 */
	public synchronized final List<String> getListOfCampaigns() {
		return campaigns.stream().map(x -> x.toString()).collect(Collectors.toList());
	}

	/**
	 * adds a campaign to the list of campaigns and loads it, doesn't add if one
	 * alredy exists
	 * 
	 * @param campaignDirectory
	 *            the directory the campaign resides on
	 * @throws FileNotFoundException
	 *             if the Campaign Directory isn't a valid directory
	 */
	public synchronized final void addCampaign(File campaignDirectory) throws FileNotFoundException {
		final Campaign campaign = new Campaign(campaignDirectory);

		// skip if it already exists
		if (campaigns.contains(campaign)) {
			System.out.println("DEBUG: campaign exist, skipping");
			return;
		}

		// add and load data
		campaigns.add(campaign);
		
		new Thread() {
			@Override
			public void run() {
				campaign.loadData();
			}
		}.start();


		setChanged();
		notifyObservers();
	}

	/**
	 * 
	 * @param campaign
	 */
	public synchronized final void removeCampaign(Campaign campaign) {

		// iterate over DataProcessors to make sure there are no dependencies
		// TODO: throw exception instead
		for (DataProcessor dataProcessor : dataProcessors) {
			if (dataProcessor.getCampaign().equals(campaign))
				return;
		}

		campaigns.remove(campaign);

		setChanged();
		notifyObservers();
	}

	public synchronized final int getNumberOfImpressions() {
		return currentCampaign.getNumberOfImpressions();
	}

	public synchronized final int getNumberOfClicks() {
		return currentCampaign.getNumberOfClicks();
	}

	public synchronized final int getNumberOfPagesViewed() {
		return currentCampaign.getNumberOfPagesViewed();
	}

	public synchronized final int getNumberOfConversions() {
		return currentCampaign.getNumberOfConversions();
	}

	public synchronized final LocalDateTime getCampaignStartDateTime() {
		return currentCampaign.getStartDateTime();
	}

	public synchronized final LocalDateTime getCampaignEndDateTime() {
		return currentCampaign.getEndDateTime();
	}

	// ==== Chart Tab ====

	public synchronized final Campaign getCurrentCampaign() {
		if(currentProcessor == null){
			return null;
		}
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
