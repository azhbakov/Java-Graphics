package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;

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
        setSize(d);
        setPreferredSize(d);
        ArrayList<Point2D.Float> gpoints = body;
        if (gpoints == null) {
            gpoints = new ArrayList<>();
        }
        scaleRanges(gpoints);
        setLayout(null);

        markers = new ArrayList<>();
        for (Point2D.Float p : gpoints) {
            float mx = p.x;
            float my = p.y;
            int x = (int)modelToPixel(new Point2D.Float(mx, my)).getX();
            int y = (int)modelToPixel(new Point2D.Float(mx, my)).getY();
            createMarker(x, y);
        }

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

    public void createMarker (int x, int y) {
        int size = 10;
        CurvePanel thisPanel = this;
        JLabel l = new JLabel(new ImageIcon("./Wireframe/icons/marker.png"));
        l.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (currentMouseMode == MouseMode.MOVE) {
                    Point mousePos = thisPanel.getMousePosition();
                    if (mousePos != null) {
                        l.setLocation(mousePos.x - l.getWidth() / 2, mousePos.y - l.getHeight() / 2);
                        repaint();
                    }
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
        l.setSize(l.getPreferredSize());
        l.setLocation(x-size/2, y-size/2);
        //l.setSize(size, size);
        markers.add(l);
        repaint();
        System.out.println(markers.size());
    }

    public void paint (Graphics g) {
        super.paint(g);
        revalidate();
        Graphics2D g2 = (Graphics2D) g;
        drawAxis (g2);
        drawConnections(g2);
        drawBSpline(g2);
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

    private void drawConnections (Graphics2D g2) {
        int x0, y0, s;
        if (!markers.isEmpty()) {
            x0 = markers.get(0).getLocation().x;
            y0 = markers.get(0).getLocation().y;
            s = (int)(markers.get(0).getSize().getWidth()/2 );
        } else return;
        for (int i = 1; i < markers.size(); i++) {
            Point p = markers.get(i).getLocation();
            g2.drawLine(x0 + s, y0 + s, p.x + s, p.y + s);
            x0 = p.x;
            y0 = p.y;
        }
    }

    private void drawBSpline (Graphics2D g2) {
        if (markers.isEmpty()) return;
        int s = (int)(markers.get(0).getSize().getWidth()/2 );
        if (markers.size() < 4) return;
        for (int i = 1; i < markers.size()-2; i++) {
            Point[] arg = {markers.get(i-1).getLocation(),
                    markers.get(i).getLocation(),
                    markers.get(i+1).getLocation(),
                    markers.get(i+2).getLocation()};
            Point[] p = getBSplinePoints(arg);

            int x0 = p[0].getLocation().x;
            int y0 = p[0].getLocation().y;
            for (int j = 1; j < p.length; j++) {
                g2.drawLine(x0 + s, y0 + s-1, p[j].x + s, p[j].y + s-1);
                x0 = p[j].x;
                y0 = p[j].y;
                //System.out.println(j + ": " + p[j].x + " " + p[j].y);
            }
        }
    }

    private Point[] getBSplinePoints (Point[] g) {
        float[][] M = {{-1, 3, -3, 1},
                {3, -6, 3, 0},
                {-3, 0, 3, 0},
                {1, 4, 1, 0}};
        float k = 1f/6;
        int n = 5;
        Point[] res = new Point[n];
        for (int np = 0; np < n; np++) {
            float px = 0;
            float py = 0;
            for (int i = 0; i < 4; i++) {
                float x = 0;
                float y = 0;
                for (int j = 0; j < 4; j++) {
                    x += M[i][j] * g[j].x;
                    y += M[i][j] * g[j].y;
                }
                //System.out.println(x);
                x *= Math.pow(1f/(n-1) * np, 3-i) * k;
                y *= Math.pow(1f/(n-1) * np, 3-i) * k;
                px += x;
                py += y;
            }
            res[np] = new Point((int)px, (int)py);
        }
        return res;
    }

    public Point2D.Float modelToPixel (Point2D.Float mp) {
        float nx, ny;
        float x = mp.x;
        float y = mp.y;
        nx = (hw + x)/(2*hw);
        ny = (hh + y)/(2*hh);
        nx = nx*getSize().width;
        ny = ny*getSize().height;
        return new Point2D.Float(Math.round(nx), Math.round(ny));
    }

    private Point2D.Float pixelToModel (Point p) {
        float mx, my;
        float w = getWidth();
        float h = getHeight();
        mx = 0 + hw * (p.x-w/2)/(w/2);
        my = 0 + hh*(p.y-h/2)/(h/2);
        return new Point2D.Float(mx, my);
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

    public ArrayList<Point2D.Float> getCurve () {
        int s = (int)(markers.get(0).getSize().getWidth()/2 );
        ArrayList<Point2D.Float> res = new ArrayList<>();
        if (markers.size() < 4) return null;

        for (int i = 1; i < markers.size()-2; i++) {
            Point[] arg = {markers.get(i - 1).getLocation(),
                    markers.get(i).getLocation(),
                    markers.get(i + 1).getLocation(),
                    markers.get(i + 2).getLocation()};
            Point[] p = getBSplinePoints(arg);
            for (int j = 0; j < p.length; j++) {
                p[j].x += s;
                p[j].y += s;
                res.add(pixelToModel(p[j]));
            }
        }
        return res;
    }

    public ArrayList<Point2D.Float> getBody () {
        int s = (int)(markers.get(0).getSize().getWidth()/2 );
        ArrayList<Point2D.Float> res = new ArrayList<>();
        for (JLabel l : markers) {
            Point p = new Point(l.getLocation().x + s, l.getLocation().y + s);
            res.add(pixelToModel(p));
        }
        return res;
    }

    public void setMouseMode (MouseMode mode) {
        currentMouseMode = mode;
    }
}
