package ui.controlelements;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import core.Model;

public class ProgressBox extends ControlPanelBox {

	JProgressBar progressBar;
	

	public ProgressBox(Model model) {
		super(model);
		//Where member variables are declared:
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setAlignmentX(LEFT_ALIGNMENT);

		addSetting(progressBar,"Progress","Not Currently Rendering");
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
