package ui.controlelements;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;
import javax.xml.crypto.Data;

import com.sun.javafx.font.Metrics;
import core.Model;
import core.campaigns.Campaign;
import core.data.DataProcessor;
import core.records.Impression;

/**
 * Created by james on 17/02/16.
 */
public class ChartTab extends ControlPanelBox {

	String[] campaignStrings = { "Campaign 1", "Campaign 2"};
	String[] metricStrings = { "Number of Impressions", "Number of Clicks", "Number of Uniques", "Number of Bounces",
			"Number of Conversions", "Total Cost", "CTR", "CPA", "CPC", "CPM","Bounce Rate"};

	JComboBox campaignComboBox = new JComboBox(campaignStrings);
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

//		filterList.addListSelectionListener(c);

		startTimeSpinner.addChangeListener(chartTabController);
		endTimeSpinner.addChangeListener(chartTabController);
//		timeGranularitySpinner.addChangeListener(chartTabController);

	}

	@Override
	public void update(Observable o, Object arg) {
        if(o == model) {
            ArrayList<Campaign> campaigns = (ArrayList<Campaign>) model.getCampaigns();
//            campaignComboBox.removeAllItems();
//
//            for (Campaign c : campaigns) {
//                campaignComboBox.addItem(c);
//            }
//
//            DataProcessor d = model.getActive();
//
//            int currentImpression = model.getMetric();
//            metricComboBox.setSelectedIndex(currentImpression);
//
//            Instant startInstant = d.getDataStartDate().toInstant(ZoneOffset.UTC);
//            Date startDate = Date.from(startInstant);
//            startTimeSpinner.setValue(startDate);
//
//            Instant endInstant = d.getDataEndDate().toInstant(ZoneOffset.UTC);
//            Date endDate = Date.from(endInstant);
//            startTimeSpinner.setValue(endDate);

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
				Campaign selectedCampaign = (Campaign) campaignComboBox.getSelectedItem();
				//TODO tell the damn model we have changed campaign for this chart
			}else if (e.getSource() == metricComboBox){
				String selectedMetric = (String) metricComboBox.getSelectedItem();
				//TODO tell the damn model we have changed metric
			}
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if(e.getSource() == startTimeSpinner){
				System.out.println("CHANGES");
				Date startDate = (Date) startTimeSpinner.getValue();
				Instant startInstant = Instant.ofEpochMilli(startDate.getTime());
				LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);

				//TODO update chart here
			}else if (e.getSource() == endTimeSpinner){
				System.out.println("CHANGES");
				Date endDate = (Date) endTimeSpinner.getValue();
				Instant endInstant = Instant.ofEpochMilli(endDate.getTime());
				LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);

				//TODO update chart here
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

