package ui.controlelements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

/**
 * Created by james on 17/02/16.
 */
public class ControlPanelTab extends Box {

    public ControlPanelTab(){
        super(BoxLayout.Y_AXIS);

    }

    protected void addSetting(JComponent control, String title, String text) {
        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));

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
