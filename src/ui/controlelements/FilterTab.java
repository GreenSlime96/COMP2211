package ui.controlelements;

import ui.controlelements.ControlPanelTab;

import javax.swing.*;

/**
 * Created by james on 17/02/16.
 */
public class FilterTab extends ControlPanelTab {

    public FilterTab() {
        JLabel filler = new JLabel("Filter");
        filler.setHorizontalAlignment(JLabel.CENTER);

        add(filler);
    }

}