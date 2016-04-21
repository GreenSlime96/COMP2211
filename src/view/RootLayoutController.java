package view;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.Model;
import core.campaigns.Campaign;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by RyanBeal on 07/03/2016.
 */

public class RootLayoutController {

    private Model model;
    private Stage mainStage;

    private DashboardOverviewController dashboardOverviewController;
    
    public void setModelandStage(Model model, Stage stage) {
        this.model = model;
        this.mainStage = stage;
    }

    public void setDashboardOverviewController(DashboardOverviewController c)
    {
    	this.dashboardOverviewController = c;
    }
    
    public void handleRemoveCampaign(){
    	model.removeCampaign();
    }

    public void handleAddCampaign(){
        DirectoryChooser dc = new DirectoryChooser();
        File campaignDirectory = dc.showDialog(mainStage);

        if (campaignDirectory != null)
            model.addCampaign(campaignDirectory);

    }
   
    public void close(){
        System.exit(0);
    }

    public void addChart(){
        model.addChart();
    }

    public void copy(){
       Campaign campaign = model.currentCampaign.get();
       
       if (campaign == null) {
			final Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Campaign Selected");
			alert.setHeaderText("No Campaign Selected");
			alert.setContentText("You need an active campaign to copy it");
			
			alert.showAndWait();	
			
			return;
       }

        StringBuilder clipboardString = new StringBuilder();

        clipboardString.append("Campaign Start Date:\t");
        clipboardString.append(campaign.getStartDateTime());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Campaign End Date:\t");
        clipboardString.append(campaign.getEndDateTime());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Number of Impressions:\t");
        clipboardString.append(campaign.getNumberOfImpressions());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Number of Clicks:\t");
        clipboardString.append(campaign.getNumberOfClicks());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Number of Uniques:\t");
        clipboardString.append(campaign.getNumberOfUniques());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Number of Bounces:\t");
        clipboardString.append(campaign.getNumberOfBounces());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Number of Conversions:\t");
        clipboardString.append(campaign.getNumberOfConversions());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Total Cost of Campaign:\t");
        clipboardString.append(campaign.getTotalCostOfCampaign());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Click Through Rate:\t");
        clipboardString.append(campaign.getClickThroughRate());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Cost Per Acquisition:\t");
        clipboardString.append(campaign.getCostPerAcquision());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Cost Per Click:\t");
        clipboardString.append(campaign.getCostPerClick());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Cost Per Thousand Impressions:\t");
        clipboardString.append(campaign.getCostPerThousandImpressions());
        clipboardString.append(System.lineSeparator());

        clipboardString.append("Bounce Rate:\t");
        clipboardString.append(campaign.getBounceRate());
        clipboardString.append(System.lineSeparator());

        // copy to clipboard
        final ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);

        // alert the user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Campaign Successfully Copied");
        alert.setHeaderText(null);
        alert.setContentText("Copied to Clipboard!");

        // wait for acknowledgement
        alert.showAndWait();

    }

	public void removeChart() {
		model.removeChart();
	}

	public void duplicateChart() {
		model.duplicateCurrentChart();
	}

	public void resetChart() {
		model.resetChart();
	}

    public void about(){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(mainStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("About:" + System.getProperty("line.separator") + ("Created By James, Ryan, Boon, Dan and Victor")));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void minimise(){
        mainStage.setIconified(true);
    }

    public void fullScreen(){
        mainStage.setFullScreen(true);
    }
    
    public void saveAsPng(){
    	WritableImage chartIMG = dashboardOverviewController.getChartAsIMG();
    	
    	FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Image");
		
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
		"PNG files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		
		// Show save file dialog
		File file = fileChooser.showSaveDialog(mainStage);
		
		if (file != null) {
		// Make sure it has the correct extension
			if (!file.getPath().endsWith(".png")) 
			{
				file = new File(file.getPath() + ".png");
			}
			
			try 
			{
				ImageIO.write(SwingFXUtils.fromFXImage(chartIMG, null), "png", file);
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Image Saved Successfully");
				alert.setHeaderText("Image Saved Successfully");
				alert.setContentText("Chart image exported successfully.");
				alert.showAndWait();
				
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
    }
    
    public void printChart(){
//    	model.printChart();
    	System.out.println("hello");
    }
}
