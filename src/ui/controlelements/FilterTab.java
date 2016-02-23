package ui.controlelements;


import core.Model;
import core.data.UserFields;

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

    public void registerFilterBoxes(ArrayList<JCheckBox> checkBoxes) {
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.addActionListener(filterTabController);
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
        System.out.println("General Tab Updating");

        if(o == model){
            genderBoxes.get(0).setSelected(model.getFieldFilteredValue(UserFields.GENDER_MALE));
            genderBoxes.get(1).setSelected(model.getFieldFilteredValue(UserFields.GENDER_FEMALE));

            ageBoxes.get(0).setSelected(model.getFieldFilteredValue(UserFields.AGE_BELOW_25));
            ageBoxes.get(1).setSelected(model.getFieldFilteredValue(UserFields.AGE_25_TO_34));
            ageBoxes.get(2).setSelected(model.getFieldFilteredValue(UserFields.AGE_35_TO_44));
            ageBoxes.get(3).setSelected(model.getFieldFilteredValue(UserFields.AGE_45_TO_54));
            ageBoxes.get(4).setSelected(model.getFieldFilteredValue(UserFields.AGE_ABOVE_54));

            incomeBoxes.get(0).setSelected(model.getFieldFilteredValue(UserFields.INCOME_LOW));
            incomeBoxes.get(1).setSelected(model.getFieldFilteredValue(UserFields.INCOME_MEDIUM));
            incomeBoxes.get(2).setSelected(model.getFieldFilteredValue(UserFields.INCOME_HIGH));

            contextBoxes.get(0).setSelected(model.getFieldFilteredValue(UserFields.CONTEXT_NEWS));
            contextBoxes.get(1).setSelected(model.getFieldFilteredValue(UserFields.CONTEXT_SHOPPING));
            contextBoxes.get(2).setSelected(model.getFieldFilteredValue(UserFields.CONTEXT_SOCIAL_MEDIA));
            contextBoxes.get(3).setSelected(model.getFieldFilteredValue(UserFields.CONTEXT_BLOG));
            contextBoxes.get(4).setSelected(model.getFieldFilteredValue(UserFields.CONTEXT_HOBBIES));
            contextBoxes.get(5).setSelected(model.getFieldFilteredValue(UserFields.CONTEXT_TRAVEL));

        }
    }


    class FilterTabController implements ActionListener,
            ChangeListener, ItemListener, ListSelectionListener {

        private Model model = null;

        public FilterTabController(Model model){
            model = model;

            verifyCheckBoxGroup(genderBoxes);
            verifyCheckBoxGroup(ageBoxes);
            verifyCheckBoxGroup(incomeBoxes);
            verifyCheckBoxGroup(contextBoxes);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() instanceof JCheckBox){
                JCheckBox checkBox = (JCheckBox) e.getSource();
                switch (checkBox.getText()) {
                    case "Male":
                        model.setFieldFilteredValue(UserFields.GENDER_MALE, checkBox.isSelected()); break;
                    case "Female":
                        model.setFieldFilteredValue(UserFields.GENDER_FEMALE, checkBox.isSelected()); break;
                    case "<25":
                        model.setFieldFilteredValue(UserFields.AGE_BELOW_25, checkBox.isSelected()); break;
                    case "25-34":
                        model.setFieldFilteredValue(UserFields.AGE_25_TO_34, checkBox.isSelected()); break;
                    case "35-44":
                        model.setFieldFilteredValue(UserFields.AGE_35_TO_44, checkBox.isSelected()); break;
                    case "45-54":
                        model.setFieldFilteredValue(UserFields.AGE_45_TO_54, checkBox.isSelected()); break;
                    case ">54":
                        model.setFieldFilteredValue(UserFields.AGE_ABOVE_54, checkBox.isSelected()); break;
                    case "Low":
                        model.setFieldFilteredValue(UserFields.INCOME_LOW, checkBox.isSelected()); break;
                    case "Medium":
                        model.setFieldFilteredValue(UserFields.INCOME_MEDIUM, checkBox.isSelected()); break;
                    case "High":
                        model.setFieldFilteredValue(UserFields.INCOME_HIGH, checkBox.isSelected()); break;
                    case "News":
                        model.setFieldFilteredValue(UserFields.CONTEXT_NEWS, checkBox.isSelected()); break;
                    case "Shopping":
                        model.setFieldFilteredValue(UserFields.CONTEXT_SHOPPING, checkBox.isSelected()); break;
                    case "Social Media":
                        model.setFieldFilteredValue(UserFields.CONTEXT_SOCIAL_MEDIA, checkBox.isSelected()); break;
                    case "Blog":
                        model.setFieldFilteredValue(UserFields.CONTEXT_BLOG, checkBox.isSelected()); break;
                    case "Hobbies":
                        model.setFieldFilteredValue(UserFields.CONTEXT_HOBBIES, checkBox.isSelected()); break;
                    case "Travel":
                        model.setFieldFilteredValue(UserFields.CONTEXT_TRAVEL, checkBox.isSelected()); break;
                }

                verifyCheckBoxGroup(genderBoxes);
                verifyCheckBoxGroup(ageBoxes);
                verifyCheckBoxGroup(incomeBoxes);
                verifyCheckBoxGroup(contextBoxes);
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

        public void verifyCheckBoxGroup(ArrayList<JCheckBox> checkBoxes){
            boolean allfalse = true;
            for(JCheckBox checkBox : checkBoxes ) {
                if (checkBox.isSelected()){
                    allfalse = false;
                    break;
                }else {
                    allfalse = true;
                }
            }

            if(allfalse) {
                for(JCheckBox checkBox : checkBoxes ) {
                    checkBox.setSelected(true);
                }
            }
        }
    }



}
