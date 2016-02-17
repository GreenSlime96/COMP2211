package ui.controlelements;

import javax.swing.JComboBox;

import core.Controller;

/**
 * Created by james on 17/02/16.
 */
public class FilterTab extends ControlPanelTab {

    public FilterTab(Controller controller) {
    	super(controller);
//        JLabel filler = new JLabel("Filter");
//        filler.setHorizontalAlignment(JLabel.CENTER);
//
//        add(filler);

        String[] campaignStrings = { "Campaign 1", "Campaign 2"};
        JComboBox campaignList = new JComboBox(campaignStrings);
        addSetting(campaignList,"Campaign","Select a campaign for your chart");

        String[] metricStrings = { "Metric 1", "Metric 2"};
        JComboBox metricList = new JComboBox(metricStrings);
        addSetting(metricList,"Metrics","Select a metric for your chart");
    }

}