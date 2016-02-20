import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.Window;
import util.DateProcessor;

public class Main {

	public static void main(String[] args) {
		char[] date = "2015-01-01 12:00:05".toCharArray();
//		long inLong = DateProcessor.charArrayToLong(date);
		long time = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			DateProcessor.charArrayToLong(date);
		}
		System.out.println(System.currentTimeMillis() - time);
//		System.out.println(DateProcessor.longToLocalDateTime(inLong));
		System.exit(0);
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