package ru.nsu.fit.g13201.azhbakov.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by azhbakov on 01.03.2016.
 */
public class SliderPanel extends JPanel {
    private final JSlider slider;
    public SliderPanel (String name, int slidexMin, int sliderMax, int sliderInit) {
        super();
        setLayout(new FlowLayout());
        slider = new JSlider(JSlider.HORIZONTAL, slidexMin, sliderMax, sliderInit);

        final JFormattedTextField textField;
        java.text.NumberFormat numberFormat =
                java.text.NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(numberFormat);
        formatter.setMinimum(new Integer(slidexMin));
        formatter.setMaximum(new Integer(sliderMax));
        textField = new JFormattedTextField(formatter);
        textField.setValue(sliderInit);
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
                    slider.setValue((Integer)textField.getValue());
                } catch (java.text.ParseException exc) { }
            }
        });

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                textField.setText(Integer.toString(slider.getValue()));
            }
        });
        setBorder(BorderFactory.createTitledBorder(name));
        add(slider);
        add(textField);
    }

    public void addChangeListener (ChangeListener changeListener) {
        slider.addChangeListener(changeListener);
    }

    public void removeChangeListener (ChangeListener changeListener) {
        slider.removeChangeListener(changeListener);
    }

    public void setMin (int min) {
        slider.setMinimum(min);
    }

    public void setMax (int max) {
        slider.setMaximum(max);
    }
    public void setValue (int value) {
        slider.setValue(value);
    }

    public int getValue () {
        return slider.getValue();
    }

}
