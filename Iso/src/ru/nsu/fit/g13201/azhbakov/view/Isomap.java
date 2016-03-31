package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.model.Logic;

import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Martin on 01.04.2016.
 */
public class Isomap extends JPanel implements Observer {
    Logic logic;

    public Isomap (Logic logic, Dimension d) {
        setPreferredSize(d);
        this.logic = logic;
        logic.addObserver(this);
    }

    public void paint (Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        if (logic.showGrid()) {
            drawGrid(g2);
        }
    }

    private void drawGrid(Graphics2D g2) {
        int stepX = getWidth()/logic.getK();
        int stepY = getHeight()/logic.getM();
        for (int i = 0; i < logic.getK(); i++) {
            g2.drawLine(i*stepX, 0, i*stepX, getHeight());
        }
        for (int i = 0; i < logic.getM(); i++) {
            g2.drawLine(0, i*stepY, getWidth(), i*stepY);
        }
    }

    public void update (Observable observable, Object object) {
        repaint();
    }
}
