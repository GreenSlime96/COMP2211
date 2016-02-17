package ui.controlelements;

import ui.controlelements.ControlPanelBox;

import java.util.Observable;

import javax.swing.*;

import core.Model;

/**
 * Created by james on 17/02/16.
 */
public class FilterTab extends ControlPanelBox {

	String[] campaignStrings = { "Campaign 1", "Campaign 2"};
	String[] metricStrings = { "Metric 1", "Metric 2"};
	
	JComboBox campaignList = new JComboBox(campaignStrings);
    JComboBox metricList = new JComboBox(metricStrings);
    
    public FilterTab(Model model) {
    	super(model);
    	
        addSetting(campaignList,"Campaign","Select a campaign for your chart");
        addSetting(metricList,"Metrics","Select a metric for your chart");
    }

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}