package view;

import core.campaigns.Campaign;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class CampaignOverviewController {
	
	// ==== Controller ====
	
	@FXML
	private TitledPane titledPane;
	
	@FXML
	private Label numberOfImpressions;
	
	@FXML
	private Label numberOfClicks;
	
	@FXML
	private Label numberOfUniques;
	
	@FXML
	private Label numberOfBounces;
	
	@FXML
	private Label numberOfConversions;
	
	@FXML
	private Label totalCost;
	
	@FXML
	private Label clickThroughRage;
	
	@FXML
	private Label costPerAcquistion;
	
	@FXML
	private Label costPerClick;
	
	@FXML
	private Label costPerThousandImpressions;
	
	@FXML
	private Label bounceRate;
	
	
	// ==== Constructor ====
	
	public CampaignOverviewController() {
		
	}
	
	@FXML
	private void initialize() {
		
	}
	
	
	// ==== Accessors ====
	
	public void setCampaign(Campaign campaign) {
		titledPane.setText(campaign.toString());
		
		numberOfImpressions.setText(Integer.toString(campaign.getNumberOfImpressions()));
		numberOfClicks.setText(Integer.toString(campaign.getNumberOfClicks()));
		numberOfUniques.setText(Integer.toString(campaign.getNumberOfUniques()));
		// TODO numberOfBounces
		numberOfConversions.setText(Integer.toString(campaign.getNumberOfConversions()));
		totalCost.setText(Integer.toString((int) (campaign.getTotalCostOfCampaign() / 100)));
		
		
	}
}
