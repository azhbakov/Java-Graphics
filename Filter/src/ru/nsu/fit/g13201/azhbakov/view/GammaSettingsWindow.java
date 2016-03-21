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
public class GammaSettingsWindow extends JFrame implements Observer {
    final Logic logic;
    JFormattedTextField gamma;

    public GammaSettingsWindow(Logic l) {
        super("Gamma correction");
        logic = l;
        logic.addObserver(this);;
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        //setLocationByPlatform(true);
        //settingsFrame.setMinimumSize(new Dimension(100, 100));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel floatParameters = new JPanel(new GridLayout(2,1, 5, 5));
        gamma = createFormattedTextField((float)logic.getGamma());
        floatParameters.add(new JLabel("Gamma"));
        floatParameters.add(gamma);
        getContentPane().add(floatParameters);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //applySettings (new GameSettings(rowsPanel.getValue(), colsPanel.getValue(), borderWidth, cellSize, 0, null, null));
                logic.setGamma((double)((Number)gamma.getValue()).floatValue());
                logic.gamma();
                dispose();
            }
        });
        getContentPane().add(applyButton);

        pack();
        setResizable(false);
        setVisible(true);
    }

    public void update (Observable observable, Object obj) {
        gamma.setValue(logic.getGamma());
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
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                } catch (java.text.ParseException exc) {
                }
            }
        });
        return textField;
    }
}
