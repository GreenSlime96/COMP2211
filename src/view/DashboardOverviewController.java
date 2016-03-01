package view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import core.Model;
import core.campaigns.Campaign;
import core.campaigns.InvalidCampaignException;
import core.data.DataProcessor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
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
	
	@FXML
	private Tab addChartTab;
	
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
		
		if (campaignDirectory != null) {
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
		int index = campaignAccordion.getPanes().indexOf(campaignAccordion.getExpandedPane());
		
		if (index == -1) {
			final Alert alert = new Alert(AlertType.ERROR);
			
			alert.initOwner(mainStage);
			alert.setTitle("No Campaign Selected");
			alert.setHeaderText("No Campaign Selected");
			alert.setContentText("You must select a campaign in order to remove it.");
			
			alert.showAndWait();
			
			return;
		}
			
		try {
			model.removeCampaign(index);
		} catch (Exception e) {
			final Alert alert = new Alert(AlertType.WARNING);
			
			alert.initOwner(mainStage);
			alert.setTitle("Campaign in Use");
			alert.setHeaderText("Campaign in Use");
			alert.setContentText("The selected campaign cannot be removed as it is currently in use. Close charts and try again");
			
			alert.showAndWait();
		}
	}
	
	@FXML
	private void handleAddChart() {
		int index = campaignAccordion.getPanes().indexOf(campaignAccordion.getExpandedPane());
		
		if (index == -1)
			return;
		
		final Campaign campaign = model.getCampaign(index);
		System.out.println("adding chart");
		model.addChart(campaign);
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
		
		model.dataProcessors.addListener(new ListChangeListener<DataProcessor>() {
			@Override
			public void onChanged(Change<? extends DataProcessor> c) {
				final List<Tab> tabsList = chartsTabPane.getTabs();
				
				while (c.next()) {
					if (c.wasAdded()) {
						// remove the last tab
						tabsList.remove(addChartTab);
						
						for (DataProcessor dataProcessor : c.getAddedSubList()) {
							try {
								// Load person overview.
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(this.getClass().getResource("ChartOverview.fxml"));
								BorderPane campaignOverview = (BorderPane) loader.load();

								ChartOverviewController controller = loader.getController();

								controller.setDataProcessor(dataProcessor, model.campaigns);

								TextField textField = new TextField("Chart");
								Label label = new Label("Chart");
								Tab tab = new Tab();
								
								textField.setMaxSize(label.getPrefWidth(), label.getPrefHeight());
								
								tab.setGraphic(label);
								tab.setContent(campaignOverview);
								
								label.setOnMouseClicked(new EventHandler<MouseEvent>() {
									@Override
									public void handle(MouseEvent event) {
										if (event.getClickCount() == 2) {
											textField.setText(label.getText());
											tab.setGraphic(textField);
											textField.selectAll();
											textField.requestFocus();
										}
									}
								});
								
								textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
									@Override
									public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
											Boolean newValue) {
										if (!newValue) {
											label.setText(textField.getText());
											tab.setGraphic(label);
										}
									}
								});
								
								textField.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										label.setText(textField.getText());
										tab.setGraphic(label);
									}
								});

								tab.setOnCloseRequest(new EventHandler<Event>() {
									@Override
									public void handle(Event event) {
										final int index = chartsTabPane.getTabs().indexOf(event.getSource());
										model.dataProcessors.remove(index);
									}
								});

								tabsList.add(tab);
							} catch (IOException e) {
								e.printStackTrace();
							}

							chartsTabPane.getSelectionModel().select(tabsList.size() - 1);
						}
						
						tabsList.add(addChartTab);
					}

				}
			}
			
		});
	}
}
