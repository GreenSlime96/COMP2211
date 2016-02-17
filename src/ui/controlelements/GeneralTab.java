package ui.controlelements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

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
    private JLabel campaignDirectoryLabel = new JLabel("######");
    
    private DateTimeFormatter dateTimeFormatter;

    public GeneralTab(Controller controller) {
    	super(controller);
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
        
        campaignList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				populateCampaignData();		
			}
        	
        });
        
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
        addSetting(campaignDirectoryLabel, "Campaign Directory", "" );        
        
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }
    
    private void populateCampaignData()
    {
    	if(campaignList.getSelectedValue() == null)
    		return;
    	
    	Campaign campaign = campaignList.getSelectedValue();
    	
    	noImpressionsLabel.setText(""+campaign.getNumberOfImpressions());
    	startDateLabel.setText(campaign.getStartDate().format(dateTimeFormatter));
    	endDateLabel.setText(campaign.getEndDate().format(dateTimeFormatter));
    	totalClicksLabel.setText(""+campaign.getNumberOfClicks());
    	campaignDirectoryLabel.setText(campaign.getDirectoryPath());
    }
    
    public void setCampaignListData(Campaign[] listData) {
    	campaignList.setListData(listData);
    	campaignList.setSelectedIndex(campaignList.getModel().getSize()-1);
    }
    
}
