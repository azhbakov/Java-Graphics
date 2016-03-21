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
public class OrderedDitheringSettingsWindow extends JFrame implements Observer {
    final Logic logic;
    SliderPanel palettePanel;

    public OrderedDitheringSettingsWindow(Logic l) {
        super("Ordered dithering");
        logic = l;
        logic.addObserver(this);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        //setLocationByPlatform(true);
        //settingsFrame.setMinimumSize(new Dimension(100, 100));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        palettePanel = new SliderPanel("Palette levels", 0, 24, (int)logic.getPalleteLevels());
        getContentPane().add(palettePanel);
//        anglePanel.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                logic.setRotationDeg(anglePanel.getValue());
//            }
//        });


        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //applySettings (new GameSettings(rowsPanel.getValue(), colsPanel.getValue(), borderWidth, cellSize, 0, null, null));
                logic.setPalleteLevels(palettePanel.getValue());
                logic.ditheringOrdered();
                dispose();
            }
        });
        getContentPane().add(applyButton);

        pack();
        setResizable(false);
        setVisible(true);
    }

    public void update (Observable observable, Object obj) {
        palettePanel.setValue((int)logic.getPalleteLevels());
    }

}
