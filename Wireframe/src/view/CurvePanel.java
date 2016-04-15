package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by marting422 on 15.04.2016.
 */
public class CurvePanel extends JPanel {
    private float DEFAULTHW = 5f;

    //private ArrayList<Point2D.Float> gpoints;
    private ArrayList<JLabel> markers;
    private float hw, hh; // halfwidth, halfheight in model space

    public enum MouseMode {MOVE, ADD, REMOVE};
    private MouseMode currentMouseMode = MouseMode.MOVE;

    CurvePanel(Dimension d, ArrayList<Point2D.Float> body) {
        setPreferredSize(d);
        ArrayList<Point2D.Float> gpoints = body;
        if (gpoints == null)
            gpoints = new ArrayList<>();
        scaleRanges(gpoints);
        setLayout(null);

        markers = new ArrayList<>();
        for (Point2D.Float p : gpoints) {
            float mx = p.x;
            float my = p.y;
            int x = (int)modelToPixel(new Point2D.Float(mx, 0)).getX();
            int y = (int)modelToPixel(new Point2D.Float(0, my)).getY();
            createMarker(x, y);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (currentMouseMode == MouseMode.REMOVE) currentMouseMode = MouseMode.MOVE;
                    if (currentMouseMode != MouseMode.ADD) return;
                    createMarker(e.getX(), e.getY());
                    currentMouseMode = MouseMode.MOVE;
                }
            });
        }
    }

    public void createMarker (int x, int y) {
        int size = 10;
        CurvePanel thisPanel = this;
        JLabel l = new JLabel("o");
        l.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (currentMouseMode == MouseMode.MOVE) {
                    Point mousePos = thisPanel.getMousePosition();
                    if (mousePos != null)
                        l.setLocation(mousePos.x - l.getWidth() / 2, mousePos.y - l.getHeight() / 2);
                }
            }
        });
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (currentMouseMode == MouseMode.REMOVE) {
                    thisPanel.remove(l);
                    markers.remove(l);
                    repaint();
                }
            }
        });
        add(l);
        l.setLocation(x-size/2, y-size/2);
        l.setSize(size, size);
        markers.add(l);
    }

    public void paint (Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        drawAxis (g2);
    }

    private void drawAxis (Graphics2D g2) {
        int w = getWidth();
        int h = getHeight();
        g2.drawLine(0, h/2, w, h/2);
        for (int i = (int)-hw; i <= hw; i++) {
            int x = (int)modelToPixel(new Point2D.Float(i, 0)).getX();
            g2.drawLine (x, h/2 - 2, x, h/2 + 2);
            if (i != 0)
                g2.drawString(Integer.toString(i), x - 3, h/2 + 15);
        }
        g2.drawLine(w/2, 0, w/2, h);
        for (int i = (int)-hh; i <= hh; i++) {
            int y = (int)modelToPixel(new Point2D.Float(0, i)).getY();
            g2.drawLine (w/2 - 2, y, w/2 + 2, y);
            if (i != 0)
                g2.drawString(Integer.toString(i), w/2 + 5, y + 5);
        }
    }

    public Point2D.Float modelToPixel (Point2D.Float mp) {
        float nx, ny;
        float x = mp.x;
        float y = mp.y;;
        nx = (hw + x)/(2*hw);
        ny = (hh + y)/(2*hh);
        nx = nx*getPreferredSize().width;
        ny = ny*getPreferredSize().height;
        return new Point2D.Float(nx, ny);
    }

    private void scaleRanges (ArrayList<Point2D.Float> gpoints) {
        float ratio = getPreferredSize().width/getPreferredSize().height;
        if (gpoints.isEmpty()) {
            hw = DEFAULTHW;
            hh = hw/ratio;
            return;
        }

        float mx = 0, my = 0;
        for (Point2D.Float p : gpoints) {
            float ax = Math.abs(p.x);
            float ay = Math.abs(p.y);
            if (ax > mx) {
                mx = ax;
            }
            if (ay > my) {
                my = ay;
            }
        }
        if (mx > my) {
            hw = mx + mx * 0.2f; // offset
            hh = hw/ratio;
        } else {
            hh = my + my * 0.2f; // offset
            hw = hh*ratio;
        }
    }

    public ArrayList<Point2D.Float> getBody () {
        return null;
    }

    public void setMouseMode (MouseMode mode) {
        currentMouseMode = mode;
    }
}
