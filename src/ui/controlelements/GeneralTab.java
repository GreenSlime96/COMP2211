package ui.controlelements;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.Model;
import core.campaigns.Campaign;

import ui.controlelements.ControlPanelBox;

import javax.swing.*;

import core.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;


/**
 * Created by james on 17/02/16.
 */


public class GeneralTab extends ControlPanelBox {

    private JButton removeCampaignBTN = new JButton("-");
    private JButton addCampaignBTN = new JButton("+");
//    JLabel empty = new JLabel("");
    String[] arr = {"Campaign 1", "Campaign 2"};
    private JList<Campaign> campaignList = new JList<Campaign>();
    JLabel noImpressionsLabel = new JLabel("######");
    JLabel startDateLabel = new JLabel("######");
    JLabel endDateLabel = new JLabel("######");
    JLabel totalClicksLabel =  new JLabel("######");
    JLabel totalCostLabel = new JLabel("#####");
    JLabel campaignDirectoryLabel = new JLabel("######");

    private DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public GeneralTab(Model model){
    	super(model);

        addSetting(campaignList,"Campaigns","Click to show stats below");
        addCampaignBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CampaignFileChooser chooser = new CampaignFileChooser();
				if(chooser.selectionMade()) {
//					if(!model.addCampaign(new Campaign(chooser.getSelectedFile())))
//						controller.showMessageDialog("Campaign has already been added");
				}else {
//					controller.showMessageDialog("No campaign selected.");
				}
			}
        });
        
        removeCampaignBTN.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.removeCampaign((Campaign) campaignList.getSelectedValue());
			}
        	
        });
        
        //campaign metrics
        addSetting(noImpressionsLabel, "Impressions", "" );
        addSetting(startDateLabel, "Start Date", "" );
        addSetting(endDateLabel, "End Date", "" );
        addSetting(totalClicksLabel, "Total Clicks", "" );
        addSetting(totalCostLabel, "Total Cost", "");
        addSetting(campaignDirectoryLabel, "Campaign Directory", "" );        

    }

    private void populateCampaignData()
    {
    	if(campaignList.getSelectedValue() == null)
    		return;

    	Campaign campaign = campaignList.getSelectedValue();

    	noImpressionsLabel.setText(""+campaign.getNumberOfImpressions());
    	startDateLabel.setText(campaign.getStartDateTime().format(dateTimeFormatter));
    	endDateLabel.setText(campaign.getEndDateTime().format(dateTimeFormatter));
    	totalClicksLabel.setText(""+campaign.getNumberOfClicks());
    	totalCostLabel.setText(("�"+new DecimalFormat("#.##").format(campaign.getTotalCostOfCampaign())));
    	campaignDirectoryLabel.setText(campaign.getDirectoryPath());
    }
    
    public void setCampaignListData(Campaign[] listData) {
    	campaignList.setListData(listData);
    	campaignList.setSelectedIndex(campaignList.getModel().getSize()-1);
    }
    

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
