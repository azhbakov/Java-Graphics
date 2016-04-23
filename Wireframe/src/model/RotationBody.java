package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by marting422 on 21.04.2016.
 */
public class RotationBody extends WiredBody {
    ArrayList<Point2D.Float> markers;
    int turns;
    Curve curve;

//    public RotationBody (ArrayList<Point2D.Float> markers, Curve curve, int turns, float x, float y, float z, float zx, float yz, float xy) {
//        super(x, y, z, zx, yz, xy);
//        this.markers = markers;
//        this.curve = curve;
//        this.turns = turns;
//        recalculate();
//    }

    public RotationBody (ArrayList<Point2D.Float> markers, int turns, float x, float y, float z, float zx, float yz, float xy) {
        super(x, y, z, zx, yz, xy);
        this.markers = markers;
        this.turns = turns;
        recalculate();
    }

//    public void setCurve (Curve curve) {
//        this.curve = curve;
//        recalculate ();
//    }

    public void setTurns (int turns) {
        this.turns = turns;
        recalculate();
    }

    private void recalculate () {
        segments.clear();
        Point2D.Float[] ar = new Point2D.Float[markers.size()];
        System.out.println("MARKERS SIZE: " + markers.size());
        curve = BezierCalculator.getBSplineCurve(markers.toArray(ar));
        if (curve == null) return;
        ArrayList<Point2D.Float> points = curve.getPoints();
        for (int i = 0; i < points.size(); i++) { // for each curve point do rotation and add segment current to up and current to right

            Point2D.Float pc = points.get(i); // current 2d curve point
            Point2D.Float pu; // next (up) 2d curve point
            if (i < points.size() - 1)
                pu = points.get(i+1);
            else
                pu = null;

            Vec4f c = new Vec4f(pc.y, pc.x, 0, 1); // segment start
            Vec4f u; // up
            float rad = c.x;
            float radUp;
            if (pu != null) {
                u = new Vec4f(pu.y, pu.x, 0, 1);
                radUp = u.x;
            } else {
                u = null;
                radUp = 0;
            }

            float angleStep = (float)(2*Math.PI/turns);
            float angle = angleStep;
            for (int j = 0; j < turns; j++) {
                // segment up
                if (u != null) {
                    segments.add(new Segment(c, u));
                    //System.out.println("Added up:");
                    //c.print();
                    //u.print();
                    u = new Vec4f(radUp*(float)Math.cos(angle), u.y, radUp*(float)Math.sin(angle), 1);
                }
                Vec4f r = new Vec4f(rad*(float)Math.cos(angle), c.y, rad*(float)Math.sin(angle), 1);
                segments.add(new Segment(c, r));
                //System.out.println("Added right:");
                //c.print();
                //r.print();
                c = r;
                angle += angleStep;
            }
        }
    }

//    public Curve getCurve () {
//        return curve;
//    }

    public void setMarkers (ArrayList<Point2D.Float> markers) {
        this.markers = markers;
        recalculate();
    }

    public ArrayList<Point2D.Float> getMarkers () {
        return markers;
    }
}