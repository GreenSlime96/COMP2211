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

        campaignList = new JList<String>(arr);
        campaignList.setVisibleRowCount(4);
        Box addsubPanel = new Box(BoxLayout.X_AXIS);
        addsubPanel.add(addCampaignButton);
        addsubPanel.add(removeCampaignButton);

        addSetting(campaignList,"Campaigns","");
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


        ArrayList<String> campaignNames = (ArrayList<String>) model.getListOfCampaigns();
        DefaultListModel<String> campaignListModel = new DefaultListModel();

        for(String campaignName : campaignNames){
            campaignListModel.addElement(campaignName);
            System.out.println(campaignName);
        }

        campaignList.setModel(campaignListModel);

        if(model.getCurrentCampaign()!=null) {



            Campaign c = model.getCurrentCampaign();
//            campaignList.setSelectedIndex(campaigns.indexOf());

            noImpressionsLabel.setText(String.valueOf(c.getNumberOfImpressions()));
            startDateLabel.setText(c.getStartDateTime().toString());
            endDateLabel.setText(c.getEndDateTime().toString());
            totalClicksLabel.setText(String.valueOf(c.getCostOfClicks()));
            totalCostLabel.setText(String.valueOf(c.getTotalCostOfCampaign()));

            campaignDirectoryLabel.setText(c.toString());
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
                    f.showSaveDialog(null);

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
                            "Are you sure you wish to delete Campaign " ,
                            "Choose",
                            JOptionPane.YES_NO_OPTION);
                    if (selectedOption == JOptionPane.YES_OPTION) {
                        //TODO erasd
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
                        //TODO ;kj;lk
//                        model.setCurrentCampaign(model.getListOfCampaigns().get(campaignList.getSelectedIndex()));
                    }
                }
            }
        }
    }

}
