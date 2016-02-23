package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import javax.swing.Timer;

import core.campaigns.Campaign;
import core.data.DataFilter;
import core.data.DataProcessor;
import core.data.User;
import ui.controlelements.CampaignFileChooser;

public class Model extends Observable implements ActionListener {

	// ==== Constants ====

	private static final boolean CHOOSE_FILE_ON_STARTUP = false;

	// ==== Properties ====
	
	// We may use this Timer to fire events as they occur
	// Use this Timer to update the Controller/View about the current query status
	private final Timer timer = new Timer(1000, this);
	
	// The list of Campaigns registered with this model
	private final List<Campaign> campaigns = new ArrayList<Campaign>();
	
	// The list of Charts stored in this model
	private final List<DataProcessor> charts = new ArrayList<DataProcessor>();
	
	private DataProcessor currentProcessor;
	private Campaign currentCampaign;
	
	// ==== Constructor ====

	public Model() {
		super();
		
		Campaign test = null;
		
		try {
			// TODO temporary file picker
			if (true) {
				CampaignFileChooser chooser = new CampaignFileChooser();
				if (chooser.selectionMade()) {
					test = new Campaign(chooser.getSelectedFile());
					addCampaign(test);
					currentProcessor = new DataProcessor(test);
					currentCampaign = test;
//					long time = System.currentTimeMillis();
//					List<Integer> list1 = dp.numberOfImpressions();
//					System.out.println(System.currentTimeMillis() - time);
//					time = System.currentTimeMillis();
//					List<Integer> list2 = dp.numberOfConversions();
//					System.out.println(System.currentTimeMillis() - time);
//					System.out.println(list1.size() + "\t" + list2.size());
//					System.exit(0);
				} else
					
					System.out.println("No Selection");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	// ==== Accessors ====
	
	
	// ==== Chart Stuff ====
	
	public final List<? extends Number> getChartData() {
		return currentProcessor.numberOfImpressions();
	}
	
	
	// ==== General Tab ====
	
    public final List<Campaign> getListOfCampaigns(){
        return campaigns;
    }
    
    public final void addCampaign(File campaignDirectory) throws FileNotFoundException {
    	campaigns.add(new Campaign(campaignDirectory));
    }
	
	public boolean addCampaign(Campaign campaign) {
		if (campaigns.contains(campaign))
			return false;
		
		campaigns.add(campaign);
		update();
		return true;		
	}
	
	public boolean removeCampaign(Campaign campaign) {
		boolean result = campaigns.remove(campaign);
		update();
		return result;
	}
	
	public final int getNumberOfImpressions() {
		return currentCampaign.getNumberOfImpressions();		
	}
	
	public final int getNumberOfClicks() {
		return currentCampaign.getNumberOfClicks();
	}
	
	public final int getNumberOfPagesViewed() {
		return currentCampaign.getNumberOfPagesViewed();
	}
	
	public final int getNumberOfConversions() {
		return currentCampaign.getNumberOfConversions();
	}
	
	public final LocalDateTime getCampaignStartDateTime() {
		return currentCampaign.getStartDateTime();
	}
	
	public final LocalDateTime getCampaignEndDateTime() {
		return currentCampaign.getEndDateTime();
	}
	
	
	// ==== Chart Tab ====
	
	public final Campaign getCurrentCampaign() {
		return currentProcessor.getCampaign();
	}
	
	public final void setCurrentCampaign(Campaign campaign) {
		currentCampaign = campaign;
	}
		
	public final int getCurrentMetric() {
		return 0;
	}
	
	public final void setCurrentMetric() {
		// TODO;
	}
	
	public final LocalDateTime getStartDateTime() {
		return currentProcessor.getDataStartDateTime();
	}
	
	public final void setStartDateTime(LocalDateTime dataStartDateTime) {
		currentProcessor.setDataStartDate(dataStartDateTime);
	}

	public final LocalDateTime getEndDateTime() {
		return currentProcessor.getDataEndDate();
	}
	
	public final void setEndDateTime(LocalDateTime dataEndDateTime) {
		currentProcessor.setDataEndDate(dataEndDateTime);
	}
	
	public final int getTimeGranularityInSeconds() {
		return currentProcessor.getTimeGranularityInSeconds();
	}
	
	public final void setTimeGranularityInSeconds(int timeGranularityInSeconds) {
		currentProcessor.setTimeGranularityInSeconds(timeGranularityInSeconds);
	}
	
	public final int getBounceMinimumPagesViewed() {
		return currentProcessor.getBounceMinimumPagesViewed();
	}
	
	public final void setBounceMinimumPagesViewed(int bounceMinimumPagesViewed) {
		currentProcessor.setBounceMinimumPagesViewed(bounceMinimumPagesViewed);
	}
	
	public final int getBounceMinimumSecondsOnPage() {
		return currentProcessor.getBounceMinimumSecondsOnPage();
	}
	
	public final void setBounceMinimumSecondsOnPage(int bounceMinimumSecondsOnPage) {
		currentProcessor.setBounceMinimumSecondsOnPage(bounceMinimumSecondsOnPage);
	}
	
	// ==== Filter Tab ====
	
	public final boolean getFieldFilteredValue(User field) {
		return currentProcessor.getFieldFilteredValue(field);
	}
	
	public final void setFieldFilteredValue(User field, boolean value) {
		currentProcessor.setFieldFilterValue(field, value);
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
