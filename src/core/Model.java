package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.swing.Timer;

import core.campaigns.Campaign;
import javafx.scene.chart.Chart;

public class Model extends Observable implements ActionListener {

	// ==== Constants ====


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
		
		new Campaign(new File("/Users/khengboonpek/Downloads/2_week_campaign_2"));
		new Campaign(new File("/Users/khengboonpek/Downloads/2_month_campaign"));
	}
	
	// ==== Accessors ====
	
	
	// ==== Private Helper Methods ====

	
	// ==== ActionListener Implementation ====

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			setChanged();
			notifyObservers();
		}
	}

}
