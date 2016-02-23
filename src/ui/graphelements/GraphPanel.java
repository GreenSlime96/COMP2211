package ui.graphelements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import core.Model;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class GraphPanel extends JPanel {
	private final Model model;

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
		}else	if(numberOfCharts == 2){
			centerPanel.setPreferredSize(secondDimension);
		}else{
			centerPanel.setPreferredSize(minimumDimension);
		}
	}
	
	public void init(){

		centerPanel = new JFXPanel();
		JLabel myLabel = new JLabel("Here goes the name of the chart");
		centerPanel.setBackground(new java.awt.Color(140, 0, 20));
		
		//Creating an ugly compound border for the TextField Panel and the Fractals
	    Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	    Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	    Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		
		centerPanel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2 && !e.isConsumed()) {
				     e.consume();
				     GraphWindow testWindow = new GraphWindow(model, "Unique Impressions");	
				     testWindow.setScene(scene);
				     chartElement.resizeChart(testWindow.getWidth(), testWindow.getHeight());
				     //GraphAreaView background = (GraphAreaView) centerPanel.getParent().getParent();
				     //background.addPanel(new GraphPanel(model, background.getNumberOfCharts()+1));
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
		this.add(myLabel, BorderLayout.NORTH);
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
