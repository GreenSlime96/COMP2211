package ui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import core.Model;

public class View extends JComponent implements Observer {

	// ==== Constants ====
	
	private static final long serialVersionUID = -3060291319561936699L;
	
	// ==== Properties ====
	
	private final Model model;
	
	// ==== Constructor ====
	
	public View(Model model) {
		this.model = model;
		model.addObserver(this);
	}
	
	// ==== Observer Implementation ====

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
