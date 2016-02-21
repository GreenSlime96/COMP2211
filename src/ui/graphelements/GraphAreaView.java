package ui.graphelements;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.Timer;

import core.Model;
import core.fields.TimeGranularity;

public class GraphAreaView extends JComponent implements Observer, ActionListener {

	// ==== Constants ====

	private static final long serialVersionUID = -3060291319561936699L;

	// ==== Properties ====

	private final Timer timer = new Timer(250, this);
	private final Model model;
	
	private ArrayList<GraphPanel> myGraphArray = new ArrayList<GraphPanel>(3);

	// ==== Constructor ====

	public GraphAreaView(Model model) {
		super();

		// Simple Default Settings...
		setPreferredSize(new Dimension(2160, 1440));
		setVisible(true);
		setLayout(new FlowLayout());

		// Register View with Model as an Observer
		this.model = model;
		model.addObserver(this);

		// Set the Timer to nonrepeating
		timer.setRepeats(false);
		
		//creating 4 mockup graphPanel
		GraphPanel myGraphPanel = new GraphPanel(model);
		GraphPanel myGraphPanel1 = new GraphPanel(model);
		GraphPanel myGraphPanel2 = new GraphPanel(model);
		GraphPanel myGraphPanel3 = new GraphPanel(model);
		
		LineChartElement lc1 = new LineChartElement();
		lc1.setTimeGranularity(TimeGranularity.DAILY);
		lc1.setMetric("CPA");
		List<Number> data = new ArrayList<Number>();
		for(int i=0; i<30; i++)
			data.add(Math.random());
		lc1.addSeries("Test Series", data, LocalDateTime.now(), null);
		myGraphPanel.setChartElement(lc1);
/*
		LineChartElement lc2 = new LineChartElement();
		lc2.setTimeGranularity(TimeGranularity.WEEKLY);
		lc2.setMetric("CPM");
		data.clear();
		for(int i=0; i<20; i++)
			data.add(Math.random());
		lc2.addSeries("Test Series", data);
		data.clear();
		for(int i=0; i<20; i++)
			data.add(Math.random());
		lc2.addSeries("Test Series 2", data);
		myGraphPanel1.setChartElement(lc2);
		*/
		
		//Addding each of the 4 arrays to the array of GraphPanels
		myGraphArray.add(myGraphPanel);
		myGraphArray.add(myGraphPanel1);
		myGraphArray.add(myGraphPanel2);
		myGraphArray.add(myGraphPanel3);
		
		//Iterating over the array of GraphPanels and adding eachother to the view
		for(GraphPanel myIterator : myGraphArray){
			this.addPanel(myIterator);
		}
		
		
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
    
	public void addPanel(GraphPanel graphPanel){
		add(graphPanel);
		
//		chartFxPanel.add(graphPanel);
	}
	
	// ==== JComponent Overrides ====

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

//		Map<Date, Integer> impMap = model.getNumberOfImpressions();

		// BufferedImage image = model.getImage();
		// g.drawImage(image, 0, 0, null);
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
			// TODO Update view on Model refresh
			repaint();
		}
	}

}
