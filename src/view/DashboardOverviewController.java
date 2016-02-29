package view;

import java.io.File;
import java.io.IOException;

import core.Model;
import core.campaigns.Campaign;
import core.campaigns.InvalidCampaignException;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class DashboardOverviewController {
	
	@FXML
	private Accordion campaignAccordion;
	
	@FXML
	private Button addCampaignButton;
	
	@FXML
	private Button removeCampaignButton;
	
	@FXML
	private TabPane chartsTabPane;
	
	private Stage mainStage;
	private Model model;
	
	
	// ==== Constructor ====
	
	public DashboardOverviewController() {
	}	
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	private void handleAddCampaign() {
		DirectoryChooser dc = new DirectoryChooser();
		File campaignDirectory = dc.showDialog(mainStage);
		
		if (dc != null) {
			try {
				model.addCampaign(campaignDirectory);
			} catch (InvalidCampaignException e) {
				final Alert alert = new Alert(AlertType.ERROR);
				
				alert.initOwner(mainStage);
				alert.setTitle("Invalid Campaign");
				alert.setHeaderText("Error Loading Campaign");
				alert.setContentText(e.getMessage());
				
				alert.showAndWait();				
			}
		}
	}
	
	@FXML
	private void handleRemoveCampaign() {
		System.out.println("clicked remove");
	}
	
	public void setStageAndModel(Stage stage, Model model) {
		this.mainStage = stage;
		this.model = model;
		
		model.campaigns.addListener(new ListChangeListener<Campaign>() {
			@Override
			public void onChanged(Change<? extends Campaign> c) {
				while (c.next()) {
					campaignAccordion.getPanes().clear();
					
					for (Campaign campaign : model.campaigns) {
						try {
				            // Load person overview.
				            FXMLLoader loader = new FXMLLoader();
				            loader.setLocation(this.getClass().getResource("CampaignOverview.fxml"));
				            TitledPane campaignOverview = (TitledPane) loader.load();
				            
				            CampaignOverviewController controller = loader.getController();
				            
				            controller.setCampaign(campaign);
				            
				            campaignAccordion.getPanes().add(campaignOverview);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
}
