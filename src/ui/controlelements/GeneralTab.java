package ui.controlelements;

import ui.controlelements.ControlPanelTab;

import javax.swing.*;

/**
 * Created by james on 17/02/16.
 */
public class GeneralTab extends ControlPanelTab {

    public GeneralTab(){
        JLabel filler = new JLabel("General");
        filler.setHorizontalAlignment(JLabel.CENTER);

        add(filler);

        addSetting(new JLabel("hi"),"title","this is a title");
    }

}
