package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
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
		super();
		
		// Simple Default Settings...
		setPreferredSize(new Dimension(800, 600));
		setVisible(false);

		this.model = model;
		model.addObserver(this);
		
		// Handle Resizing
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Update Model with getSize()
				model.setSize(getSize());
			}
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Update Model with getSize()
				model.setSize(getSize());
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
	
	// ==== Observer Implementation ====

	@Override
	public void update(Observable o, Object arg) {
		if (o == model) {
			// TODO Update view on Model refresh
			repaint();
		}
	}

}
