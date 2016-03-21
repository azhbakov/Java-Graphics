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
public class RotateSettingsWindow extends JFrame implements Observer {
    final Logic logic;
    SliderPanel anglePanel;

    public RotateSettingsWindow(Logic l) {
        super("Parameters");
        logic = l;
        logic.addObserver(this);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        //setLocationByPlatform(true);
        //settingsFrame.setMinimumSize(new Dimension(100, 100));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        anglePanel = new SliderPanel("Angle (deg)", -180, 180, (int)logic.getRotationDeg());
        getContentPane().add(anglePanel);
//        anglePanel.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                logic.setRotationDeg(anglePanel.getValue());
//            }
//        });


        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //applySettings (new GameSettings(rowsPanel.getValue(), colsPanel.getValue(), borderWidth, cellSize, 0, null, null));
                logic.setRotationDeg(anglePanel.getValue());
                logic.rotate();
                dispose();
            }
        });
        getContentPane().add(applyButton);

        pack();
        setResizable(false);
        setVisible(true);
    }

    public void update (Observable observable, Object obj) {
        anglePanel.setValue((int)logic.getRotationDeg());
    }

}
