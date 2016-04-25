package view;

import javafx.geometry.Point3D;
import model.Logic;
import model.Segment;
import model.UVLine;
import model.Vec3f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Martin on 18.04.2016.
 */
public class CameraScreen extends JPanel {
    ArrayList<UVLine> lines = new ArrayList<>();

    public CameraScreen (Logic logic) {
        addMouseMotionListener(new MouseMotionAdapter() {
            int lastX = -1, lastY = -1;
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (lastX != -1 ) {
                    if (SwingUtilities.isLeftMouseButton(e))
                        logic.leftMouseMoved((float) (e.getX() - lastX) / getWidth() * 10, (float) (e.getY() - lastY) / getHeight() * 10);
                    else
                        logic.rightMouseMoved((float) (e.getX() - lastX) / getWidth() * 10, (float) (e.getY() - lastY) / getHeight() * 10);
                }
                lastX = e.getX();
                lastY = e.getY();
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                logic.wheelRotated(e.getWheelRotation());
            }
        });
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        revalidate();
        Graphics2D g2 = (Graphics2D) g;
        for (UVLine l : lines) {
            l.drawLine(g2, getWidth(), getHeight());
        }
        //logic.updateCameraScreen();
    }

//    public void drawUVLine (float x1, float y1, float x2, float y2) {
//        int width = getWidth();
//        int height = getHeight();
//        if (((Graphics2D) getGraphics()).equals(g2))
//            g2 = (Graphics2D) getGraphics();
//        g2.drawLine((int)(x1 * width), (int)((1-y1) * height),
//                (int)(x2 * width), (int)((1-y2) * height));
////        System.out.println("From " + (int)(x1 * width) + " " + (int)(y1 * height) +
////                " to " + (int)(x2 * width) + " " + (int)(y2 * height));
//    }
//
//    public void drawUVLine (float x1, float y1, float x2, float y2, String s, Color c) {
//        int width = getWidth();
//        int height = getHeight();
//        g2.setColor(c);
//        g2.drawLine((int)(x1 * width), (int)((1-y1) * height),
//                (int)(x2 * width), (int)((1-y2) * height));
//        g2.drawString(s, (int)(x2 * width), (int)((1-y2) * height));
////        System.out.println("From " + (int)(x1 * width) + " " + (int)(y1 * height) +
////                " to " + (int)(x2 * width) + " " + (int)(y2 * height));
//    }

    public void setUVLines (ArrayList<UVLine> a) {
        lines = a;
        repaint();
    }
}
