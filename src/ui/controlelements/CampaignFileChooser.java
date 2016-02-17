package ui.controlelements;

import java.io.File;

import javax.swing.JFileChooser;

public class CampaignFileChooser {

	private JFileChooser chooser;
	
	public CampaignFileChooser() {
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Campaign Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
	}
	
	public boolean selectionMade() {
		return chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION;
	}
	
	public File getSelectedFile() {
		return chooser.getSelectedFile();
	}
	
}
