package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.game_of_life.Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by azhbakov on 01.03.2016.
 */
public class PreferencesWindow extends JFrame implements Observer {

    final Game game;
    SliderPanel sizePanel, widthPanel;

    public PreferencesWindow(final Game g) {
        super("Settings");
        game = g;
        game.addObserver(this);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        //setLocationByPlatform(true);
        //settingsFrame.setMinimumSize(new Dimension(100, 100));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        sizePanel = new SliderPanel("Cell size", game.getMinCellSize(), game.getMaxCellSize(), game.getCellSize());
        sizePanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                game.setCellSize(sizePanel.getValue());
            }
        });
        getContentPane().add(sizePanel);
        widthPanel = new SliderPanel("Border width",game.getMinBorderWidth(), game.getMaxBorderWidth(), game.getBorderWidth());
        widthPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                game.setBorderWidth(widthPanel.getValue());
            }
        });
        getContentPane().add(widthPanel);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //applySettings(new GameSettings(rows, cols, widthPanel.getValue(), sizePanel.getValue(), 0, null, null));
                dispose();
            }
        });
        getContentPane().add(applyButton);

        pack();
        setResizable(false);
        setVisible(true);
    }

    public void update (Observable observable, Object obj) {
        sizePanel.setMin(game.getMinCellSize());
        sizePanel.setValue(game.getCellSize());
        sizePanel.setMax(game.getMaxCellSize());
        widthPanel.setMin(game.getMinBorderWidth());
        widthPanel.setValue(game.getBorderWidth());
        widthPanel.setMax(game.getMaxBorderWidth());
    }

}
