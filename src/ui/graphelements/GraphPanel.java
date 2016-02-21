package ui.graphelements;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
	
	public GraphPanel(Model model){
		this.setBackground(new java.awt.Color(0, 140, 100));
		this.setPreferredSize(new Dimension(530, 350));
		this.setLayout(new BorderLayout());
		this.model = model;
		init();
	}
	
	public void init(){
		JPanel centerPanel = new JPanel();
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
//				     GraphAreaView background = (GraphAreaView) centerPanel.getParent().getParent();
//				     background.addPanel(new GraphPanel(model));

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
}
