package ui;

import javax.swing.JFrame;

import core.Model;

public class Window extends JFrame {

	// ==== Constants ====
	private static final long serialVersionUID = 1092418710020581973L;
	
	
	// ==== Constructor ====
	
	public Window() {
		super();
		
		setTitle("COMP2211 Group 6");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final Model model = new Model();
		
		final Controls controls = new Controls(model);
		final View view = new View(model);
	}

}
