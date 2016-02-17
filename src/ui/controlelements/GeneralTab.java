package ui.controlelements;

import ui.controlelements.ControlPanelBox;

import javax.swing.*;

import core.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/**
 * Created by james on 17/02/16.
 */
public class GeneralTab extends ControlPanelBox {

//    JLabel empty = new JLabel("");
    String[] arr = {"Campaign 1", "Campaign 2"};
    JList<String> campaignList= new JList<>(arr);
    JLabel noImpressionsLabel = new JLabel("######");
    JLabel startDateLabel = new JLabel("######");
    JLabel endDateLabel = new JLabel("######");
    JLabel totalClicksLabel =  new JLabel("######");
    JLabel campaignDirectorLabel = new JLabel("######");

    public GeneralTab(Model model){
    	super(model);

        addSetting(campaignList,"Campaigns","Click to show stats below");

        //campaign metrics
        addSetting(noImpressionsLabel, "Impressions", "" );
        addSetting(startDateLabel, "Start Date", "" );
        addSetting(endDateLabel, "End Date", "" );
        addSetting(totalClicksLabel, "Total Clicks", "" );
        addSetting(campaignDirectorLabel, "Campaign Directory", "" );
    }

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}


}
