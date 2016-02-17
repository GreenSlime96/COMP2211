package ui.controlelements;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

import core.Controller;

/**
 * Created by james on 17/02/16.
 */
public class ControlPanelTab extends Box {

	protected Controller controller;
	
    public ControlPanelTab(Controller controller){
        super(BoxLayout.Y_AXIS);
        this.controller = controller;
    }

    protected void addSetting(JComponent control, String title, String text) {
        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

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


}
