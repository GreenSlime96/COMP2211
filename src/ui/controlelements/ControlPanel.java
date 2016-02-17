package ui.controlelements;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Model;

public class ControlPanel extends JPanel implements Observer, ActionListener, ChangeListener, ItemListener {

	// ==== Constants ====
	
	private static final long serialVersionUID = 824395947852730145L;
	
	// ==== Properties ====	
	
	private final Model model;
	
	// Add controls here
	/* private final JSpinner ...
	 * private final JTextField ...
	 * private ...
	 */
	
	// ==== Constructor ====
	
	public ControlPanel(Model model) {
		
		// Register as an Observer of Model
		this.model = model;
		model.addObserver(this);
		
		// Add Borders for visual appeal!!!
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(280, 600));
		setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.DARK_GRAY));

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

		add(tabbedPane, BorderLayout.CENTER);
		// Vertical spacing

		add(new ProgressBox(),BorderLayout.SOUTH);
		
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
		if (o == model) {
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
