package view;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;

import core.campaigns.Campaign;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;

public class CampaignOverviewController {
	
	private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	// ==== Controller ====
	
	@FXML
	private TitledPane titledPane;
	
	@FXML
	private Label campaignStartDate;
	
	@FXML
	private Label campaignEndDate;
	
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
		titledPane.getContent().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					StringBuilder clipboardString = new StringBuilder();
					
					clipboardString.append("Campaign Start Date:\t");					
					clipboardString.append(campaignStartDate.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Campaign End Date:\t");					
					clipboardString.append(campaignEndDate.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Number of Impressions:\t");				
					clipboardString.append(numberOfImpressions.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Number of Clicks:\t");
					clipboardString.append(numberOfClicks.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Number of Uniques:\t");
					clipboardString.append(numberOfUniques.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Number of Bounces:\t");
					clipboardString.append(numberOfBounces.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Number of Conversions:\t");
					clipboardString.append(numberOfConversions.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Total Cost of Campaign:\t");
					clipboardString.append(totalCost.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Click Through Rate:\t");
					clipboardString.append(clickThroughRate.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Cost Per Acquisition:\t");
					clipboardString.append(costPerAcquisition.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Cost Per Click:\t");
					clipboardString.append(costPerClick.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Cost Per Thousand Impressions:\t");
					clipboardString.append(costPerThousandImpressions.getText());
					clipboardString.append(System.lineSeparator());
					
					clipboardString.append("Bounce Rate:\t");
					clipboardString.append(bounceRate.getText());
					clipboardString.append(System.lineSeparator());
					
					// copy to clipboard
			        final ClipboardContent content = new ClipboardContent();
			        content.putString(clipboardString.toString());
			        Clipboard.getSystemClipboard().setContent(content);
			        
			        // alert the user
			        Alert alert = new Alert(AlertType.INFORMATION);
			        alert.setTitle("Campaign Successfully Copied");
			        alert.setHeaderText(null);
			        alert.setContentText("Copied to Clipboard!");

			        // wait for acknowledgement
			        alert.showAndWait();
				}				
			}
			
		});
	}
	
	
	// ==== Accessors ====
	
	public void setCampaign(Campaign campaign) {
		titledPane.setText(campaign.toString());
		
		final NumberFormat numberFormatter = NumberFormat.getInstance();
		final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		final NumberFormat percentFormatter = NumberFormat.getPercentInstance();
		
		final String startDate = campaign.getStartDateTime().format(dateFormat);
		final String endDate = campaign.getEndDateTime().format(dateFormat);
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
		
		campaignStartDate.setText(startDate);
		campaignEndDate.setText(endDate);
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
