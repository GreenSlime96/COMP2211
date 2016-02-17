package ui.controlelements;

import ui.controlelements.ControlPanelTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by james on 17/02/16.
 */
public class GeneralTab extends ControlPanelTab {

//    JLabel empty = new JLabel("");
    JLabel noImpressionsLabel = new JLabel("######");
    JLabel startDateLabel = new JLabel("######");
    JLabel endDateLabel = new JLabel("######");
    JLabel totalClicksLabel =  new JLabel("######");
    JLabel campaignDirectorLabel = new JLabel("######");

    public GeneralTab(){

        addSetting(new JTable(3,1),"Campaigns","Click to show stats below");

        //campaign metrics
//        addSetting(empty, "", "" );
        addSetting(noImpressionsLabel, "Impressions", "" );
        addSetting(startDateLabel, "Start Date", "" );
        addSetting(endDateLabel, "End Date", "" );
        addSetting(totalClicksLabel, "Total Clicks", "" );
        addSetting(campaignDirectorLabel, "Campaign Directory", "" );
    }

}
