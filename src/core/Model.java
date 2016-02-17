package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.swing.Timer;

import core.campaigns.Campaign;
import core.data.DataProcessor;
import javafx.scene.chart.Chart;
import ui.controlelements.CampaignFileChooser;

public class Model extends Observable implements ActionListener {

	// ==== Constants ====

<<<<<<< HEAD
	private static final boolean CHOOSE_FILE_ON_STARTUP = true;
=======
>>>>>>> e9f120c2e17bc6c64b06f13bffa7f6761587f26e

	// ==== Properties ====
	
	// We may use this Timer to fire events as they occur
	// Use this Timer to update the Controller/View about the current query status
	private final Timer timer = new Timer(1000, this);
	
	// The list of Campaigns registered with this model
	private final List<Campaign> campaigns = new ArrayList<Campaign>();
	
	// The list of Charts stored in this model
//	private final List<Chart> charts = new ArrayList<Chart>();
	
	// ==== Constructor ====

	public Model() {
		super();
		
		Campaign test = null;
		
		// TODO temporary file picker
		if (CHOOSE_FILE_ON_STARTUP) {
			CampaignFileChooser chooser = new CampaignFileChooser();
			if (chooser.selectionMade()) {
				test = new Campaign(chooser.getSelectedFile());
				addCampaign(test);
			} else
				System.out.println("No Selection");
		}
		
		DataProcessor dp = new DataProcessor(test);
		dp.numberOfImpressions();
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
	
	// ==== Accessors ====

    public List<Campaign> getCampaigns(){
        return campaigns;
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
