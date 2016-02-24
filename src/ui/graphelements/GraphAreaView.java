package ui.graphelements;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.Timer;

import core.Model;
import javafx.collections.FXCollections;
import javafx.scene.chart.PieChart;

public class GraphAreaView extends JComponent implements Observer, ActionListener {

	// ==== Constants ====
	private static final long serialVersionUID = -3060291319561936699L;

	
	Dimension fullViewDimension = Toolkit.getDefaultToolkit().getScreenSize();
	private final Dimension maxDimensionForPanel = new Dimension((int)fullViewDimension.getWidth()-300, (int)fullViewDimension.getHeight()-100);
	private final Dimension secondDimension = new Dimension((int)maxDimensionForPanel.getWidth(), (int)(maxDimensionForPanel.getHeight()-20)/2);
	private final Dimension minimumDimension = new Dimension((int)secondDimension.getWidth()/2-20, (int) secondDimension.getHeight());

	// ==== Properties ====
	
	private final Timer timer = new Timer(250, this);
	private final Model model;
	public int numberOfCharts = 0;
	
	private GraphPanel theGraphPanel;
	private ArrayList<GraphPanel> panelList = new ArrayList<GraphPanel>();

	// ==== Constructor ====

	@SuppressWarnings("restriction")
	public GraphAreaView(Model model) {
		super();
		
		// Simple Default Settings...
		this.setSize(fullViewDimension);
		
		setVisible(true);
		setLayout(new FlowLayout());

		// Register View with Model as an Observer
		this.model = model;
		model.addObserver(this);

		// Set the Timer to nonrepeating
		timer.setRepeats(false);
		

		theGraphPanel = new GraphPanel(model,numberOfCharts);
		theGraphPanel.setCenterPanelSize(maxDimensionForPanel);
		
		addPanel(theGraphPanel);

		
		
		// Handle Resizing
		addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				// Restart the Timer to handle resizing
				// This way we do not overload the Model with rendering
				timer.restart();
			}

			@Override
			public void componentShown(ComponentEvent e) {
				final Timer timer = new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// model.setSize(getSize());
//						chartFxPanel.setSize(getSize());
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		});
		
	}
	
	//Refactored addPanel method
	public boolean addPanel(GraphPanel graphPanel)
	{
		if(panelList.size() >= 4)
			return false;
		
		panelList.add(graphPanel);
		this.add(graphPanel);
		revalidate();
		return true;	
	}
    /*
	public void addPanel(GraphPanel graphPanel){
		//increase the over-all number of charts 
		numberOfCharts++;
		
		//there can only be 4 charts at a time.
		//if a 5th is added, delete chart 1 and add it in its place
		if(numberOfCharts > 4){
			//temporary
			removeAll();
			int numberOfChartsAsIndex = numberOfCharts%4;
			
			if(numberOfChartsAsIndex != 0){
				numberOfChartsAsIndex--;
			}else{
				numberOfChartsAsIndex = 3;
			}
			myGraphArray.remove(numberOfChartsAsIndex);
			myGraphArray.add(numberOfChartsAsIndex, graphPanel);
		}else{
			myGraphArray.add(graphPanel);
		}
			
		//Iterating over the array of GraphPanels and adding each of them to the view
		//if there is only one chart add it to the view
		//if there are 2 charts, modify the size of the first so that both can fill the screen
		// if there are 3 or more charts, resize the previous charts and add the new one
		for(GraphPanel myIterator : myGraphArray){
			if(myGraphArray.size() == 1){
				this.add(myIterator);
				myGraphArray.get(0).getChartElement().resizeChart(maxDimensionForPanel);
				
		}
			if(myGraphArray.size() == 2 ){
				myGraphArray.get(0).setCenterPanelSize(secondDimension);
				this.add(myIterator);
				myGraphArray.get(1).getChartElement().resizeChart(maxDimensionForPanel);
				
			}else	if(myGraphArray.size() == 3){
				myGraphArray.get(0).setCenterPanelSize(minimumDimension);
				myGraphArray.get(1).setCenterPanelSize(minimumDimension);
				this.add(myIterator);
				myGraphArray.get(2).setCenterPanelSize(secondDimension);
				myGraphArray.get(2).getChartElement().resizeChart(maxDimensionForPanel);
				
				
			}else	if(myGraphArray.size() > 3 ){
				myGraphArray.get(0).setCenterPanelSize(minimumDimension);
				myGraphArray.get(1).setCenterPanelSize(minimumDimension);
				
				this.add(myIterator);
				myGraphArray.get(2).setCenterPanelSize(minimumDimension);
				myGraphArray.get(2).getChartElement().resizeChart(maxDimensionForPanel);
				
			}
			this.add(myIterator);
		}
		
		revalidate();
	}
	*/
	
	private void updateGraphPanel(int index)
	{
		LineChartElement lce;

			System.out.println("New Chart");
			lce = new LineChartElement(model.getCurrentMetric().toString());
			lce.setMetric(model.getCurrentMetric().toString());
			lce.setTimeGranularity(model.getTimeGranularityInSeconds());
			lce.addSeries(model.getChartData(), model.getStartDateTime());
			lce.resizeChart(maxDimensionForPanel);
			theGraphPanel.setChartElement(lce);

		
		System.out.println("show chart");
	}
	
	// ==== JComponent Overrides ====

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

//		Map<Date, Integer> impMap = model.getNumberOfImpressions();

		// BufferedImage image = model.getImage();
		// g.drawImage(image, 0, 0, null);
//		g.dispose();
	}

	// ==== ActionListener Implementation ====

	@Override
	public void actionPerformed(ActionEvent e) {

		// Trigger the Resize when Timer fires
		if (e.getSource() == timer) {
			// model.setSize(getSize());
//			chartFxPanel.setSize(getSize());
		}
	}

	// ==== Observer Implementation ====

	@Override
	public void update(Observable o, Object arg) {
		if (o == model) {
			updateGraphPanel(0);
			repaint();
		}
	}
	public int getNumberOfCharts(){
		return numberOfCharts;
	}
	
}