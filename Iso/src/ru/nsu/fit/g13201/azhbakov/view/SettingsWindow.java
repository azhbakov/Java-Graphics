package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.model.Logic;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 01.03.2016.
 */
public class SettingsWindow extends JFrame implements Observer {
    final Logic logic;
    JFormattedTextField aField, bField, cField, dField, kField, mField;

    public SettingsWindow(Logic l) {
        super("Settings");
        logic = l;
        logic.addObserver(this);;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        //setLocationByPlatform(true);
        //settingsFrame.setMinimumSize(new Dimension(100, 100));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel floatParameters = new JPanel(new GridLayout(2,6, 5, 5));
        aField = createFormattedTextField((float)logic.getA());
        floatParameters.add(new JLabel("Xmin"));
        floatParameters.add(aField);
        getContentPane().add(floatParameters);

        bField = createFormattedTextField((float)logic.getB());
        floatParameters.add(new JLabel("Xmax"));
        floatParameters.add(bField);
        getContentPane().add(floatParameters);

        cField = createFormattedTextField((float)logic.getC());
        floatParameters.add(new JLabel("Ymin"));
        floatParameters.add(cField);
        getContentPane().add(floatParameters);

        dField = createFormattedTextField((float)logic.getD());
        floatParameters.add(new JLabel("Ymax"));
        floatParameters.add(dField);
        getContentPane().add(floatParameters);

        kField = createFormattedTextField((int)logic.getK());
        floatParameters.add(new JLabel("Steps X"));
        floatParameters.add(kField);
        getContentPane().add(floatParameters);

        mField = createFormattedTextField((int)logic.getM());
        floatParameters.add(new JLabel("Steps Y"));
        floatParameters.add(mField);
        getContentPane().add(floatParameters);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //applySettings (new GameSettings(rowsPanel.getValue(), colsPanel.getValue(), borderWidth, cellSize, 0, null, null));
                logic.setA(((Number)aField.getValue()).floatValue());
                logic.setB(((Number)bField.getValue()).floatValue());
                logic.setC(((Number)cField.getValue()).floatValue());
                logic.setD(((Number)dField.getValue()).floatValue());
                logic.setK(((Number)kField.getValue()).intValue());
                //System.out.println(((Number)dField.getValue()).floatValue());
                logic.setM(((Number)mField.getValue()).intValue());
                dispose();
            }
        });
        getContentPane().add(applyButton);

        pack();
        setResizable(false);
        setVisible(true);
    }

    public void update (Observable observable, Object obj) {
//        aField.setValue(logic.getA());
//        bField.setValue(logic.getB());
//        cField.setValue(logic.getC());
//        dField.setValue(logic.getD());
//        kField.setValue(logic.getK());
//        mField.setValue(logic.getM());
    }

    private JFormattedTextField createFormattedTextField (float init) {
        final JFormattedTextField textField;
        NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0.0"));
        //formatter.setMinimum(min);
        //formatter.setMaximum(max);
        textField = new JFormattedTextField(formatter);
        textField.setValue(init);
        textField.setColumns(5); //get some space
        textField.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0),
                "check");
        textField.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!textField.isEditValid()) { //The text is invalid.
                    Toolkit.getDefaultToolkit().beep();
                    textField.selectAll();
                } else try {                    //The text is valid,
                    textField.commitEdit();     //so use it.
                    //System.out.println(((Number)textField.getValue()).floatValue());
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                } catch (java.text.ParseException exc) {
                    exc.printStackTrace();
                }
            }
        });
        return textField;
    }
}
