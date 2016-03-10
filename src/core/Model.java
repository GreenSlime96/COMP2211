package core;

import java.io.File;
import java.util.Observable;

import core.campaigns.Campaign;
import core.campaigns.InvalidCampaignException;
import core.data.DataProcessor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Model extends Observable {

	// ==== Properties ====

	// The list of Campaigns registered with this model
	public final ObservableList<Campaign> campaigns = FXCollections.observableArrayList();

	// The list of Charts stored in this model
	public final ObservableList<DataProcessor> dataProcessors = FXCollections.observableArrayList();

	// stores current processors etc
	public final ObjectProperty<DataProcessor> currentProcessor = new SimpleObjectProperty<DataProcessor>();
	public final ObjectProperty<Campaign> currentCampaign = new SimpleObjectProperty<Campaign>();
	
	// stores whether or not the model is occupied
	public final BooleanProperty busy = new SimpleBooleanProperty();


	// ==== Constructor ====

	public Model() {
		super();
	}

	// ==== Accessors ====

	// ==== Chart Stuff ====
	
	/**
	 * Adds the currently selected campaign as a chart
	 * 
	 */
	public synchronized final void addChart() {
		// store old value
		final Campaign campaign = currentCampaign.get();
		
		// if doesn't exist, cannot duplicate
		if (campaign == null) {
			final Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Campaign Selected");
			alert.setHeaderText("No Campaign Selected");
			alert.setContentText("You must have a Campaign Selected in order to add a Chart");
			
			alert.showAndWait();	
			
			return;
		}
		
		// duplicate the current DataProcessor
		final DataProcessor newProcessor = new DataProcessor(campaign);
		
		// add this to the list
		dataProcessors.add(newProcessor);
		
		// update the current processor to the newly created DP
		currentProcessor.set(newProcessor);
		
	}
	
	/**
	 * Duplicates the currently selected chart
	 */
	public synchronized final void duplicateCurrentChart() {
		// store old value
		final DataProcessor oldProcessor = currentProcessor.get();
		
		// if doesn't exist, cannot duplicate
		if (oldProcessor == null)
			System.out.println("cannot duplicate null chart.");
		
		// duplicate the current DataProcessor
		final DataProcessor newProcessor = new DataProcessor(currentProcessor.get());
		
		// add this to the list
		dataProcessors.add(newProcessor);
		
		// update the current processor to the newly created DP
		currentProcessor.set(newProcessor);
	}
	
	// TODO: remove chart
	public synchronized final void removeChart(int index) {
		dataProcessors.remove(index);
	}
	
	public synchronized final void addCampaign(File campaignDirectory) {
		final Campaign campaign = new Campaign(campaignDirectory);

		// skip if it already exists
		if (campaigns.contains(campaign)) {
			final Alert alert = new Alert(AlertType.WARNING);
			
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
					busy.set(true);					
					campaign.loadData();
				} finally {
					busy.set(false);
				}
				
				return null;
			}					
		};

		new Thread(task).start();
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				currentCampaign.set(campaign);
				campaigns.add(campaign);
				
				addChart();
			}				
		});
		
		task.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				final Alert alert = new Alert(AlertType.ERROR);
				
				alert.setTitle("Invalid Campaign");
				alert.setHeaderText("Error Loading Campaign");
				alert.setContentText(task.getException().getMessage());
				
				alert.showAndWait();						
			}				
		});
	}
	
	public synchronized final void removeCampaign() {
		removeCampaign(currentCampaign.get());
	}

	/**
	 * TODO: throw exception, need to notify user
	 * 
	 * @param campaign
	 * @throws Exception 
	 */
	public synchronized final void removeCampaign(int index) {
		removeCampaign(campaigns.get(index));
	}
	
	public synchronized final void removeCampaign(Campaign campaign) {
		
		// error if null campaign
		if (campaign == null) {
			final Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("Invalid Campaign");
			alert.setHeaderText("Error Removing Campaign");
			alert.setContentText("You cannot remove a Campaign when one isn't selected");
			
			alert.showAndWait();	
		}
			
		// iterate over DataProcessors to make sure there are no dependencies
		for (DataProcessor dataProcessor : dataProcessors) {
			if (dataProcessor.getCampaign().equals(campaign)) {
				final Alert alert = new Alert(AlertType.WARNING);
				
				alert.setTitle("Campaign In Use");
				alert.setHeaderText("Error Removing Campaign");
				alert.setContentText("Your Campaign cannot be removed as it is currently in use.");
				
				alert.showAndWait();	
				
				return;
			}
		}

		// remove if naked
		campaigns.remove(campaign);
	}
}
