package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by marting422 on 16.04.2016.
 */
public class BodySettingsWindow extends JFrame {
    Dimension size = new Dimension(800, 600);
    Dimension curvePanelSize = new Dimension(800, 500);
    CurvePanel curvePanel;
    ArrayList<Point2D.Float> body;
    //ArrayList<Point2D.Float> bodyOriginal;
    //ArrayList<Point2D.Float> bodyCopy;
    public int mouseMode = 0;

    public BodySettingsWindow (ArrayList<Point2D.Float> body) {
        this.body = body;
//        bodyOriginal = body;
//        if (body != null) {
//            bodyCopy = new ArrayList<Point2D.Float>();
//            for (Point2D.Float p : body) {
//                bodyCopy.add(p);
//            }
//        }

        curvePanel = new CurvePanel(curvePanelSize, this.body);
        add(curvePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        JButton applyButton = new JButton("Apply");
        applyButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //curvePanel.getBody();
                //if (bodyCopy != null) {
                //    bodyOriginal = bodyCopy;
                if (curvePanel.getCurve() != null) {
                    for (Point2D.Float p : curvePanel.getCurve()) {
                        System.out.println(p);
                    }
                }
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

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
