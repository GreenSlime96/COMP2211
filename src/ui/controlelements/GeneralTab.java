package ui.controlelements;


import java.awt.event.*;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.Model;
import core.campaigns.Campaign;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;


/**
 * Created by james on 17/02/16.
 */


public class GeneralTab extends ControlPanelBox {

    public enum FilterType {
        GENDER, AGE, INCOME, CONTEXT
    }

    private JButton removeCampaignButton = new JButton("-");
    private JButton addCampaignButton = new JButton("+");

    private JList<String> campaignList;
    private JLabel noImpressionsLabel = new JLabel(" ");
    private JLabel startDateLabel = new JLabel(" ");
    private JLabel endDateLabel = new JLabel(" ");
    private JLabel totalClicksLabel =  new JLabel(" ");
    private JLabel totalCostLabel = new JLabel(" ");
    private JLabel campaignDirectoryLabel = new JLabel(" ");

    private DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    GeneralTabController generalTabController;

    boolean active;

    public GeneralTab(Model model){
    	super(model);
        String[] arr = {"Try Pressing +"};

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setSize(200,100);

        campaignList = new JList<>(arr);
        campaignList.setVisibleRowCount(4);
        campaignList.setFixedCellHeight(20);
        campaignList.setFixedCellWidth(300);
        campaignList.setSize(200,100);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(campaignList);

        Box addsubPanel = new Box(BoxLayout.X_AXIS);
        addsubPanel.add(addCampaignButton);
        addsubPanel.add(removeCampaignButton);

        addSetting(scrollPane,"Campaigns","");
        addSetting(addsubPanel,"","");
        addSetting(noImpressionsLabel, "Impressions", " " );
        addSetting(startDateLabel, "Start Date", " " );
        addSetting(endDateLabel, "End Date", " " );
        addSetting(totalClicksLabel, "Total Clicks", " " );
        addSetting(totalCostLabel, "Total Cost", " ");
        addSetting(campaignDirectoryLabel, "Campaign Directory", " " );

        generalTabController = new GeneralTabController(model);

        campaignList.addListSelectionListener(generalTabController);
        addCampaignButton.addActionListener(generalTabController);
        removeCampaignButton.addActionListener(generalTabController);

        active = true;

    }

	@Override
	public void update(Observable o, Object arg) {

        active = false;

        ArrayList<String> campaignNames = (ArrayList<String>) model.getListOfCampaignNames();
        DefaultListModel<String> campaignListModel = new DefaultListModel();

        for(String campaignName : campaignNames){
            campaignListModel.addElement(campaignName);
            System.out.println(campaignName);
        }

        campaignList.setModel(campaignListModel);

        campaignList.setVisibleRowCount(4);
        campaignList.setFixedCellHeight(20);
        campaignList.setFixedCellWidth(300);

        if(campaignNames.size()>0) {
            ArrayList<Campaign> campaigns = (ArrayList<Campaign>) model.getListOfCampaigns();

            if(model.getCurrentCampaign() == -1){
                campaignList.setSelectedIndex(0);
            }else{
                campaignList.setSelectedIndex(model.getCurrentCampaign());
            }

            int selectedIndex = campaignList.getSelectedIndex();
            noImpressionsLabel.setText(String.valueOf(campaigns.get(selectedIndex).getNumberOfImpressions()));
            startDateLabel.setText(campaigns.get(selectedIndex).getStartDateTime().format(dateTimeFormatter));
            endDateLabel.setText(campaigns.get(selectedIndex).getEndDateTime().format(dateTimeFormatter));
            totalClicksLabel.setText(String.valueOf(campaigns.get(selectedIndex).getCostOfClicks()));

            double totalCostPennies = campaigns.get(selectedIndex).getTotalCostOfCampaign();

            totalCostLabel.setText("£" + String.format("%.2f",totalCostPennies/100));

            campaignDirectoryLabel.setText(campaigns.get(selectedIndex).toString());
        }
        active = true;
	}

    class GeneralTabController implements ActionListener,
            ChangeListener, ItemListener, ListSelectionListener {

        public GeneralTabController(Model model){

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(active) {
                if (e.getSource() == addCampaignButton) {
                    JFileChooser f = new JFileChooser();
                    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    f.showOpenDialog(null);

                    if (f.getSelectedFile() != null) {
                        try {
                            model.addCampaign(f.getSelectedFile());
                        } catch (FileNotFoundException e1) {
                            JOptionPane.showMessageDialog(null, "Directory does not contain correct files");
                            e1.printStackTrace();
                        }
                    }
                } else if (e.getSource() == removeCampaignButton) {
                    int selectedOption = JOptionPane.showConfirmDialog(null,
                            //TODO Get Current campaign
                            "Are you sure you wish to delete Campaign " + model.getListOfCampaigns().get(campaignList.getSelectedIndex()),
                            "Choose",
                            JOptionPane.YES_NO_OPTION);
                    if (selectedOption == JOptionPane.YES_OPTION) {
                        //TODO remove model
//                        Campaign c = model.getListOfCampaigns().get(campaignList.getSelectedIndex());
//                        model.removeCampaign(c);
                    }
                }
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {

        }

        @Override
        public void itemStateChanged(ItemEvent e) {

        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (active){
                if (e.getSource() == campaignList) {
                    //stops event firing twice.
                    if (!e.getValueIsAdjusting()) {
                        int selectedIndex = campaignList.getSelectedIndex();
                        ArrayList<Campaign> campaigns = (ArrayList<Campaign>) model.getListOfCampaigns();
                        noImpressionsLabel.setText(String.valueOf(campaigns.get(selectedIndex).getNumberOfImpressions()));
                        startDateLabel.setText(campaigns.get(selectedIndex).getStartDateTime().toString());
                        endDateLabel.setText(campaigns.get(selectedIndex).getEndDateTime().toString());
                        totalClicksLabel.setText(String.valueOf(campaigns.get(selectedIndex).getCostOfClicks()));
                        totalCostLabel.setText(String.valueOf(campaigns.get(selectedIndex).getTotalCostOfCampaign()));

                        campaignDirectoryLabel.setText(campaigns.get(selectedIndex).toString());
                    }
                }
            }
        }
    }

}
