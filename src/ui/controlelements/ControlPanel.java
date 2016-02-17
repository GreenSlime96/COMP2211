package ui.controlelements;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Controller;

public class ControlPanel extends Box implements Observer, ActionListener, ChangeListener, ItemListener {

	// ==== Constants ====
	
	private static final long serialVersionUID = 824395947852730145L;
	
	// ==== Properties ====	
	
	private final Controller controller;
	
	// Add controls here
	/* private final JSpinner ...
	 * private final JTextField ...
	 * private ...
	 */
	
	// ==== Constructor ====
	
	public ControlPanel(Controller controller) {
		super(BoxLayout.Y_AXIS);
		
		// Register as an Observer of Model
		this.controller = controller;
		controller.getModel().addObserver(this);
		
		// Add Borders for visual appeal!!!
		setPreferredSize(new Dimension(280, 600));
//		setBorder(BorderFactory.createCompoundBorder(
//				new MatteBorder(0, 1, 0, 0, new Color(150, 150, 150)),
//				new EmptyBorder(0, 20, 20, 20)));

		// Add Listener Hooks here
		/* x.addChangeListener(this);
		 * y.addActionListener(this);
		 * z.addItemListener(this);
		 */
		
		// Add Settings here
		/* addSetting(component, title, help text)
		 * 
		 */
		JTabbedPane tabbedPane = new JTabbedPane();

		GeneralTab generalTab = new GeneralTab();
		tabbedPane.addTab("General", null, generalTab,
				"Does nothing");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		FilterTab filterTab = new FilterTab();
		tabbedPane.addTab("Filter", null, filterTab,
				"Does twice as much nothing");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		addSetting(tabbedPane,"","");
		// Vertical spacing
		add(new JPanel(new GridBagLayout()));

		//Where member variables are declared:
		JProgressBar progressBar;
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(50);
		progressBar.setStringPainted(true);

		addSetting(progressBar,"Progress","currently rendering...");

	}
	
	// ==== Private Helper Methods ====
	
	private void addSetting(JComponent control, String title, String text) {
		JLabel label = new JLabel(title);
		label.setFont(label.getFont().deriveFont(Font.BOLD));

		// setting maximum size to comply with BoxLayout
		control.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		control.setMaximumSize(new Dimension(300, control.getPreferredSize().height));
		
		// use cheap "<html>" to enable line-wrapping
		JLabel help = new JLabel("<html>" + text + "</html>");
		help.setFont(label.getFont().deriveFont(Font.ITALIC, 10));
	
		add(label);
		add(Box.createRigidArea(new Dimension(0, 3)));
		add(control);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(help);
		add(Box.createRigidArea(new Dimension(0, 15)));
	}
	
	// ==== Observer Implementation ====

	@Override
	public void update(Observable o, Object arg) {
		if (o == controller.getModel()) {
			// TODO What happens when Model updates the controls?
			
			// We would like to update the existing values in the ControlPanel to reflect
			// the new state of the Model
		}
	}
	
	
	// ==== ActionListener Implementation ====
	
	// This method triggers whenever a control is manipulated
	// Use this to alter the Model by calling its appropriate Models

	@Override
	public void actionPerformed(ActionEvent e) {
//		final Object source = e.getSource();
		
		// TODO Add if-statements to determine appropriate actions
		
	}
	
	
	// ==== ItemListener Implementation ====

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	// ==== ChangeListener Implementation ====

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}



}
