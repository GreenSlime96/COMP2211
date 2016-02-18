package ui.controlelements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import core.Model;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by james on 17/02/16.
 */
abstract class ControlPanelBox extends Box implements Observer{

	protected Model model;
	
	private static final long serialVersionUID = -3828765871783229793L;

	public ControlPanelBox(Model model){
        super(BoxLayout.Y_AXIS);
        this.model = model;
        model.addObserver(this);
    }

    protected void addSetting(JComponent control, String title, String text) {
        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 12));
        //fixes [g|y] not rendering correctly.
        label.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

        // setting maximum size to comply with BoxLayout
        control.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        control.setMaximumSize(new Dimension(300, control.getPreferredSize().height));


        // use cheap "<html>" to enable line-wrapping
        JLabel help = new JLabel("<html>" + text + "</html>");
        help.setFont(label.getFont().deriveFont(Font.ITALIC, 9));
        help.setBorder(BorderFactory.createEmptyBorder(0,0,3,0));

        add(label);
        add(Box.createRigidArea(new Dimension(0, 3)));
        add(control);
        add(Box.createRigidArea(new Dimension(0, 2)));
        add(help);
        add(Box.createRigidArea(new Dimension(0, 15)));
    }

}
