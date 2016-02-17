package ui.controlelements;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * Created by james on 17/02/16.
 */
public class GeneralTab extends ControlPanelTab {

	private JTable campaignTable = new JTable(3, 1);
	
	private JButton removeCampaignBTN = new JButton("-");
	private JButton addCampaignBTN = new JButton("+");
	
    JLabel empty = new JLabel("");
    JLabel noImpressionsLabel = new JLabel("######");
    JLabel startDateLabel = new JLabel("######");
    JLabel endDateLabel = new JLabel("######");
    JLabel totalClicksLabel =  new JLabel("######");
    JLabel campaignDirectorLabel = new JLabel("######");

    public GeneralTab(){
//        JLabel filler = new JLabel("General");
//        filler.setHorizontalAlignment(JLabel.CENTER);
//
    	//Campaign management
        addSetting(campaignTable, "Campaigns","Click to show stats below");
        JPanel addSubtractCampaignsPanel = new JPanel();
        addSubtractCampaignsPanel.setLayout(new BoxLayout(addSubtractCampaignsPanel, BoxLayout.X_AXIS));
        addSubtractCampaignsPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        addSubtractCampaignsPanel.add(removeCampaignBTN);
        addSubtractCampaignsPanel.add(addCampaignBTN);
        add(addSubtractCampaignsPanel);
        
        
        //campaign metrics
        addSetting(empty, "", "" );
        addSetting(noImpressionsLabel, "Impressions", "" );
        addSetting(startDateLabel, "Start Date", "" );
        addSetting(endDateLabel, "End Date", "" );
        addSetting(totalClicksLabel, "Total Clicks", "" );
        addSetting(campaignDirectorLabel, "Campaign Directory", "" );
    }

}
