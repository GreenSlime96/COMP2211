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

import core.Metric;
import core.Model;
import core.campaigns.Campaign;

/**
 * Created by james on 17/02/16.
 */
public class ChartTab extends ControlPanelBox {

	String[] metricStrings = { Metric.NUMBER_OF_IMPRESSIONS.toString(), Metric.NUMBER_OF_CLICKS.toString(), Metric.NUMBER_OF_UNIQUES.toString(),
	Metric.NUMBER_OF_BOUNCES.toString(), Metric.NUMBER_OF_CONVERSIONS.toString(), Metric.TOTAL_COST.toString(), Metric.CLICK_THROUGH_RATE.toString(),
	Metric.COST_PER_ACQUISITION.toString(), Metric.COST_PER_CLICK.toString(), Metric.COST_PER_THOUSAND_IMPRESSIONS.toString() ,Metric.BOUNCE_RATE.toString()};

	JComboBox campaignComboBox = new JComboBox();
    JComboBox metricComboBox = new JComboBox(metricStrings);

	JSpinner startTimeSpinner = new JSpinner( new SpinnerDateModel() );
	JSpinner endTimeSpinner = new JSpinner( new SpinnerDateModel() );

	String[] granularityStrings = {"Weekly","Daily","Hourly"};
	JComboBox timeGranularityComboBox = new JComboBox(granularityStrings);
	ChartTabController chartTabController;

	boolean active;

    public ChartTab(Model model) {
    	super(model);

		JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "dd-MMM-yyyy HH:mm:ss");
		startTimeSpinner.setEditor(startTimeEditor);

		JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "dd-MMM-yyyy HH:mm:ss");
		endTimeSpinner.setEditor(endTimeEditor);

		addSetting(campaignComboBox,"Campaign","Select a campaign for your chart");
		addSetting(metricComboBox,"Metrics","Select a metric for your chart");

		//addSetting(startTimeSpinner,"Start Time","Choose Start Time for Chart");
		//addSetting(endTimeSpinner,"End Time","Choose End Time for Chart");
		addSetting(timeGranularityComboBox,"Time Granularity","");

		chartTabController = new ChartTabController(model);

		registerWithController();
		active = true;
    }

	public void registerWithController(){

		campaignComboBox.addActionListener(chartTabController);
		metricComboBox.addActionListener(chartTabController);
		timeGranularityComboBox.addActionListener(chartTabController);

		startTimeSpinner.addChangeListener(chartTabController);
		endTimeSpinner.addChangeListener(chartTabController);

	}

	@Override
	public synchronized void update(Observable o, Object arg) {

		active = false;

		ArrayList<String> campaigns = (ArrayList<String>) model.getListOfCampaignNames();
		campaignComboBox.removeAllItems();

		for (String campaignName : campaigns) {
			campaignComboBox.addItem(campaignName);
		}

		if(model.getCurrentCampaign()!=-1) {
			if (o == model) {

				campaignComboBox.setSelectedIndex(model.getCurrentCampaign());
				metricComboBox.setSelectedItem(model.getCurrentMetric().toString());

				Instant startInstant = model.getStartDateTime().toInstant(ZoneOffset.UTC);
				Date startDate = Date.from(startInstant);
				startTimeSpinner.setValue(startDate);

				Instant endInstant = model.getEndDateTime().toInstant(ZoneOffset.UTC);
				Date endDate = Date.from(endInstant);
				startTimeSpinner.setValue(endDate);

				int timeGranularityInSeconds = model.getTimeGranularityInSeconds();
				if (timeGranularityInSeconds > 500000) {
					timeGranularityComboBox.setSelectedIndex(0);
				} else if (timeGranularityInSeconds > 70000) {
					timeGranularityComboBox.setSelectedIndex(1);
				} else {
					timeGranularityComboBox.setSelectedIndex(2);
				}

			}
		}else{
			if(model.getListOfCampaigns().size()>0){
				Campaign c = model.getListOfCampaigns().get(0);

				Instant startInstant = c.getStartDateTime().toInstant(ZoneOffset.UTC);
				Date startDate = Date.from(startInstant);
				startTimeSpinner.setValue(startDate);

				Instant endInstant = c.getEndDateTime().toInstant(ZoneOffset.UTC);
				Date endDate = Date.from(endInstant);
				startTimeSpinner.setValue(endDate);

				model.setCurrentMetric(Metric.toMetric((String) metricComboBox.getItemAt(0)));

			}
		}

		active = true;

	}

	class ChartTabController implements ActionListener,
			ChangeListener, ItemListener, ListSelectionListener {

		private Model model;

		public ChartTabController(Model model){
			this.model = model;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (active){
				System.out.println("ACTION PERFORMED");
				if(e.getSource() == campaignComboBox){
					if(campaignComboBox.getSelectedIndex() >= 0)
					model.setCurrentCampaign(campaignComboBox.getSelectedIndex());
				}else if (e.getSource() == metricComboBox){
					if(metricComboBox.getSelectedIndex() >= 0)
					model.setCurrentMetric(Metric.toMetric((String) metricComboBox.getSelectedItem()));
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
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if(active) {
				if (e.getSource() == startTimeSpinner) {
					Date startDate = (Date) startTimeSpinner.getValue();
					Instant startInstant = Instant.ofEpochMilli(startDate.getTime());
					LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);

					model.setStartDateTime(startLocalDateTime);
				} else if (e.getSource() == endTimeSpinner) {
					Date endDate = (Date) endTimeSpinner.getValue();
					Instant endInstant = Instant.ofEpochMilli(endDate.getTime());
					LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);

					model.setEndDateTime(endLocalDateTime);
				}
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

