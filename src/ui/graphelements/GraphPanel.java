package ui.graphelements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import core.Model;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.GridPane;

public class GraphPanel extends JPanel {
	private final Model model;
	Boolean isItAPieChart = false;
	private JFXPanel centerPanel;
	
	private Scene scene;
	private GridPane chartElementPane;
	private ChartElement chartElement;
	
	Dimension fullViewDimension = Toolkit.getDefaultToolkit().getScreenSize();
	private final Dimension maxDimensionForPanel = new Dimension((int)fullViewDimension.getWidth()-300, (int)fullViewDimension.getHeight()-100);
	private final Dimension secondDimension = new Dimension((int)maxDimensionForPanel.getWidth(), (int)(maxDimensionForPanel.getHeight()-20)/2);
	private final Dimension minimumDimension = new Dimension((int)secondDimension.getWidth()/2-20, (int) secondDimension.getHeight());
	

	public GraphPanel(Model model, int numberOfCharts){
		
		this.setBackground(new java.awt.Color(0, 140, 100));
		this.setLayout(new BorderLayout());
		this.model = model;
		init();
		
		//if its the first chart display it on the full view
		//if there are 2 charts to display, split the view
		//else show all 4
		if(numberOfCharts==0){
			centerPanel.setPreferredSize(maxDimensionForPanel);

		}else	if(numberOfCharts == 2 || numberOfCharts == 1){
			centerPanel.setPreferredSize(secondDimension);
		}else{
			centerPanel.setPreferredSize(minimumDimension);
		}
	}
	
	public void init(){

		centerPanel = new JFXPanel();
		JLabel myLabel = new JLabel("Chart name~ ");
		centerPanel.setBackground(new java.awt.Color(140, 0, 20));
		
		//Creating an ugly compound border for the TextField Panel and the Fractals
	    Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	    Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	    Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);

		centerPanel.addMouseListener(new MouseListener() {
			@SuppressWarnings("restriction")
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
				     e.consume();
				     
				     //creating a new window, adding the chart and resizing it
//				     GraphWindow testWindow = new GraphWindow(model, "Unique Impressions");	
//				     testWindow.setScene(scene);
//				     chartElement.resizeChart(testWindow.fullViewDimension); 
				     
				     
				     GraphAreaView background = (GraphAreaView) centerPanel.getParent().getParent();
				     
				     if(!isItAPieChart){
				    	 isItAPieChart = true;
				    	 GraphPanel myGraphPanel = new GraphPanel(model,background.getNumberOfCharts()+1);
					     LineChartElement lc1 = new LineChartElement("CPA Chart "+ (background.getNumberOfCharts()+1));
					     lc1.setTimeGranularity(60*60*24);
					     lc1.setMetric("CPA");
					     List<Number> data = new ArrayList<Number>();
					     for(int i=0; i<30; i++){
							data.add(Math.random() * i);
					     }
					     lc1.addSeries(data, LocalDateTime.now());
					     

					     
					     myGraphPanel.setChartElement(lc1);
					     background.addPanel(myGraphPanel);

				     }else{
				    	 isItAPieChart = false;
				    	 GraphPanel myPiePanel = new GraphPanel(model,background.getNumberOfCharts()+1);
				 		//PieChart
				 		PieChartElement pc1 = new PieChartElement("Age Range, chart number " + (background.getNumberOfCharts()+1));
				 		Random random = new Random();
				 		pc1.setData(FXCollections.observableArrayList(
				 				new PieChart.Data("<25", random.nextInt(10)),
				 				new PieChart.Data("25-34", random.nextInt(10)),
				 				new PieChart.Data("35-44", random.nextInt(10)),
				 				new PieChart.Data("44-54", random.nextInt(10)),
				 				new PieChart.Data(">45", random.nextInt(10))));
					 		
					    //creating a new window, adding the chart and resizing it
//					    GraphWindow testWindow = new GraphWindow(model, "Unique Impressions");	
//					    testWindow.setScene(scene);
//					    chartElement.resizeChart(testWindow.fullViewDimension); 
					     
				 		myPiePanel.setChartElement(pc1);
				 		background.addPanel(myPiePanel);
				     }
				     

				}
			}
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub	
			}
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub	
			}
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});

		this.add(centerPanel, BorderLayout.CENTER);
//		this.add(myLabel, BorderLayout.NORTH);
		this.setBorder(compound);
		
		chartElementPane = new GridPane();
		scene = new Scene(chartElementPane, 0, 0);	
		scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
	}	
	

	public void setCenterPanelSize(Dimension size) {
		centerPanel.setPreferredSize(size);
	}
	/**
	 * Assigns a ChartElement to this panel and renders it.
	 * ChartElement should be fully defined before assigning.
	 * @param chartElement Element to be assigned
	 */
	public void setChartElement(ChartElement chartElement)
	{
		this.chartElement = chartElement;
		chartElementPane.add(chartElement.getChart(), 0, 0);
		centerPanel.setScene(scene);
	}
	
	public ChartElement getChartElement()
	{
		return chartElement;
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

//		Map<Date, Integer> impMap = model.getNumberOfImpressions();
		
		
		// BufferedImage image = model.getImage();
		// g.drawImage(image, 0, 0, null);
	}
}



