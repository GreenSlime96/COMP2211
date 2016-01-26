package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Timer;

public class Model extends Observable implements ActionListener {
	
	// ==== Properties ====
	
	// We may use this Timer to fire events as they occur
	private final Timer timer = new Timer(1000, this);
	

	// ==== ActionListener Implementation ====
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// This block of code executes every time the Timer fires
		// Notifies all Observers of an event
		if (e.getSource() == timer) {
			setChanged();
			notifyObservers();
		}
		
	}

}
