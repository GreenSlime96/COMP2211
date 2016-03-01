package view;

import java.text.NumberFormat;

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
		
		final NumberFormat numberFormatter = NumberFormat.getInstance();
		final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		final NumberFormat percentFormatter = NumberFormat.getPercentInstance();
		
		final String impressions = numberFormatter.format(campaign.getNumberOfImpressions());
		final String clicks = numberFormatter.format(campaign.getNumberOfClicks());
		final String uniques = numberFormatter.format(campaign.getNumberOfUniques());
		final String bounces = numberFormatter.format(campaign.getNumberOfBounces());
		final String conversions = numberFormatter.format(campaign.getNumberOfConversions());
		final String cost = currencyFormatter.format(campaign.getTotalCostOfCampaign() / 100);
		final String cpa = currencyFormatter.format(campaign.getTotalCostOfCampaign() / 100 / campaign.getNumberOfConversions());
		final String ctr = numberFormatter.format(campaign.getClickThroughRate());
		final String cpc = currencyFormatter.format(campaign.getCostPerClick() / 100);
		final String cpm = currencyFormatter.format(campaign.getTotalCostOfCampaign() * 10 / campaign.getNumberOfImpressions());
		final String br = percentFormatter.format(campaign.getBounceRate());
		
		numberOfImpressions.setText(impressions);
		numberOfClicks.setText(clicks);
		numberOfUniques.setText(uniques);
		numberOfBounces.setText(bounces);
		numberOfConversions.setText(conversions);
		totalCost.setText(cost);
		clickThroughRate.setText(ctr);
		costPerAcquisition.setText(cpa);
		costPerClick.setText(cpc);
		costPerThousandImpressions.setText(cpm);
		bounceRate.setText(br);
	}
}
