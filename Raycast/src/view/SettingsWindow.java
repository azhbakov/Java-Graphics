package view;

import model.Logic;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Martin on 23.05.2016.
 */
public class SettingsWindow extends JFrame{
    JSpinner gamma;
    JSpinner depth;
    JSpinner quality;
    JSpinner rSpinner, gSpinner, bSpinner;

    public SettingsWindow (Logic logic) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(6, 2, 10, 10));
        gamma = new JSpinner(new SpinnerNumberModel(logic.getCamera().gamma, 0, 10, 1));
        gamma.setEditor(new JSpinner.NumberEditor(gamma, "#.#"));
        gamma.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().gamma = ((SpinnerNumberModel)gamma.getModel()).getNumber().floatValue();
            }
        });
        fieldPanel.add(new JLabel("Gamma: "));
        fieldPanel.add(gamma);

        depth = new JSpinner(new SpinnerNumberModel(logic.getCamera().rayMax, 1, 10, 1));
        depth.setEditor(new JSpinner.NumberEditor(depth, "#"));
        depth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().rayMax = (int)depth.getValue();
            }
        });
        fieldPanel.add(new JLabel("Depth: "));
        fieldPanel.add(depth);

        quality = new JSpinner(new SpinnerNumberModel(logic.getCamera().quality, 1, 3, 1));
        quality.setEditor(new JSpinner.NumberEditor(quality, "#"));
        quality.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().quality = (int)quality.getValue();
            }
        });
        fieldPanel.add(new JLabel("Quality: "));
        fieldPanel.add(quality);

        rSpinner = new JSpinner(new SpinnerNumberModel((int)logic.getCamera().ambientColor.x*255, 0, 255, 1));
        rSpinner.setEditor(new JSpinner.NumberEditor(rSpinner, "#"));
        rSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().ambientColor.x = (float)rSpinner.getValue()/255;
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("R: "));
        fieldPanel.add(rSpinner);

        gSpinner = new JSpinner(new SpinnerNumberModel((int)logic.getCamera().ambientColor.y*255, 0, 255, 1));
        gSpinner.setEditor(new JSpinner.NumberEditor(gSpinner, "#"));
        gSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().ambientColor.y = (float)gSpinner.getValue()/255;
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("G: "));
        fieldPanel.add(gSpinner);

        bSpinner = new JSpinner(new SpinnerNumberModel((int)logic.getCamera().ambientColor.z*255, 0, 255, 1));
        bSpinner.setEditor(new JSpinner.NumberEditor(bSpinner, "#"));
        bSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().ambientColor.z = (float)bSpinner.getValue()/255;
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("B: "));
        fieldPanel.add(bSpinner);

        add(fieldPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
