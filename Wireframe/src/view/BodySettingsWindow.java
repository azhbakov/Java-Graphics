package view;

import model.Logic;
import model.RotationBody;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 16.04.2016.
 */
public class BodySettingsWindow extends JFrame implements Observer{
    Dimension size = new Dimension(800, 600);
    Dimension curvePanelSize = new Dimension(800, 500);
    CurvePanel curvePanel;
    Logic logic;
    JSpinner bodySpinner;


    public BodySettingsWindow (Logic logic) {
        this.logic = logic;
        logic.addObserver(this);

        curvePanel = new CurvePanel(curvePanelSize);
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add (initFieldPanel());
        p.add (initButtonPanel());
        add(curvePanel, BorderLayout.CENTER);
        add(p, BorderLayout.SOUTH);
        showCurrentCurve();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel initFieldPanel () {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(1, 2, 10, 10));
        bodySpinner = new JSpinner(new SpinnerNumberModel(1, 1, logic.getWorld().getBodiesNum(), 1));
        bodySpinner.setEditor(new JSpinner.NumberEditor(bodySpinner, "#"));
        bodySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("Body: "));
        fieldPanel.add(bodySpinner);
        return fieldPanel;
    }

    protected void showCurrentCurve () {
        RotationBody r = (RotationBody)(logic.getWorld().getBody((int)bodySpinner.getValue()-1));
        if (r == null) {
            //curvePanel.loadCurve(null);
            curvePanel.loadPoints(null);
            System.out.println("NULL");
        } else {
            //curvePanel.loadCurve(r.getCurve());
            curvePanel.loadPoints(r.getMarkers());
            System.out.println("LOADED");
        }
    }

    public void update (Observable observable, Object object) {
        SpinnerNumberModel m = (SpinnerNumberModel) bodySpinner.getModel();
        m.setMaximum(logic.getWorld().getBodiesNum());
    }

    private JPanel initButtonPanel () {
        JPanel buttonPanel = new JPanel();

        JButton applyButton = new JButton("Apply");
        applyButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RotationBody r = (RotationBody)(logic.getWorld().getBody((int)bodySpinner.getValue()-1));
                if (r == null) {
                    logic.getWorld().addBody(new RotationBody(curvePanel.getMarkers(), 0, 0,0,0, 0,0,0));
                } else {
                    //r.setCurve(curvePanel.getCurve());
                    r.setMarkers(curvePanel.getMarkers());
                }

                logic.notifyObservers();
                dispose();
            }
        });
        applyButton.setText("Apply");
        buttonPanel.add(applyButton);

        JButton addButton = new JButton("Add");
        addButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curvePanel.setMouseMode(CurvePanel.MouseMode.ADD);
            }
        });
        addButton.setText("Add point");
        buttonPanel.add(addButton);

        JButton removeButton = new JButton("Remove");
        removeButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curvePanel.setMouseMode(CurvePanel.MouseMode.REMOVE);
            }
        });
        removeButton.setText("Remove point");
        buttonPanel.add(removeButton);

        JButton addBodyButton = new JButton("Add");
        addBodyButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //logic.getWorld().addBody(new RotationBody(curvePanel.getCurve(), 0, 0,0,0, 0,0,0));
            }
        });
        addBodyButton.setText("Add body");
        buttonPanel.add(addBodyButton);

        JButton removeBodyButton = new JButton("Remove");
        removeBodyButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curvePanel.setMouseMode(CurvePanel.MouseMode.REMOVE);
            }
        });
        removeBodyButton.setText("Remove body");
        buttonPanel.add(removeBodyButton);

        return buttonPanel;
    }

}
