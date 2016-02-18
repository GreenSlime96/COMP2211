package ui.controlelements;


import core.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by jamesadams on 18/02/2016.
 */
public class FilterTab extends ControlPanelBox {


    Box genderBox = new Box(BoxLayout.X_AXIS);
    Box ageBox = new Box(BoxLayout.Y_AXIS);
    Box incomeBox = new Box(BoxLayout.Y_AXIS);
    Box contextBox = new Box(BoxLayout.Y_AXIS);
    public FilterTab(Model model){
        super(model);

        ArrayList<String> genders = new ArrayList<>();
        genders.add("Any"); genders.add("Male");	genders.add("Female");

        ArrayList<String> ages = new ArrayList<>();
        ages.add("Any"); ages.add("<25"); ages.add("25-34"); ages.add("35-44"); ages.add("45-54"); ages.add(">54");

        ArrayList<String> incomes = new ArrayList<>();
        incomes.add("Any"); incomes.add("Low"); incomes.add("Medium"); incomes.add("High");

        ArrayList<String> contexts = new ArrayList<>();
        contexts.add("Any"); contexts.add("News"); contexts.add("Shopping"); contexts.add("Social Media"); contexts.add("Blog");
        contexts.add("Hobbies"); contexts.add("Travel");

        createRadioButtonGroup(genders,genderBox);
        createCheckBoxGroup(ages,ageBox);
        createCheckBoxGroup(incomes,incomeBox);
        createCheckBoxGroup(contexts,contextBox);

        addSetting(genderBox,"Gender","Filter by Gender");
        addSetting(ageBox,"Age","Filter by Age");
        addSetting(incomeBox,"Income","Filter by Income");
        addSetting(contextBox,"Context","Filter by Context");
    }

    public void createRadioButtonGroup(ArrayList<String> nameList, Box buttonBox){
        ButtonGroup group = new ButtonGroup();
        for(String title : nameList){
            JRadioButton radioButton = new JRadioButton(title);
            radioButton.setMnemonic(KeyEvent.VK_P);
            radioButton.setActionCommand(title);
            buttonBox.add(radioButton);
            group.add(radioButton);
        }
    }

    public void createCheckBoxGroup(ArrayList<String> nameList, Box buttonBox){
        for(String title : nameList){
            JCheckBox checkBox = new JCheckBox(title);
            buttonBox.add(checkBox);
        }
    }


    @Override
    public void update(Observable o, Object arg) {

    }

    private static class ClickAction implements ActionListener {

        private JPopupMenu menu;
        private JButton button;
        private JMenuItem item;
        private boolean active;
        private ClickAction(JPopupMenu menu, JButton button, JMenuItem item) {
            this.menu = menu;
            this.button = button;
            this.item = item;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            active = (active) ? false : true;

            if(item == menu.getComponent(0)){
                for (int i = 1; i < menu.getComponents().length; i++) {
                    JMenuItem menuItem = (JMenuItem) menu.getComponents()[i];
                    if(menuItem.isSelected()) menuItem.doClick();
                }
                button.setText(item.getText());
            }else {
                if (active) {
                    button.setText(button.getText() + " " + item.getText() + " ");
                    button.setText(button.getText().replace("Any",""));
                    JMenuItem menuItem = (JMenuItem) menu.getComponent(0);
                    menuItem.setSelected(false);

                } else {
                    button.setText(button.getText().replace(" " + item.getText() + " ", ""));
                }
            }
            button.invalidate();

        }
    }

//    public void setupMultipleDropdown(ArrayList<String> options, JButton button){
//
//        JPopupMenu popupMenu = new JPopupMenu();
//
//        button.addActionListener(e -> {
//            if (!popupMenu.isVisible()) {
//                Point p = button.getLocationOnScreen();
//                popupMenu.setInvoker(button);
//                popupMenu.setLocation((int) p.getX(),
//                        (int) p.getY() + button.getHeight());
//                popupMenu.setVisible(true);
//            } else {
//                popupMenu.setVisible(false);
//            }
//        });
//
//        for (String s : options){
//            JMenuItem item = new JCheckBoxMenuItem(s);
//            popupMenu.add(item);
//            item.addActionListener(new ClickAction(popupMenu, button, item));
//        }
//
//        popupMenu.setSelected(popupMenu.getComponent(0));
//        JMenuItem menuItem = (JMenuItem) popupMenu.getComponent(0);
//        menuItem.doClick();
//
//
//    }

    }
