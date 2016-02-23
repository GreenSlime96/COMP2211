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

//    String[] arr = {"Campaign 1", "Campaign 2"};
    private JList<String> campaignList = new JList<String>();
    JLabel noImpressionsLabel = new JLabel("######");
    JLabel startDateLabel = new JLabel("######");
    JLabel endDateLabel = new JLabel("######");
    JLabel totalClicksLabel =  new JLabel("######");
    JLabel totalCostLabel = new JLabel("#####");
    JLabel campaignDirectoryLabel = new JLabel("######");

    private DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    GeneralTabController generalTabController;
    public GeneralTab(Model model){
    	super(model);

        Box addsubPanel = new Box(BoxLayout.X_AXIS);
        addsubPanel.add(addCampaignButton);
        addsubPanel.add(removeCampaignButton);
        //campaign metrics

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


    }

	@Override
	public void update(Observable o, Object arg) {

        System.out.println("General Tab Updating");
        ArrayList<Campaign> campaigns = (ArrayList<Campaign>) model.getListOfCampaigns();
        String[] nameArray = new String[campaigns.size()];
        for (Campaign c : campaigns){
            nameArray[campaigns.indexOf(c)] = c.getDirectoryPath();
        }
        campaignList.setListData(nameArray);

        Campaign c = model.getCurrentCampaign();
        campaignList.setSelectedIndex(campaigns.indexOf(c));

        noImpressionsLabel.setText(String.valueOf(c.getNumberOfImpressions()));
        startDateLabel.setText(c.getStartDateTime().toString());
        endDateLabel.setText(c.getEndDateTime().toString());
        totalClicksLabel.setText(String.valueOf(c.getCostOfClicks()));
        totalCostLabel.setText(String.valueOf(c.getTotalCostOfCampaign()));
        campaignDirectoryLabel.setText(String.valueOf(c.getDirectoryPath()));

	}

    class GeneralTabController implements ActionListener,
            ChangeListener, ItemListener, ListSelectionListener {

        public GeneralTabController(Model model){

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == addCampaignButton){
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                f.showSaveDialog(null);

                if (f.getSelectedFile() != null){
                    try {
                        model.addCampaign(f.getSelectedFile());
                    } catch (FileNotFoundException e1) {
                        JOptionPane.showMessageDialog(null, "Directory does not contain correct files");
                        e1.printStackTrace();
                    }
                }
            }else if (e.getSource() == removeCampaignButton){
                int selectedOption = JOptionPane.showConfirmDialog(null,
                        "Are you sure you wish to delete Campaign " + model.getCurrentCampaign().getDirectoryPath(),
                        "Choose",
                        JOptionPane.YES_NO_OPTION);
                if (selectedOption == JOptionPane.YES_OPTION) {
                    Campaign c = model.getListOfCampaigns().get(campaignList.getSelectedIndex());
                    model.removeCampaign(c);
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
            if(e.getSource() == campaignList){
                //stops event firing twice.
                if(!e.getValueIsAdjusting())
                model.setCurrentCampaign(model.getListOfCampaigns().get(campaignList.getSelectedIndex()));
            }
        }
    }


}
