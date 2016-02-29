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
	private Label clickThroughRate;
	
	@FXML
	private Label costPerAcquisition;
	
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
		numberOfBounces.setText(Integer.toString(campaign.getNumberOfBounces()));
		numberOfConversions.setText(Integer.toString(campaign.getNumberOfConversions()));
		totalCost.setText("£" + Double.toString(campaign.getTotalCostOfCampaign() / 100).substring(0, Double.toString(campaign.getTotalCostOfCampaign() / 100).indexOf('.') + 3));
		clickThroughRate.setText(Double.toString(campaign.getClickThroughRate()).substring(0, Double.toString(campaign.getClickThroughRate()).indexOf('.') + 3));
		costPerAcquisition.setText("£" + Double.toString(campaign.getCostPerAcquision()).substring(0, Double.toString(campaign.getCostPerAcquision()).indexOf('.') + 3));
		costPerClick.setText("£" + Double.toString(campaign.getCostPerClick()).substring(0, Double.toString(campaign.getCostPerClick()).indexOf('.') + 3 ));
		costPerThousandImpressions.setText("£" + Double.toString(campaign.getCostPerThousandImpressions()).substring(0,  Double.toString(campaign.getCostPerThousandImpressions()).indexOf('.') + 3));
		bounceRate.setText(Double.toString(campaign.getBounceRate()));
	}
}
