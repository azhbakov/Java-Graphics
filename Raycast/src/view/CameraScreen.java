package view;

import javafx.geometry.Point3D;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by Martin on 18.04.2016.
 */
public class CameraScreen extends JPanel {
    ArrayList<UVLine> lines = new ArrayList<>();
    ArrayList<ScreenPoint> points = new ArrayList<>();
    Color backgroundCol = Color.gray;

    public CameraScreen (Logic logic) {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            int lastX = -1, lastY = -1;
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (lastX != -1 ) {
                    if (SwingUtilities.isLeftMouseButton(e))
                        logic.leftMouseMoved((float) (e.getX() - lastX) / getWidth() * 10, (float) (e.getY() - lastY) / getHeight() * 10);
                    //else
                    //logic.rightMouseMoved((float) (e.getX() - lastX) / getWidth() * 10, (float) (e.getY() - lastY) / getHeight() * 10);
                }
                lastX = e.getX();
                lastY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                lastX = -1;
                lastY = -1;
            }
        };
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    logic.ctrlWheelRotated(e.getWheelRotation());
                    //System.out.println("CTR");
                } else {
                    logic.wheelRotated(e.getWheelRotation());
                }
            }
        });
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        revalidate();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(backgroundCol);
        g2.fillRect(0,0,getWidth(),getHeight());
        //g2.
        if (points != null) {
            for (ScreenPoint p : points) {
                p.drawPoint(g2, getWidth(), getHeight());
            }
        }
        if (lines != null) {
            for (UVLine l : lines) {
                l.drawLine(g2, getWidth(), getHeight());
            }
        }
        //logic.updateCameraScreen();
    }

    public void setUVLines (ArrayList<UVLine> a) {
        lines = a;
        repaint();
    }

    public void setScreenPoints (ArrayList<ScreenPoint> a) {
        points = a;
        repaint();
    }

    public void setBackgroundCol (Color c) {
        backgroundCol = c;
    }
    public Color getBackgroundCol () {
        return backgroundCol;
    }
}
