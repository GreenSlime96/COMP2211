package ui.controlelements;

import ui.controlelements.ControlPanelBox;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import core.Model;

/**
 * Created by james on 17/02/16.
 */
public class FilterTab extends ControlPanelBox {

	String[] campaignStrings = { "Campaign 1", "Campaign 2"};
	String[] metricStrings = { "Number of Impressions", "Number of Clicks", "Number of Uniques", "Number of Bounces",
			"Number of Conversions", "Total Cost", "CTR", "CPA", "CPC", "CPM","Bounce Rate"};

	JComboBox campaignList = new JComboBox(campaignStrings);
    JComboBox metricList = new JComboBox(metricStrings);

	String[] arr = {"Filter 1", "Filter 2"};
	JList<String> filterList= new JList<>(arr);

	JSpinner startTimeSpinner = new JSpinner( new SpinnerDateModel() );
	JSpinner endTimeSpinner = new JSpinner( new SpinnerDateModel() );
	JSpinner timeGranularitySpinner = new JSpinner( new SpinnerDateModel());

	JButton genderButton = new JButton();
	JButton incomeButton = new JButton();
	JButton contextButton = new JButton();
	JButton ageButton = new JButton();


    public FilterTab(Model model) {
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

		ArrayList<String> genders = new ArrayList<>();
		genders.add("Any"); genders.add("Male");	genders.add("Female");

		ArrayList<String> ages = new ArrayList<>();
		ages.add("Any"); ages.add("<25"); ages.add("25-34"); ages.add("35-44"); ages.add("45-54"); ages.add(">54");

		ArrayList<String> incomes = new ArrayList<>();
		incomes.add("Any"); incomes.add("Low"); incomes.add("Medium"); incomes.add("High");

		ArrayList<String> contexts = new ArrayList<>();
		contexts.add("Any"); contexts.add("News"); contexts.add("Shopping"); contexts.add("Social Media"); contexts.add("Blog");
		contexts.add("Hobbies"); contexts.add("Travel");


		setupMultipleDropdown(genders, genderButton);
		setupMultipleDropdown(ages, ageButton);
		setupMultipleDropdown(incomes, incomeButton);
		setupMultipleDropdown(contexts, contextButton);

		addSetting(campaignList,"Campaign","Select a campaign for your chart");
		addSetting(metricList,"Metrics","Select a metric for your chart");
		addSetting(filterList, "Filters", "Select a filter to edit its properties");

		addSetting(startTimeSpinner,"Start Time","Choose Start Time for Chart");
		addSetting(endTimeSpinner,"End Time","Choose End Time for Chart");
		addSetting(timeGranularitySpinner,"Time Granularity","Choose Time Granularity for Chart (dd HH:mm:ss)");

		addSetting(genderButton,"Gender","Filter by Gender");
		addSetting(ageButton,"Age","Filter by Age");
		addSetting(incomeButton,"Income","Filter by Income");
		addSetting(contextButton,"Context","Filter by Context");

    }

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public void setupMultipleDropdown(ArrayList<String> options, JButton button){

		JPopupMenu popupMenu = new JPopupMenu();

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!popupMenu.isVisible()) {
					Point p = button.getLocationOnScreen();
					popupMenu.setInvoker(button);
					popupMenu.setLocation((int) p.getX(),
							(int) p.getY() + button.getHeight());
					popupMenu.setVisible(true);
				} else {
					popupMenu.setVisible(false);
				}

			}
		});

		for (String s : options){
			JMenuItem item = new JCheckBoxMenuItem(s);
			popupMenu.add(item);
			item.addActionListener(new ClickAction(popupMenu, button, item));

		}

		popupMenu.setSelected(popupMenu.getComponent(0));
		JMenuItem menuItem = (JMenuItem) popupMenu.getComponent(0);
		menuItem.doClick();


	}

	private static class ClickAction implements ActionListener {

		private JPopupMenu menu;
		private JButton button;
		private JMenuItem item;
		private boolean active;
		private ClickAction(JPopupMenu menu, JButton button, JMenuItem item) {
			this.menu = menu;
			this.button = button;
			this.item = item;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
//			System.out.println("ACTION PERFORMED");
//			menu.show(button, 0, button.getHeight());
			active = (active) ? false : true;

			if(item == menu.getComponent(0)){
				for (int i = 1; i < menu.getComponents().length; i++) {
					JMenuItem menuItem = (JMenuItem) menu.getComponents()[i];
					if(menuItem.isSelected()) menuItem.doClick();
				}
				button.setText(item.getText());
			}else {


				if (active) {
					button.setText(button.getText() + " " + item.getText() + " ");
					button.setText(button.getText().replace("Any",""));
					JMenuItem menuItem = (JMenuItem) menu.getComponent(0);
					menuItem.setSelected(false);

				} else {
					button.setText(button.getText().replace(" " + item.getText() + " ", ""));
				}
			}
			button.invalidate();

		}
	}
}

