package model;


import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Curve {
    ArrayList<Point2D.Float> points;

    public Curve () {
        points = new ArrayList<>();
    }
    public Curve (ArrayList<Point2D.Float> points) {
        this.points = points;
    }
    public void addPoint (Point2D.Float point) {
        points.add(point);
    }
    public void addPoint (float x, float y) {
        points.add(new Point2D.Float(x, y));
    }
    public void removePoint (Point2D.Float point) {
        points.remove(point);
    }
    public ArrayList<Point2D.Float> getPoints () {
        return points;
    }
}
