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
    Color backgroundCol = Color.gray;

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
        g2.setColor(backgroundCol);
        g2.fillRect(0,0,getWidth(),getHeight());
        //g2.
        for (UVLine l : lines) {
            l.drawLine(g2, getWidth(), getHeight());
        }
        //logic.updateCameraScreen();
    }

    public void setUVLines (ArrayList<UVLine> a) {
        lines = a;
        repaint();
    }

    public void setBackgroundCol (Color c) {
        backgroundCol = c;
    }
    public Color getBackgroundCol () {
        return backgroundCol;
    }
}
