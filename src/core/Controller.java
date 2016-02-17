package core;

import javax.swing.JOptionPane;
import ui.Window;

public class Controller {

	private Window window;
	private Model model;
	
	public Controller() {
	}
	
	public void begin() {
	}
	
	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(window, message);
	}
	
	public Model getModel() {
		return model;
	}
	
	
}
