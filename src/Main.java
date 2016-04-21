import java.io.IOException;

import core.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.DashboardOverviewController;
import view.RootLayoutController;

public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private Model model;

	private RootLayoutController rootLayoutController;
	private DashboardOverviewController dashboardOverviewController;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		primaryStage.setTitle("Ad Auction Dashboard");

		// 720p
		primaryStage.setMinWidth(1280);
		primaryStage.setMinHeight(750);
				
		model = new Model();

		initRootLayout();
		
		showDashboard();
		
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		scene.getStylesheets().add("style.css");
		primaryStage.show();

	}
	
	/**
	 * Initialise Root Layout
	 */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

			rootLayoutController = loader.getController();

			rootLayoutController.setModelandStage(model, primaryStage);
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showDashboard() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/DashboardOverview.fxml"));
            AnchorPane dashboardOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(dashboardOverview);

            dashboardOverviewController = loader.getController();

            dashboardOverviewController.setStageAndModel(primaryStage, model);
            
            rootLayoutController.setDashboardOverviewController(dashboardOverviewController);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
	public static void main(String[] args) {
		launch(args);
	}

}