package ui.controlelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import core.Controller;
import core.campaigns.Campaign;

/**
 * Created by james on 17/02/16.
 */
public class GeneralTab extends ControlPanelTab {
	
	private JList<Campaign> campaignList = new JList<Campaign>();
	
	private JButton removeCampaignBTN = new JButton("-");
	private JButton addCampaignBTN = new JButton("+");
	
    private JLabel empty = new JLabel("");
    private JLabel noImpressionsLabel = new JLabel("######");
    private JLabel startDateLabel = new JLabel("######");
    private JLabel endDateLabel = new JLabel("######");
    private JLabel totalClicksLabel =  new JLabel("######");
    private JLabel campaignDirectorLabel = new JLabel("######");

    public GeneralTab(Controller controller){
//        JLabel filler = new JLabel("General");
//        filler.setHorizontalAlignment(JLabel.CENTER);
//
    	//Campaign management
    	//campaignList.setListData(new String[]{"test", "test2"});
    	campaignList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    	campaignList.setVisibleRowCount(4);
    	
    	JScrollPane campaignListScroller = new JScrollPane(campaignList);
        addSetting(campaignListScroller, "Campaigns","Click to show stats below");
        
        JPanel addSubtractCampaignsPanel = new JPanel();
        addSubtractCampaignsPanel.setLayout(new BoxLayout(addSubtractCampaignsPanel, BoxLayout.X_AXIS));
        addSubtractCampaignsPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        addSubtractCampaignsPanel.add(removeCampaignBTN);
        addSubtractCampaignsPanel.add(addCampaignBTN);
        add(addSubtractCampaignsPanel);
        
        addCampaignBTN.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CampaignFileChooser chooser = new CampaignFileChooser();
				if(chooser.selectionMade()) {
					if(!controller.getModel().addCampaign(new Campaign(chooser.getSelectedFile())))
						controller.showMessageDialog("Campaign has already been added");
				}else {
					controller.showMessageDialog("No campaign selected.");
				}
			}
        });
        
        removeCampaignBTN.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getModel().removeCampaign((Campaign) campaignList.getSelectedValue());
			}
        	
        });
        
        //campaign metrics
        addSetting(empty, "", "" );
        addSetting(noImpressionsLabel, "Impressions", "" );
        addSetting(startDateLabel, "Start Date", "" );
        addSetting(endDateLabel, "End Date", "" );
        addSetting(totalClicksLabel, "Total Clicks", "" );
        addSetting(campaignDirectorLabel, "Campaign Directory", "" );        
    }
    
    public void setCampaignListData(Campaign[] listData) {
    	campaignList.setListData(listData);
    }

}
