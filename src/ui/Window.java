package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Model;
import ui.controlelements.ControlPanel;
import ui.graphelements.GraphAreaView;
import ui.graphelements.GraphPanel;

public class Window extends JFrame {

	// ==== Constants ====
	
	private static final long serialVersionUID = 1092418710020581973L;
	
	
	// ==== Constructor ====
	
	public Window() {
		super();

		// Default Operations on JFrame
		setTitle("COMP2211 Group 6");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Initialising the Model
		final Model model = new Model();
		
		// Initialising Controller and View
		final ControlPanel controlPanel = new ControlPanel(model);
		final GraphAreaView graphAreaView = new GraphAreaView(model);
		
		// Layout of our UI
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(controlPanel, BorderLayout.EAST);
		panel.add(graphAreaView, BorderLayout.CENTER);
		
		// Set as Default Content Pane
		setContentPane(panel);
		// James is a special snowflake
		//getContentPane().setPreferredSize(new Dimension(1920,1080));

		// Resize Window to UI Components
		pack();
		
		// Set the View to Visible
		graphAreaView.setVisible(true);
	}

}
