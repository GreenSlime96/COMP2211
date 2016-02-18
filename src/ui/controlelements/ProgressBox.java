package ui.controlelements;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.*;

import core.Model;

public class ProgressBox extends ControlPanelBox {

	JProgressBar progressBar;

	public ProgressBox(Model model) {
		super(model);
		//Where member variables are declared:
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setAlignmentX(LEFT_ALIGNMENT);

		addSetting(progressBar,"Progress","");
		
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == model){
//            if(model.isComputing()){
//                progressBar.setValue(model.getProgress*1000);
//            }

		}
	}

}
