package ui.graphelements;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import core.Model;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class GraphPanel extends JPanel {
	
	private JFXPanel centerPanel;
	
	private Scene scene;
	private GridPane chartElementPane;
	private ChartElement chartElement;
	
	public GraphPanel(Model model){
		this.setBackground(new java.awt.Color(0, 140, 100));
		this.setPreferredSize(new Dimension(530, 350));
		this.setLayout(new BorderLayout());
		
		init();
	}
	
	//second constructor, might use it later
	public GraphPanel(Model model, int number){
		this.setBackground(new java.awt.Color(200, 0, 200));
		this.setPreferredSize(new Dimension(1500, 700));
		this.setLayout(new BorderLayout());
		init();
	}
	
	public void init(){
		centerPanel = new JFXPanel();
		JLabel myLabel = new JLabel("Here goes the name of the chart");
		centerPanel.setBackground(new java.awt.Color(140, 0, 20));
		
		//Creating an ugly compound border for the TextField Panel and the Fractals
	    Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	    Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	    Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		
//		centerPanel.addMouseListener(new MouseListener() {
//			public void mouseClicked(MouseEvent e) {	
//				
//				JFrame expandedFrame = new JFrame("Your Chart");
//				expandedFrame.setSize(1080, 720);
//				JPanel myExpandedPanel = new JPanel();
//				expandedFrame.setContentPane(myExpandedPanel);
//				myExpandedPanel.setBackground(new java.awt.Color(0, 0, 120));
//			}
//
//			
//			public void mousePressed(MouseEvent e) {
//				// TODO Auto-generated method stub	
//			}
//			public void mouseReleased(MouseEvent e) {
//				// TODO Auto-generated method stub	
//			}
//			public void mouseEntered(MouseEvent e) {
//				// TODO Auto-generated method stub
//			}
//			public void mouseExited(MouseEvent e) {
//				// TODO Auto-generated method stub
//			}
//		});

		this.add(centerPanel, BorderLayout.CENTER);
		this.add(myLabel, BorderLayout.NORTH);
		this.setBorder(compound);
		
		chartElementPane = new GridPane();
		scene = new Scene(chartElementPane, 0, 0);	
	}	
	
	public void attachChartElement(ChartElement chartElement)
	{
		this.chartElement = chartElement;
		chartElementPane.add(chartElement.getChart(), 0, 0);
		centerPanel.setScene(scene);
	}
}
