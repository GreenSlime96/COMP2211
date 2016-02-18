package ui.controlelements;


import core.Model;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

/**
 * Created by jamesadams on 18/02/2016.
 */
public class FilterTab extends ControlPanelBox {

    Box genderBox = new Box(BoxLayout.X_AXIS);
    Box ageBox = new Box(BoxLayout.Y_AXIS);
    Box incomeBox = new Box(BoxLayout.Y_AXIS);
    Box contextBox = new Box(BoxLayout.Y_AXIS);
    ArrayList<JCheckBox> genderBoxes, ageBoxes, incomeBoxes, contextBoxes;
    FilterTabController filterTabController;
    public FilterTab(Model model){
        super(model);

        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");	genders.add("Female");

        ArrayList<String> ages = new ArrayList<>();
        ages.add("<25"); ages.add("25-34"); ages.add("35-44"); ages.add("45-54"); ages.add(">54");

        ArrayList<String> incomes = new ArrayList<>();
        incomes.add("Low"); incomes.add("Medium"); incomes.add("High");

        ArrayList<String> contexts = new ArrayList<>();
        contexts.add("News"); contexts.add("Shopping"); contexts.add("Social Media"); contexts.add("Blog");
        contexts.add("Hobbies"); contexts.add("Travel");

        genderBoxes = createCheckBoxGroup(genders,genderBox);
        ageBoxes = createCheckBoxGroup(ages,ageBox);
        incomeBoxes = createCheckBoxGroup(incomes,incomeBox);
        contextBoxes = createCheckBoxGroup(contexts,contextBox);

        addSetting(genderBox,"Gender","Filter by Gender");
        addSetting(ageBox,"Age","Filter by Age");
        addSetting(incomeBox,"Income","Filter by Income");
        addSetting(contextBox,"Context","Filter by Context");

        filterTabController = new FilterTabController(model);

        registerFilterBoxes(genderBoxes);
        registerFilterBoxes(ageBoxes);
        registerFilterBoxes(incomeBoxes);
        registerFilterBoxes(contextBoxes);
    }

    public void registerFilterBoxes(ArrayList<JCheckBox> checkBoxes){
        for(JCheckBox checkBox : checkBoxes){
            checkBox.addActionListener(filterTabController);
        }
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

    public ArrayList<JCheckBox> createCheckBoxGroup(ArrayList<String> nameList, Box buttonBox){
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for(String title : nameList){
            JCheckBox checkBox = new JCheckBox(title);
            checkBox.setSelected(true);
            buttonBox.add(checkBox);
            checkBoxes.add(checkBox);
        }
        return checkBoxes;
    }


    @Override
    public void update(Observable o, Object arg) {

    }


    class FilterTabController implements ActionListener,
            ChangeListener, ItemListener, ListSelectionListener {

        private Model model = null;

        boolean[] genderArray = new boolean[2];
        boolean[] ageArray = new boolean[5];
        boolean[] incomeArray = new boolean[3];
        boolean[] contextArray = new boolean[6];

        public FilterTabController(Model model){
            model = model;

            Arrays.fill(genderArray,true);
            Arrays.fill(ageArray,true);
            Arrays.fill(incomeArray,true);
            Arrays.fill(contextArray,true);


            verifyCheckBoxGroup(genderBoxes,genderArray);
            verifyCheckBoxGroup(ageBoxes,ageArray);
            verifyCheckBoxGroup(incomeBoxes,incomeArray);
            verifyCheckBoxGroup(contextBoxes,contextArray);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("STATE CHANGED!");

            if(e.getSource() instanceof JCheckBox){
                System.out.println("Checkbox was Ticked");
                JCheckBox checkBox = (JCheckBox) e.getSource();
                switch (checkBox.getText()) {
                    case "Male":
                        genderArray[0] = checkBox.isSelected();
                        break;
                    case "Female":
                        genderArray[1] = checkBox.isSelected();
                        break;
                    case "<25":
                        ageArray[0] = checkBox.isSelected();
                        break;
                    case "25-34":
                        ageArray[1] = checkBox.isSelected();
                        break;
                    case "35-44":
                        ageArray[2] = checkBox.isSelected();
                        break;
                    case "45-54":
                        ageArray[3] = checkBox.isSelected();
                        break;
                    case ">54":
                        ageArray[4] = checkBox.isSelected();
                        break;
                    case "Low":
                        incomeArray[0] = checkBox.isSelected();
                        break;
                    case "Medium":
                        incomeArray[1] = checkBox.isSelected();
                        break;
                    case "High":
                        incomeArray[2] = checkBox.isSelected();
                        break;
                    case "News":
                        contextArray[0] = checkBox.isSelected();
                        break;
                    case "Shopping":
                        contextArray[1] = checkBox.isSelected();
                        break;
                    case "Social Media":
                        contextArray[2] = checkBox.isSelected();
                        break;
                    case "Blog":
                        contextArray[3] = checkBox.isSelected();
                        break;
                    case "Hobbies":
                        contextArray[4] = checkBox.isSelected();
                        break;
                    case "Travel":
                        contextArray[5] = checkBox.isSelected();
                        break;
                }

                verifyCheckBoxGroup(genderBoxes,genderArray);
                verifyCheckBoxGroup(ageBoxes,ageArray);
                verifyCheckBoxGroup(incomeBoxes,incomeArray);
                verifyCheckBoxGroup(contextBoxes,contextArray);

            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {

        }

        @Override
        public void itemStateChanged(ItemEvent e) {

        }

        @Override
        public void valueChanged(ListSelectionEvent e) {

        }

        public void verifyCheckBoxGroup(ArrayList<JCheckBox> checkBoxes,boolean[] filterArr){
            boolean allfalse = true;
            for (int i = 0; i < filterArr.length; i++) {
                System.out.println(filterArr[i]);
                if (filterArr[i]){
                    allfalse = false;
                    break;
                }else {
                    allfalse = true;
                }
            }

            if(allfalse) {
                System.out.println("EVERYTHING IS FALSE");
                Arrays.fill(filterArr,true);
                for(JCheckBox checkBox : checkBoxes ) {
                    checkBox.setSelected(true);
                }
            }
        }
    }



}
