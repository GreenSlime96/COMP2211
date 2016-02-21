import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.Window;

public class Main {
	public static void main(String[] args) {
//		String date = "1234.567890";
//		char[] data = date.toCharArray();
//		
//		for (;;) {
//			double result = 0;
//			long time = System.currentTimeMillis();
//			for (int i = 0; i < 1000000; i++) {
//				result = 0;
//				result = Double.valueOf(new String(data));
//			}
//			System.out.print(System.currentTimeMillis() - time + "\t" + result + "\t");
//			time = System.currentTimeMillis();
//			for (int i = 0; i < 1000000; i++) {
//				result = 0;
//				for (char c : data) {
//					if (c == '.')
//						continue;
//
//					result *= 10;
//					result += c & 0xF;
//				}
//				result /= (double) 1E6;
//			}
//			System.out.println(System.currentTimeMillis() - time + "\t" + result);
//		}
		
		
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