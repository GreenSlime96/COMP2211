import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.Window;

public class Main {
	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader("/Users/kbp2g14/Downloads/2_month_campaign_2");
		long time = System.currentTimeMillis();
		int temp, count = 0;
		while ((temp = fr.read()) != -1) {
			if 
		}
		
		System.out.println(System.currentTimeMillis() - time);
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