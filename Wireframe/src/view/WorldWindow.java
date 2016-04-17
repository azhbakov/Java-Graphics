package view;

import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Martin on 18.04.2016.
 */
public class WorldWindow extends JPanel {
    ArrayList<RotationBody> bodies = new ArrayList<>();

    public void paint (Graphics g) {
        super.paint(g);
        revalidate();
        Graphics2D g2 = (Graphics2D) g;

    }

    public void addBody (ArrayList<Point2D.Float> body) {
        bodies.add(new RotationBody(body));
    }

    class RotationBody {
        ArrayList<Point3D> points;
        Point2D.Float worldPosition;

        public RotationBody (ArrayList<Point2D.Float> curve) {
            for (Point2D.Float p2 : curve) {
                points.add(new Point3D(0, p2.y, p2.x));
            }
        }
    }
}
