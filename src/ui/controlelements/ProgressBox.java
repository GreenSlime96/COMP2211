package ui.controlelements;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBox extends ControlPanelTab {

	public ProgressBox() {
		
		//Where member variables are declared:
		JProgressBar progressBar;
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(50);
		progressBar.setStringPainted(true);

		addSetting(progressBar,"BLA","BLA");
		
	}

}
