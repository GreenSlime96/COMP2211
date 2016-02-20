package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import javax.swing.Timer;

import core.campaigns.Campaign;
import core.data.DataFilter;
import core.data.DataProcessor;
import ui.controlelements.CampaignFileChooser;

public class Model extends Observable implements ActionListener {

	// ==== Constants ====

	private static final boolean CHOOSE_FILE_ON_STARTUP = true;

	// ==== Properties ====
	
	// We may use this Timer to fire events as they occur
	// Use this Timer to update the Controller/View about the current query status
	private final Timer timer = new Timer(1000, this);
	
	// The current dataprocessor in use
	private DataProcessor dataProcessor;
	
	// The list of Campaigns registered with this model
	private final List<Campaign> campaigns = new ArrayList<Campaign>();
	
	// The list of Charts stored in this model
	private final List<DataProcessor> charts = new ArrayList<DataProcessor>();
	
	// ==== Constructor ====

//	public Model() {
//		super();
//		
//		Campaign test = null;
//		
//		// TODO temporary file picker
//		if (CHOOSE_FILE_ON_STARTUP) {
//			CampaignFileChooser chooser = new CampaignFileChooser();
//			if (chooser.selectionMade()) {
//				test = new Campaign(chooser.getSelectedFile());
//				addCampaign(test);
//				DataProcessor dp = new DataProcessor(test);
//				dp.numberOfImpressions();
//			} else
//				System.out.println("No Selection");
//		}
//	}
//	
	
	// ==== Accessors ====
	
	
	// Begin Logic 
	
	public DataFilter getFilter() {
		return null; //dataProcessor.getFilter();
	}
	
	public Campaign getCampaign() {
		return null;
	}

    public Collection<Campaign> getCampaigns(){
        return campaigns;
    }
    
	public boolean addCampaign(Campaign campaign) {
		for(Campaign c : campaigns)
			if(c.equals(campaign))
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
