package ui.controlelements;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.Model;
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
    private JLabel totalClicksLabel = new JLabel("######");
    private JLabel totalCostLabel = new JLabel("######"); 
    private JLabel campaignDirectoryLabel = new JLabel("######");
    
    private DateTimeFormatter dateTimeFormatter;

    public GeneralTab(Model model) {
    	super(model);
//        JLabel filler = new JLabel("General");
//        filler.setHorizontalAlignment(JLabel.CENTER);
//

    	campaignList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    	campaignList.setVisibleRowCount(4);
    	
    	JScrollPane campaignListScroller = new JScrollPane(campaignList);
        addSetting(campaignListScroller, "Campaigns","Click to show stats below");
        
        JPanel addSubtractCampaignsPanel = new JPanel();
        addSubtractCampaignsPanel.setLayout(new BoxLayout(addSubtractCampaignsPanel, BoxLayout.X_AXIS));
        addSubtractCampaignsPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        
        Dimension buttonSize = new Dimension(20, 20);
        
        removeCampaignBTN.setPreferredSize(buttonSize);
        addSubtractCampaignsPanel.add(removeCampaignBTN);
        
        addCampaignBTN.setPreferredSize(buttonSize);
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
					if(!model.addCampaign(new Campaign(chooser.getSelectedFile())))
						JOptionPane.showMessageDialog(null, "Campaign has already been added");
				}else {
					JOptionPane.showMessageDialog(null, "No campaign selected.");
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
        addSetting(empty, "", "" );
        addSetting(noImpressionsLabel, "Impressions", "" );
        addSetting(startDateLabel, "Start Date", "" );
        addSetting(endDateLabel, "End Date", "" );
        addSetting(totalClicksLabel, "Total Clicks", "" );
        addSetting(totalCostLabel, "Total Cost", "");
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
    	totalCostLabel.setText(("�"+new DecimalFormat("#.##").format(campaign.getTotalCostOfCampaign())));
    	campaignDirectoryLabel.setText(campaign.getDirectoryPath());
    }
    
    public void setCampaignListData(Campaign[] listData) {
    	campaignList.setListData(listData);
    	campaignList.setSelectedIndex(campaignList.getModel().getSize()-1);
    }
    
}
