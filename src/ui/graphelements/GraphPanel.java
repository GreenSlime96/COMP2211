package ui.graphelements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
	ArrayList<GraphPanel> myCurrentGraphs;
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
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2 && !e.isConsumed()) {
				     e.consume();
				     //creating a new window, adding the chart and resizing it
//				     GraphWindow testWindow = new GraphWindow(model, "Unique Impressions");	
//				     testWindow.setScene(scene);
//				     chartElement.resizeChart(testWindow.fullViewDimension);

				     //creating an object for the background
				     GraphAreaView background = (GraphAreaView) centerPanel.getParent().getParent();
				     
//				     if(!isItAPieChart){
//			    	 isItAPieChart = true;
			    	 GraphPanel myGraphPanel = new GraphPanel(model,background.getNumberOfCharts()+1);
			    	 
			    	 //Creating a Chart and populating it with random data
				     LineChartElement lc1 = new LineChartElement("CPA Chart "+ (background.getNumberOfCharts()+1));
				     lc1.setTimeGranularity(60*60*24);
				     lc1.setMetric("CPA");
				     List<Number> data = new ArrayList<Number>();
				     for(int i=0; i<30; i++){
						data.add(Math.random() * i);
				     }
				     lc1.addSeries(data, LocalDateTime.now());
				     
				     //setting the chart to the panel and adding the panel to the view
				     myGraphPanel.setChartElement(lc1);
				     background.addPanel(myGraphPanel,true);	
				     
				     //Creating an array to store all the graphs in the view
				     myCurrentGraphs = new ArrayList<GraphPanel>(3);
				     myCurrentGraphs.clear();
				     //going over the view and adding all of the GraphPanels to the array
				     //Doing this so I can re-add them later on T_T 
				     int i = 0;
				     while(i < background.getComponentCount()){
				    	System.out.println(background.getComponent(i));
				    	myCurrentGraphs.add((GraphPanel) background.getComponent(i));
				    	i++;
				     }
				     GraphPanel testPanel = new GraphPanel(model,0);
				     testPanel.setBackground(new Color(200,0,0));
//				     testPanel.setPreferredSize(new Dimension((int)maxDimensionForPanel.getWidth()+10,(int) maxDimensionForPanel.getHeight()+20));
				    
//				     background.removeAll();

//				     background.updateUI();
//				     testPanel.setChartElement(lc1);
//				     background.addPanel(testPanel,false);
				     
				     

					     
//				     }else{
//				    	 isItAPieChart = false;
//				    	 GraphPanel myGraphPanel = new GraphPanel(model,background.getNumberOfCharts()+1);
//				 		//PieChart
//				 		PieChartElement pc1 = new PieChartElement("Age Range, chart number " + (background.getNumberOfCharts()+1));
//				 		Random random = new Random();
//				 		pc1.setData(FXCollections.observableArrayList(
//				 				new PieChart.Data("<25", random.nextInt(10)),
//				 				new PieChart.Data("25-34", random.nextInt(10)),
//				 				new PieChart.Data("35-44", random.nextInt(10)),
//				 				new PieChart.Data("44-54", random.nextInt(10)),
//				 				new PieChart.Data(">45", random.nextInt(10))));
//				 		myGraphPanel.setChartElement(pc1);
//				 		background.addPanel(myGraphPanel);
//				     }
				     
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
}