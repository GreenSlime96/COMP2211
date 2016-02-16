package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.Timer;

public class Model extends Observable implements ActionListener {

	// ==== Constants ====


	// ==== Properties ====

	// We may use this Timer to fire events as they occur
	// Use this Timer to update the Controller/View about the current query status
	private final Timer timer = new Timer(1000, this);

	// ==== Constructor ====

	public Model() {
		super();
		
	}
	
	// ==== Accessors ====
	
	
	// ==== Private Helper Methods ====

	
	// ==== ActionListener Implementation ====

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			setChanged();
			notifyObservers();
		}
	}

}
