package ui;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Controller;
import core.Model;
import ui.controlelements.ControlPanel;
import ui.graphelements.GraphAreaView;

public class Window extends JFrame {

	// ==== Constants ====
	
	private static final long serialVersionUID = 1092418710020581973L;
	
	private Controller controller;
	
	// ==== Constructor ====
	
	public Window(Controller controller) {
		super();
		
		this.controller = controller;

		// Default Operations on JFrame
		setTitle("COMP2211 Group 6");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		// Initialising Controller and View
		final ControlPanel controlPanel = new ControlPanel(controller);
		final GraphAreaView graphAreaView = new GraphAreaView(controller);
		
		// Layout of our UI
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(controlPanel, BorderLayout.EAST);
		panel.add(graphAreaView, BorderLayout.CENTER);
		
		// Set as Default Content Pane
		setContentPane(panel);
		// James is a special snowflake
		getContentPane().setPreferredSize(new Dimension(1920,1080));

		// Resize Window to UI Components
		pack();
		
		// Set the View to Visible
		graphAreaView.setVisible(true);
	}

}
