package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Timer;

public class Model extends Observable implements ActionListener {
	
	// ==== Properties ====
	
	private final Timer timer = new Timer(1000, this);

	// ==== ActionListener Implementation ====
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			setChanged();
			notifyObservers();
		}
		
	}

}
