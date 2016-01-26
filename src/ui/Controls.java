package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import core.Model;

public class Controls extends Box implements Observer, ActionListener {

	// ==== Constants ====
	
	private static final long serialVersionUID = 824395947852730145L;
	
	// ==== Properties ====
	
	private final Model model;
	
	// ==== Constructor ====
	
	public Controls(Model model) {
		super(BoxLayout.Y_AXIS);
		
		// Register as an Observer of Model
		this.model = model;
		model.addObserver(this);
		
		// Add Borders for visual appeal!!!
		setPreferredSize(new Dimension(280, 600));
		setBorder(BorderFactory.createCompoundBorder( 
				new MatteBorder(0, 1, 0, 0, new Color(150, 150, 150)), 
				new EmptyBorder(20, 20, 20, 20))); 

	}
	
	// ==== Observer Implementation ====

	@Override
	public void update(Observable o, Object arg) {
		if (o == model) {
			// TODO What happens when Model updates the controls?
			
			// We would like to update the existing values in the Controls to reflect
			// the new state of the Model
		}
	}
	
	
	// ==== ActionListener Implementation ====
	
	// This method triggers whenever a control is manipulated
	// Use this to alter the Model by calling its appropriate Models

	@Override
	public void actionPerformed(ActionEvent e) {
//		final Object source = e.getSource();
		
		// TODO Add if-statements to determine appropriate actions
		
	}

}
