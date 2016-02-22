package ui.graphelements;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import core.Model;

public class GraphPanel extends JPanel {
	private final Model model;
	JPanel centerPanel;
	
	//getting the size of the screen and calculating sizes for the panles
	private final Dimension fullViewDimension = Toolkit.getDefaultToolkit().getScreenSize();
	private final Dimension maxDimensionForPanel = new Dimension((int)fullViewDimension.getWidth()-300, (int)fullViewDimension.getHeight()-100);
	private final Dimension secondDimension = new Dimension((int)maxDimensionForPanel.getWidth(), (int)(maxDimensionForPanel.getHeight()-20)/2);
	private final Dimension minimumDimension = new Dimension((int)secondDimension.getWidth()/2-20, (int) secondDimension.getHeight());

	public GraphPanel(Model model, int numberOfCharts){
		super();
		
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
		centerPanel = new JPanel();
		JLabel myLabel = new JLabel("name of the chart goes here");
		centerPanel.setBackground(new Color(140, 0, 20));
		
		//Creating an ugly compound border for the TextField Panel and the Fractals
	    Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	    Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	    Border compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		
		centerPanel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {	
				
				if (e.getClickCount() == 2 && !e.isConsumed()) {
				     e.consume();
//				     GraphWindow testWindow = new GraphWindow(model, "Unique Impressions");		
				     GraphAreaView background = (GraphAreaView) centerPanel.getParent().getParent();
//				     System.out.println(background.getDimension());
				     background.addPanel(new GraphPanel(model, background.getNumberOfCharts()+1));
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
	}	
	
	public void setCenterPanelSize(Dimension size){
		centerPanel.setPreferredSize(size);
	}
}
