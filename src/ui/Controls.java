package ui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import core.Model;

public class Controls extends JComponent implements Observer {

	// ==== Constants ====
	
	private static final long serialVersionUID = 824395947852730145L;
	
	// ==== Properties ====
	
	private final Model model;
	
	// ==== Constructor ====
	
	public Controls(Model model) {
		this.model = model;

		model.addObserver(this);
	}
	
	// ==== Observer Implementation ====

	@Override
	public void update(Observable o, Object arg) {
		if (o == model) {
			// TODO What happens when Model updates the controls?
		}
	}

}
