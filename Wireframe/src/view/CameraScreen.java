package view;

import javafx.geometry.Point3D;
import model.Logic;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Martin on 18.04.2016.
 */
public class CameraScreen extends JPanel implements Observer {
    //ArrayList<RotationBody> bodies = new ArrayList<>();
    Logic logic;
    Graphics2D g2;

    public CameraScreen (Logic logic) {
        logic.addObserver(this);
        this.logic = logic;
    }

    public void paint (Graphics g) {
        super.paint(g);
        revalidate();
        g2 = (Graphics2D) g;
        logic.updateCameraScreen();
    }

    public void drawUVLine (float x1, float y1, float x2, float y2) {
        int width = getWidth();
        int height = getHeight();
        g2.drawLine((int)(x1 * width), (int)((1-y1) * height),
                (int)(x2 * width), (int)((1-y2) * height));
//        System.out.println("From " + (int)(x1 * width) + " " + (int)(y1 * height) +
//                " to " + (int)(x2 * width) + " " + (int)(y2 * height));
    }

    public void drawUVLine (float x1, float y1, float x2, float y2, String s, Color c) {
        int width = getWidth();
        int height = getHeight();
        g2.setColor(c);
        g2.drawLine((int)(x1 * width), (int)((1-y1) * height),
                (int)(x2 * width), (int)((1-y2) * height));
        g2.drawString(s, (int)(x2 * width), (int)((1-y2) * height));
//        System.out.println("From " + (int)(x1 * width) + " " + (int)(y1 * height) +
//                " to " + (int)(x2 * width) + " " + (int)(y2 * height));
    }

    public void update (Observable observable, Object object) {
        repaint();
    }

}
