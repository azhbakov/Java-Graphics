package view;

import model.Logic;
import model.RotationBody;
import model.WiredBody;

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
    JSpinner nSpinner, mSpinner, kSpinner;
    JSpinner rSpinner, gSpinner, bSpinner;
    JSpinner zfSpinner, zbSpinner, swSpinner, shSpinner;


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
        fieldPanel.setLayout(new GridLayout(2, 12, 10, 10));
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

        nSpinner = new JSpinner(new SpinnerNumberModel(logic.getN(), 3, 50, 1));
        nSpinner.setEditor(new JSpinner.NumberEditor(nSpinner, "#"));
        nSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                curvePanel.setNK ((int)nSpinner.getValue(), (int)kSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("n: "));
        fieldPanel.add(nSpinner);

        mSpinner = new JSpinner(new SpinnerNumberModel(logic.getM(), 3, 50, 1));
        mSpinner.setEditor(new JSpinner.NumberEditor(mSpinner, "#"));
        mSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                curvePanel.setNK ((int)nSpinner.getValue(), (int)kSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("m: "));
        fieldPanel.add(mSpinner);

        kSpinner = new JSpinner(new SpinnerNumberModel(logic.getK(), 1, 50, 1));
        kSpinner.setEditor(new JSpinner.NumberEditor(kSpinner, "#"));
        kSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                curvePanel.setNK ((int)nSpinner.getValue(), (int)kSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("k: "));
        fieldPanel.add(kSpinner);

        WiredBody body = logic.getWorld().getBody((int)bodySpinner.getValue());
        int r = 255, g = 255, b = 255;
        if (body != null) {
            r = body.getColor().getRed();
            g = body.getColor().getGreen();
            b = body.getColor().getBlue();
        }
        rSpinner = new JSpinner(new SpinnerNumberModel(r, 0, 255, 1));
        rSpinner.setEditor(new JSpinner.NumberEditor(rSpinner, "#"));
        rSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                curvePanel.setColor ((int)rSpinner.getValue(), (int)gSpinner.getValue(), (int)bSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("R: "));
        fieldPanel.add(rSpinner);

        gSpinner = new JSpinner(new SpinnerNumberModel(g, 0, 255, 1));
        gSpinner.setEditor(new JSpinner.NumberEditor(gSpinner, "#"));
        gSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                curvePanel.setColor ((int)rSpinner.getValue(), (int)gSpinner.getValue(), (int)bSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("G: "));
        fieldPanel.add(gSpinner);

        bSpinner = new JSpinner(new SpinnerNumberModel(b, 0, 255, 1));
        bSpinner.setEditor(new JSpinner.NumberEditor(bSpinner, "#"));
        bSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                curvePanel.setColor ((int)rSpinner.getValue(), (int)gSpinner.getValue(), (int)bSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("B: "));
        fieldPanel.add(bSpinner);

        zfSpinner = new JSpinner(new SpinnerNumberModel(logic.getCamera().getZf(), 0, 255, 1));
        zfSpinner.setEditor(new JSpinner.NumberEditor(zfSpinner, "#"));
        zfSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().setZf((int)zfSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("zf: "));
        fieldPanel.add(zfSpinner);

        zbSpinner = new JSpinner(new SpinnerNumberModel(logic.getCamera().getZb(), 0, 255, 1));
        zbSpinner.setEditor(new JSpinner.NumberEditor(zbSpinner, "#"));
        zbSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().setZb((int)zbSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("zb: "));
        fieldPanel.add(zbSpinner);

        swSpinner = new JSpinner(new SpinnerNumberModel(logic.getCamera().getSw(), 0, 255, 1));
        swSpinner.setEditor(new JSpinner.NumberEditor(swSpinner, "#"));
        swSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().setSw((int)swSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("sw: "));
        fieldPanel.add(swSpinner);

        shSpinner = new JSpinner(new SpinnerNumberModel(logic.getCamera().getSh(), 0, 255, 1));
        shSpinner.setEditor(new JSpinner.NumberEditor(shSpinner, "#"));
        shSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                logic.getCamera().setSh((int)shSpinner.getValue());
                //showCurrentCurve();
            }
        });
        fieldPanel.add(new JLabel("sh: "));
        fieldPanel.add(shSpinner);

        return fieldPanel;
    }

    protected void showCurrentCurve () {
        RotationBody r = (RotationBody)(logic.getWorld().getBody((int)bodySpinner.getValue()));
        if (r == null) {
            //curvePanel.loadCurve(null);
            curvePanel.loadPoints(null);
            //System.out.println("NULL");
        } else {
            //curvePanel.loadCurve(r.getCurve());
            curvePanel.loadPoints(r.getMarkers());
            rSpinner.setValue(r.getColor().getRed());
            gSpinner.setValue(r.getColor().getGreen());
            bSpinner.setValue(r.getColor().getBlue());
            //System.out.println("LOADED");
        }
        curvePanel.setNK ((int)nSpinner.getValue(), (int)kSpinner.getValue());
        curvePanel.setColor ((int)rSpinner.getValue(), (int)gSpinner.getValue(), (int)bSpinner.getValue());
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
                logic.setN((int)nSpinner.getValue());
                logic.setM((int)mSpinner.getValue());
                logic.setK((int)kSpinner.getValue());
                RotationBody r = (RotationBody)(logic.getWorld().getBody((int)bodySpinner.getValue()));
                if (r == null) {
                    r = new RotationBody(curvePanel.getMarkers(), (int)nSpinner.getValue(), (int)mSpinner.getValue(),
                            (int)kSpinner.getValue(),
                            0,0,0, 0,0,0);
                    r.setColor(new Color((int)rSpinner.getValue(), (int)gSpinner.getValue(), (int)bSpinner.getValue()));
                    logic.getWorld().addBody(r);
                } else {
                    //r.setCurve(curvePanel.getCurve());
                    r.setMarkers(curvePanel.getMarkers(), (int)nSpinner.getValue(), (int)mSpinner.getValue(),
                            (int)kSpinner.getValue());
                    r.setColor(new Color((int)rSpinner.getValue(), (int)gSpinner.getValue(), (int)bSpinner.getValue()));
                }

                //logic.notifyObservers();
                logic.render();
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
