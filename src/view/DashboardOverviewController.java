package view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import core.Model;
import core.campaigns.Campaign;
import core.campaigns.InvalidCampaignException;
import core.data.DataProcessor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.scene.control.ProgressBar;
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
	
	@FXML
	private ProgressBar progress;
	
	private Stage mainStage;
	private Model model;
	
	private BorderPane campaignOverview;
	private ChartOverviewController chartController;
	
	
	// ==== Constructor ====
	
	public DashboardOverviewController() {
		// Load person overview.
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("ChartOverview.fxml"));
			campaignOverview = (BorderPane) loader.load();
			chartController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void initialize() {
		campaignAccordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
			@Override
			public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {
		        if (oldValue != null) 
		        	oldValue.setCollapsible(true);
		        
				if (newValue != null)
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							newValue.setCollapsible(false);
						}
					});
			}			
		});
		
		chartsTabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				int index = newValue.intValue();
				
				System.out.println("changed to " + newValue.intValue());
				
				if (index == -1)
					return;
				
				// return if clicked on nothing
				if (index == chartsTabPane.getTabs().size() - 1) {
					System.out.println("user clicked on selection tab");
					chartsTabPane.getSelectionModel().clearAndSelect(index - 2);
					index -= 2;
				}
				
//				System.out.println("changed fired: " + index);
				chartController.setDataProcessor(model.dataProcessors.get(0), model.campaigns);
			}
		});;
		
		progress.managedProperty().bind(progress.visibleProperty());
		
		addCampaignButton.managedProperty().bind(progress.visibleProperty().not());
		addCampaignButton.visibleProperty().bind(progress.visibleProperty().not());
		
		removeCampaignButton.managedProperty().bind(progress.visibleProperty().not());
		removeCampaignButton.visibleProperty().bind(progress.visibleProperty().not());
		
		progress.setVisible(false);
	}
	
	@FXML
	private void handleAddCampaign() {
		DirectoryChooser dc = new DirectoryChooser();
		File campaignDirectory = dc.showDialog(mainStage);
		
		if (campaignDirectory != null) {
			final Campaign campaign = new Campaign(campaignDirectory);
			
			if (model.campaigns.contains(campaign)) {
				final Alert alert = new Alert(AlertType.WARNING);
				
				alert.initOwner(mainStage);
				alert.setTitle("Campaign Already Loaded");
				alert.setHeaderText("Campaign Already Loaded");
				alert.setContentText("Your selected Campaign has already been loaded. Please check the sidebar");
				
				alert.showAndWait();	
				
				return;
			}
			
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws InvalidCampaignException {
					try {
						progress.setVisible(true);						
						campaign.loadData();
					} finally {
						progress.setVisible(false);
					}
					
					return null;
				}					
			};

			new Thread(task).start();
			
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					model.campaigns.add(campaign);					
				}				
			});
			
			task.setOnFailed(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					final Alert alert = new Alert(AlertType.ERROR);
					
					alert.initOwner(mainStage);
					alert.setTitle("Invalid Campaign");
					alert.setHeaderText("Error Loading Campaign");
					alert.setContentText(task.getException().getMessage());
					
					alert.showAndWait();						
				}				
			});
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
			alert.setContentText("The selected campaign cannot be removed as it is currently in use. Close charts and try again.");
			
			alert.showAndWait();
		}
	}
	
	@FXML
	private void handleAddChart() {
		int index = campaignAccordion.getPanes().indexOf(campaignAccordion.getExpandedPane());
		
		if (index == -1)
			return;
		long t1 = System.currentTimeMillis();
		final Campaign campaign = model.getCampaign(index);
		model.addChart(campaign);
		long t2 = System.currentTimeMillis();
		
		System.out.println(t2 - t1 + " in handler");
	}
	
	public void setStageAndModel(Stage stage, Model model) {
		this.mainStage = stage;
		this.model = model;
		
		model.campaigns.addListener(new ListChangeListener<Campaign>() {
			@Override
			public void onChanged(Change<? extends Campaign> c) {
				while (c.next()) {
					TitledPane campaignOverview = null;
					
					campaignAccordion.getPanes().clear();

					for (Campaign campaign : model.campaigns) {
						try {
							// Load person overview.
							FXMLLoader loader = new FXMLLoader();
							loader.setLocation(this.getClass().getResource("CampaignOverview.fxml"));
							campaignOverview = (TitledPane) loader.load();

							CampaignOverviewController controller = loader.getController();

							controller.setCampaign(campaign);

							campaignAccordion.getPanes().add(campaignOverview);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					campaignAccordion.setExpandedPane(campaignOverview);
				}
			}
		});
		
		model.dataProcessors.addListener(new ListChangeListener<DataProcessor>() {
			@Override
			public void onChanged(Change<? extends DataProcessor> c) {
				final List<Tab> tabsList = chartsTabPane.getTabs();
				
				while (c.next()) {
					if (c.wasAdded()) {
						for (DataProcessor dataProcessor : c.getAddedSubList()) {
//							chartController.setDataProcessor(dataProcessor, model.campaigns);

							TextField textField = new TextField("Chart");
							Label label = new Label("Chart");
							Tab tab = new Tab();

							// textField.setMaxSize(label.getPrefWidth(),
							// label.getPrefHeight());

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
										if (!textField.getText().isEmpty())
											label.setText(textField.getText());

										tab.setGraphic(label);
									}
								}
							});

							textField.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									if (!textField.getText().isEmpty())
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

							tabsList.add(tabsList.size() - 1, tab);
						}
					}

				}
			}
			
		});
	}
}
