package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.Timer;

import core.Model;

public class View extends JComponent implements Observer, ActionListener {

	// ==== Constants ====
	
	private static final long serialVersionUID = -3060291319561936699L;
	
	// ==== Properties ====
	
	private final Timer timer = new Timer(250, this);
	private final Model model;
	
	// ==== Constructor ====
	
	public View(Model model) {
		super();
		
		// Simple Default Settings...
		setPreferredSize(new Dimension(800, 600));
		setVisible(false);

		// Register View with Model as an Observer
		this.model = model;
		model.addObserver(this);
		
		// Set the Timer to nonrepeating
		timer.setRepeats(false);
		
		// Handle Resizing
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				// Restart the Timer to handle resizing
				// This way we do not overload the Model with rendering
				timer.restart();
			}
			
			@Override
			public void componentShown(ComponentEvent e) {
				final Timer timer = new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						model.setSize(getSize());
					}
				});
				
				timer.setRepeats(false);
				timer.start();
			}
		});
	}
	
	// ==== JComponent Overrides ====
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		BufferedImage image = model.getImage();
		g.drawImage(image, 0, 0, null);
	}
	
	// ==== ActionListener Implementation ====
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Trigger the Resize when Timer fires
		if (e.getSource() == timer) {
			model.setSize(getSize());
		}
	}
	
	// ==== Observer Implementation ====

	@Override
	public void update(Observable o, Object arg) {
		if (o == model) {
			// TODO Update view on Model refresh
			repaint();
		}
	}

}
