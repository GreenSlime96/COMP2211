package ui.controlelements;

import ui.controlelements.ControlPanelTab;

import javax.swing.*;

/**
 * Created by james on 17/02/16.
 */
public class FilterTab extends ControlPanelTab {

	String[] campaignStrings = { "Campaign 1", "Campaign 2"};
	String[] metricStrings = { "Metric 1", "Metric 2"};
	
	JComboBox campaignList = new JComboBox(campaignStrings);
    JComboBox metricList = new JComboBox(metricStrings);
    
    public FilterTab() {


        addSetting(campaignList,"Campaign","Select a campaign for your chart");
        addSetting(metricList,"Metrics","Select a metric for your chart");
    }

}