package ui.controlelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.Model;
import core.campaigns.Campaign;

/**
 * Created by james on 17/02/16.
 */
public class ChartTab extends ControlPanelBox {

//	String[] campaignStrings = { "Campaign 1", "Campaign 2"};
	String[] metricStrings = { "Number of Impressions", "Number of Clicks", "Number of Uniques", "Number of Bounces",
			"Number of Conversions", "Total Cost", "CTR", "CPA", "CPC", "CPM","Bounce Rate"};

	JComboBox campaignComboBox = new JComboBox();
    JComboBox metricComboBox = new JComboBox(metricStrings);

//	String[] arr = {"Filter 1", "Filter 2"};
//	JList<String> filterList= new JList<>(arr);

	JSpinner startTimeSpinner = new JSpinner( new SpinnerDateModel() );
	JSpinner endTimeSpinner = new JSpinner( new SpinnerDateModel() );

	String[] granularityStrings = {"Weekly","Daily","Hourly"};
	JComboBox timeGranularityComboBox = new JComboBox(granularityStrings);
	ChartTabController chartTabController;

    public ChartTab(Model model) {
    	super(model);

		JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "dd-MMM-yyyy HH:mm:ss");
		startTimeSpinner.setEditor(startTimeEditor);
		startTimeSpinner.setValue(new Date()); // will only show the current time

		JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "dd-MMM-yyyy HH:mm:ss");
		endTimeSpinner.setEditor(endTimeEditor);
		endTimeSpinner.setValue(new Date()); // will only show the current time

		addSetting(campaignComboBox,"Campaign","Select a campaign for your chart");
		addSetting(metricComboBox,"Metrics","Select a metric for your chart");

		addSetting(startTimeSpinner,"Start Time","Choose Start Time for Chart");
		addSetting(endTimeSpinner,"End Time","Choose End Time for Chart");
		addSetting(timeGranularityComboBox,"Time Granularity","");

		chartTabController = new ChartTabController(model);

		registerWithController();
    }

	public void registerWithController(){

		campaignComboBox.addActionListener(chartTabController);
		metricComboBox.addActionListener(chartTabController);
		timeGranularityComboBox.addActionListener(chartTabController);

//		filterList.addListSelectionListener(c);

		startTimeSpinner.addChangeListener(chartTabController);
		endTimeSpinner.addChangeListener(chartTabController);

	}

	@Override
	public void update(Observable o, Object arg) {

		System.out.println("General Tab Updating");
        if(o == model) {
            ArrayList<Campaign> campaigns = (ArrayList<Campaign>) model.getListOfCampaigns();
            campaignComboBox.removeAllItems();

            for (Campaign c : campaigns) {
                campaignComboBox.addItem(c.getDirectoryPath());
            }

            metricComboBox.setSelectedIndex(model.getCurrentMetric());

            Instant startInstant = model.getStartDateTime().toInstant(ZoneOffset.UTC);
            Date startDate = Date.from(startInstant);
            startTimeSpinner.setValue(startDate);

            Instant endInstant = model.getEndDateTime().toInstant(ZoneOffset.UTC);
            Date endDate = Date.from(endInstant);
            startTimeSpinner.setValue(endDate);

			int timeGranularityInSeconds = model.getTimeGranularityInSeconds();
			if(timeGranularityInSeconds>500000){
				timeGranularityComboBox.setSelectedIndex(0);
			}else if (timeGranularityInSeconds > 70000){
				timeGranularityComboBox.setSelectedIndex(1);
			}else{
				timeGranularityComboBox.setSelectedIndex(2);
			}

        }
		
	}

	class ChartTabController implements ActionListener,
			ChangeListener, ItemListener, ListSelectionListener {

		private Model model = null;

		public ChartTabController(Model model){
			model = model;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == campaignComboBox){
				model.setCurrentCampaign(model.getListOfCampaigns().get(campaignComboBox.getSelectedIndex()));
			}else if (e.getSource() == metricComboBox){
				model.setCurrentMetric();
			}else if (e.getSource() == timeGranularityComboBox){
				int timeGranularitySeconds = 0;
				switch (timeGranularityComboBox.getSelectedIndex()) {
					case 0: timeGranularitySeconds = 604800; break;
					case 1: timeGranularitySeconds = 86400; break;
					case 2: timeGranularitySeconds = 3600; break;
				}
				model.setTimeGranularityInSeconds(timeGranularitySeconds);
			}
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if(e.getSource() == startTimeSpinner){
				Date startDate = (Date) startTimeSpinner.getValue();
				Instant startInstant = Instant.ofEpochMilli(startDate.getTime());
				LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);

				model.setStartDateTime(startLocalDateTime);
			}else if (e.getSource() == endTimeSpinner) {
				Date endDate = (Date) endTimeSpinner.getValue();
				Instant endInstant = Instant.ofEpochMilli(endDate.getTime());
				LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);

				model.setEndDateTime(endLocalDateTime);
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {

		}

		@Override
		public void valueChanged(ListSelectionEvent e) {

		}
	}


}

