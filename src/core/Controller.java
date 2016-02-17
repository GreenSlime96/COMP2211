package core;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.Window;

public class Controller {

	private Window window;
	private Model model;
	
	public Controller() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("unable to use system look and feel");
		}	
	}
	
	public void begin() {
		model = new Model(this);
		window = new Window(this);
		
		// start the program within the UI thread
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {				
				window.setVisible(true);
				window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			}
		});
	}
	
	public Model getModel() {
		return model;
	}
	
	
}
