package ui.controlelements;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.crypto.Data;

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
	JSpinner timeGranularitySpinner = new JSpinner( new SpinnerDateModel());

	JButton genderButton = new JButton();
	JButton incomeButton = new JButton();
	JButton contextButton = new JButton();
	JButton ageButton = new JButton();


    public ChartTab(Model model) {
    	super(model);

		JSpinner.DateEditor startTimeEditor = new JSpinner.DateEditor(startTimeSpinner, "dd-MMM-yyyy HH:mm:ss");
		startTimeSpinner.setEditor(startTimeEditor);
		startTimeSpinner.setValue(new Date()); // will only show the current time

		JSpinner.DateEditor endTimeEditor = new JSpinner.DateEditor(endTimeSpinner, "dd-MMM-yyyy HH:mm:ss");
		endTimeSpinner.setEditor(endTimeEditor);
		endTimeSpinner.setValue(new Date()); // will only show the current time

		JSpinner.DateEditor timeGranularityEditor = new JSpinner.DateEditor(timeGranularitySpinner, "dd HH:mm:ss");
		timeGranularitySpinner.setEditor(timeGranularityEditor);
		timeGranularitySpinner.setValue(new Date()); // will only show the current time


		addSetting(campaignComboBox,"Campaign","Select a campaign for your chart");
		addSetting(metricComboBox,"Metrics","Select a metric for your chart");

		addSetting(startTimeSpinner,"Start Time","Choose Start Time for Chart");
		addSetting(endTimeSpinner,"End Time","Choose End Time for Chart");
		addSetting(timeGranularitySpinner,"Time Granularity","Choose Time Granularity for Chart (dd HH:mm:ss)");

		registerWithController(new FilterTabController(model));
    }

	public void registerWithController(FilterTabController c){

		campaignComboBox.addActionListener(c);
		metricComboBox.addActionListener(c);

//		filterList.addListSelectionListener(c);

		startTimeSpinner.addChangeListener(c);
		endTimeSpinner.addChangeListener(c);
		timeGranularitySpinner.addChangeListener(c);

	}

	@Override
	public void update(Observable o, Object arg) {
        if(o == model) {
//            ArrayList<Campaign> campaigns = (ArrayList<Campaign>) model.getCampaigns();
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

	class FilterTabController implements ActionListener,
			ChangeListener, ItemListener, ListSelectionListener {

		private Model model = null;

		public FilterTabController(Model model){
			model = model;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

		}

		@Override
		public void stateChanged(ChangeEvent e) {

		}

		@Override
		public void itemStateChanged(ItemEvent e) {

		}

		@Override
		public void valueChanged(ListSelectionEvent e) {

		}
	}


}

