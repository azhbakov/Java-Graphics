package view;

import model.BezierCalculator;
import model.Curve;

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
    private ArrayList<Marker> markers;
    private float hw, hh; // halfwidth, halfheight in model space
    int n, k;
    Color c;

    public enum MouseMode {MOVE, ADD, REMOVE};
    private MouseMode currentMouseMode = MouseMode.MOVE;

    CurvePanel(Dimension d) {
        setSize(d);
        setPreferredSize(d);
        setLayout(null);
        markers = new ArrayList<>();
        scaleRanges(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (currentMouseMode == MouseMode.REMOVE) currentMouseMode = MouseMode.MOVE;
                if (currentMouseMode != MouseMode.ADD) return;
                //createMarker(e.getX(), e.getY());
                markers.add(new Marker(createLabel(), pixelToModel(e.getPoint())));
                repaint();
                currentMouseMode = MouseMode.MOVE;
            }
        });
    }

//    public void loadCurve (Curve c) {
//        markers.clear();
//        if (c == null) {
//            scaleRanges(null);
//            return;
//        }
//        scaleRanges(c.getPoints());
//        for (Point2D.Float p : c.getPoints()) {
//            float mx = p.x;
//            float my = p.y;
//            int x = (int)modelToPixel(new Point2D.Float(mx, my)).getX();
//            int y = (int)modelToPixel(new Point2D.Float(mx, my)).getY();
//            createMarker(x, y);
//        }
//    }
    public void loadPoints (ArrayList<Point2D.Float> gpoints) {
        for (Marker m : markers) {
            remove(m.l);
        }
        markers.clear();
        if (gpoints == null) {
            scaleRanges(null);
            return;
        }
        scaleRanges(gpoints);
        for (Point2D.Float p : gpoints) {
            markers.add(new Marker(createLabel(), p));
        }
        repaint();
    }

    public JLabel createLabel () {
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
                        //l.setLocation(mousePos.x - l.getWidth() / 2, mousePos.y - l.getHeight() / 2);
                        for (Marker m : markers) {
                            if (m.hasLabel(l)) {
                                m.setPos(pixelToModel(mousePos));
                                break;
                            }
                        }
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
                    for (Marker m : markers) {
                        if (m.hasLabel(l)) {
                            markers.remove(m);
                            break;
                        }
                    }
                    repaint();
                }
            }
        });
        add(l);
        l.setSize(l.getPreferredSize());
        return l;
        //l.setLocation(x-size/2, y-size/2);
        //l.setSize(size, size);
        //markers.add(l);
        //repaint();
        //System.out.println(markers.size());
    }

    public void paint (Graphics g) {
        super.paint(g);
        revalidate();
        Graphics2D g2 = (Graphics2D) g;
        drawAxis (g2);
        drawConnections(g2);
        g2.setColor(c);
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
        //int x0, y0, s;
        Point p0;
        if (!markers.isEmpty()) {
            p0 = markers.get(0).getPixelPos();
            //x0 = markers.get(0).getLocation().x;
            //y0 = markers.get(0).getLocation().y;
            //s = (int)(markers.get(0).getSize().getWidth()/2 );
        } else return;
        for (int i = 1; i < markers.size(); i++) {
            Point p = markers.get(i).getPixelPos();
            g2.drawLine(p0.x, p0.y, p.x, p.y);
            p0 = p;
//            Point p = markers.get(i).getLocation();
//            g2.drawLine(x0 + s, y0 + s, p.x + s, p.y + s);
//            x0 = p.x;
//            y0 = p.y;
        }
    }

    private void drawBSpline (Graphics2D g2) {
        if (markers.isEmpty()) return;
        //int s = (int)(markers.get(0).getSize().getWidth()/2 );
        if (markers.size() < 4) return;
        for (int i = 1; i < markers.size()-2; i++) {
            Point[] arg = {markers.get(i-1).getPixelPos(),
                    markers.get(i).getPixelPos(),
                    markers.get(i+1).getPixelPos(),
                    markers.get(i+2).getPixelPos()};
            Point[] p = BezierCalculator.getBSplinePoints(arg, n, k);

            int x0 = p[0].getLocation().x;
            int y0 = p[0].getLocation().y;
            for (int j = 1; j < p.length; j++) {
                g2.drawLine(x0, y0, p[j].x, p[j].y);
                x0 = p[j].x;
                y0 = p[j].y;
                //System.out.println(j + ": " + p[j].x + " " + p[j].y);
            }
        }
    }



    protected Point modelToPixel (Point2D.Float mp) {
        float nx, ny;
        float x = mp.x;
        float y = mp.y;
        nx = (hw + x)/(2*hw);
        ny = (hh + y)/(2*hh);
        nx = nx*getSize().width;
        ny = ny*getSize().height;
        return new Point(Math.round(nx), Math.round(ny));
    }

    protected Point2D.Float pixelToModel (Point p) {
        float mx, my;
        float w = getWidth();
        float h = getHeight();
        mx = 0 + hw * (p.x-w/2)/(w/2);
        my = 0 + hh*(p.y-h/2)/(h/2);
        return new Point2D.Float(mx, my);
    }

    private void scaleRanges (ArrayList<Point2D.Float> gpoints) {
        float ratio = getPreferredSize().width/getPreferredSize().height;
        //float ratio = getSize().width/getSize().height;
        if (gpoints == null || gpoints.isEmpty()) {
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

//    public Curve getCurve () {
//        int s = (int)(markers.get(0).getSize().getWidth()/2 );
//        //ArrayList<Point2D.Float> res = new ArrayList<>();
//        Curve res = new Curve();
//        if (markers.size() < 4) return null;
//
//        for (int i = 1; i < markers.size()-2; i++) {
//            Point[] arg = {markers.get(i - 1).getLocation(),
//                    markers.get(i).getLocation(),
//                    markers.get(i + 1).getLocation(),
//                    markers.get(i + 2).getLocation()};
//            Point[] p = BezierCalculator.getBSplinePoints(arg, n, k);
//            for (int j = 0; j < p.length; j++) {
//                p[j].x += s;
//                p[j].y += s;
//                Point2D.Float pm = pixelToModel(p[j]);
//                res.addPoint(pm.x, pm.y);
//            }
//        }
//        return res;
//    }

    public ArrayList<Point2D.Float> getMarkers () {
        //int s = (int)(markers.get(0).getSize().getWidth()/2 );
        ArrayList<Point2D.Float> res = new ArrayList<>();
        for (Marker m : markers) {
            res.add(m.getModelPos());
        }
        return res;
    }

    public void setMouseMode (MouseMode mode) {
        currentMouseMode = mode;
    }

    public void setNK (int n, int k) {
        this.n = n;
        this.k = k;
        repaint();
    }
    public void setColor (int r, int g, int b) {
        c = new Color(r, g, b);
        repaint();
    }

    class Marker {
        Point2D.Float modelPos;
        public JLabel l;

        public Marker (JLabel l, Point2D.Float mp) {
            this.l = l;
            setPos(mp);
        }
        public void setPos (Point2D.Float mp) {
            modelPos = mp;
            int size = l.getWidth();
            Point p = modelToPixel(mp);
            p.x -= size/2;
            p.y -= size/2;
            l.setLocation(p);
        }
        public Point2D.Float getModelPos () {
            return modelPos;
        }

        public Point getPixelPos () {
            Point p = modelToPixel(modelPos);
            int size = l.getWidth();
            //p.x -= size/2;
            //p.y += size/2;
            return p;
        }

        public boolean hasLabel (JLabel l) {
            return this.l.equals(l);
        }
    }
}
