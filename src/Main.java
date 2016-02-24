import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import core.records.Impression;
import ui.Window;

public class Main {
	public static void main(String[] args) {
		// use system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("unable to use system look and feel");
		}

		// start the program within the UI thread
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final Window window = new Window();
				window.setVisible(true);
				window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			}
		});
	}
}