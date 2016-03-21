package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.model.Logic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 01.03.2016.
 */
public class SobelSettingsWindow extends JFrame implements Observer {
    final Logic logic;
    SliderPanel thresholdPanel;

    public SobelSettingsWindow(Logic l) {
        super("Threshold");
        logic = l;
        logic.addObserver(this);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        //setLocationByPlatform(true);
        //settingsFrame.setMinimumSize(new Dimension(100, 100));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        thresholdPanel = new SliderPanel("Threshold", 0, 255, (int)logic.getThreshold());
        getContentPane().add(thresholdPanel);
//        anglePanel.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                logic.setRotationDeg(anglePanel.getValue());
//            }
//        });


        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //applySettings (new GameSettings(rowsPanel.getValue(), colsPanel.getValue(), borderWidth, cellSize, 0, null, null));
                logic.setThreshold(thresholdPanel.getValue());
                logic.sobel();
                dispose();
            }
        });
        getContentPane().add(applyButton);

        pack();
        setResizable(false);
        setVisible(true);
    }

    public void update (Observable observable, Object obj) {
        thresholdPanel.setValue((int)logic.getThreshold());
    }

}
