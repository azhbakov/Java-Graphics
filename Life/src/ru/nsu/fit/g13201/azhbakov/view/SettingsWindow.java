package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.game_of_life.Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    final Game game;
    SliderPanel rowsPanel;
    SliderPanel colsPanel;
    JFormattedTextField liveBegin, liveEnd, birthBegin, birthEnd, fstImpact, sndImpact;

    public SettingsWindow(Game g) {
        super("Parameters");
        game = g;
        game.addObserver(this);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        //setLocationByPlatform(true);
        //settingsFrame.setMinimumSize(new Dimension(100, 100));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        rowsPanel = new SliderPanel("Rows", game.getMinHeight(), game.getMaxHeight(), game.getHeight());
        getContentPane().add(rowsPanel);
        rowsPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                game.setHeight(rowsPanel.getValue());
            }
        });
        colsPanel = new SliderPanel("Cols", game.getMinWidth(), game.getMaxWidth(), game.getWidth());
        getContentPane().add(colsPanel);
        colsPanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                game.setWidth(colsPanel.getValue());
            }
        });

        JPanel floatParameters = new JPanel(new GridLayout(3,4, 5, 5));
        liveBegin = createFormattedTextField(game.getLIVE_BEGIN());
        floatParameters.add(new JLabel("LIVE_BEGIN"));
        floatParameters.add(liveBegin);
        liveEnd = createFormattedTextField(game.getLIVE_END());
        floatParameters.add(new JLabel("LIVE_END"));
        floatParameters.add(liveEnd);
        birthBegin = createFormattedTextField(game.getBIRTH_BEGIN());
        floatParameters.add(new JLabel("BIRTH_BEGIN"));
        floatParameters.add(birthBegin);
        birthEnd = createFormattedTextField(game.getBIRTH_END());
        floatParameters.add(new JLabel("BIRTH_END"));
        floatParameters.add(birthEnd);
        fstImpact = createFormattedTextField(game.getFST_IMPACT());
        floatParameters.add(new JLabel("FST_IMPACT"));
        floatParameters.add(fstImpact);
        sndImpact = createFormattedTextField(game.getSND_IMPACT());
        floatParameters.add(new JLabel("SND_IMPACT"));
        floatParameters.add(sndImpact);
        floatParameters.setBorder(BorderFactory.createTitledBorder("Simulation parameters"));
        add(floatParameters);

        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //applySettings (new GameSettings(rowsPanel.getValue(), colsPanel.getValue(), borderWidth, cellSize, 0, null, null));
                game.setSimulationParameters(((Number)liveBegin.getValue()).floatValue(), ((Number)liveEnd.getValue()).floatValue(),
                        ((Number)birthBegin.getValue()).floatValue(), ((Number)birthEnd.getValue()).floatValue(),
                        ((Number)fstImpact.getValue()).floatValue(), ((Number) sndImpact.getValue()).floatValue());

                dispose();
            }
        });
        getContentPane().add(applyButton);

        pack();
        setResizable(false);
        setVisible(true);
    }

    public void update (Observable observable, Object obj) {
        rowsPanel.setMin(game.getMinHeight());
        rowsPanel.setValue(game.getHeight());
        rowsPanel.setMax(game.getMaxHeight());
        colsPanel.setMin(game.getMinWidth());
        colsPanel.setValue(game.getWidth());
        colsPanel.setMax(game.getMaxWidth());

        liveBegin.setValue(game.getLIVE_BEGIN());
        liveEnd.setValue(game.getLIVE_END());
        birthBegin.setValue(game.getBIRTH_BEGIN());
        birthEnd.setValue(game.getBIRTH_END());
        fstImpact.setValue(game.getFST_IMPACT());
        sndImpact.setValue(game.getSND_IMPACT());
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
