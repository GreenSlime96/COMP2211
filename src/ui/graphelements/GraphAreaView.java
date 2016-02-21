package ui.graphelements;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import core.Controller;
import core.Model;
import javafx.embed.swing.JFXPanel;
import javafx.scene.paint.Color;

public class GraphAreaView extends JComponent implements Observer, ActionListener {

	// ==== Constants ====

	private static final long serialVersionUID = -3060291319561936699L;
	private final Dimension fullDimension = new Dimension(1070,660);
	private final Dimension twoChartsDimension = new Dimension(1070, 325);
	private final Dimension threeOrMoreDimension = new Dimension(530, 325);

	// ==== Properties ====

	private final Timer timer = new Timer(250, this);
	private final Model model;
	public int numberOfCharts = 0;
	
	private ArrayList<GraphPanel> myGraphArray = new ArrayList<GraphPanel>(3);

	// ==== Constructor ====

	public GraphAreaView(Model model) {
		super();

		// Simple Default Settings...
//		setPreferredSize(new Dimension(2160, 1440));
		setVisible(true);
		setLayout(new FlowLayout());

		// Register View with Model as an Observer
		this.model = model;
		model.addObserver(this);

		// Set the Timer to nonrepeating
		timer.setRepeats(false);
		
		//Addding 4 Panels to the View
//		myGraphArray.add(new GraphPanel(model));
		addPanel(new GraphPanel(model,numberOfCharts));
//		addPanel(new GraphPanel(model));
//		addPanel(new GraphPanel(model));
//		addPanel(new GraphPanel(model));
		
		
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
		//increase the over-all number of charts 
		numberOfCharts++;
		
		
		//there can only be 4 charts at a time.
		//if a 5th is added, delete chart 1 and add it in its place
		if(numberOfCharts > 4){
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
		
		this.removeAll();
		repaint();
		
		//Iterating over the array of GraphPanels and adding each of them to the view
		for(GraphPanel myIterator : myGraphArray){
			if(numberOfCharts == 0){
				this.add(myIterator);
			}
			if(numberOfCharts == 2){
				myGraphArray.get(0).setCenterPanelSize(twoChartsDimension);
				this.add(myIterator);
			}else	if(numberOfCharts == 3){
				myGraphArray.get(0).setCenterPanelSize(threeOrMoreDimension);
				myGraphArray.get(1).setCenterPanelSize(threeOrMoreDimension);
				this.add(myIterator);
			}
			this.add(myIterator);
		}
		
		revalidate();
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
	public int getNumberOfCharts(){
		return numberOfCharts;
	}
}
