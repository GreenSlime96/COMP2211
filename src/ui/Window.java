package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Model;

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
		final Controls controls = new Controls(model);
		final View view = new View(model);
		
		// Layout of our UI
		final JPanel panel = new JPanel(new BorderLayout());
		panel.add(controls, BorderLayout.EAST);
		panel.add(view, BorderLayout.CENTER);
		
		// Set as Default Content Pane
		setContentPane(panel);
		
		// Resize Window to UI Components
		pack();
		
		// Set the View to Visible
		view.setVisible(true);
	}

}
