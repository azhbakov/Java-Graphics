package model;

import javafx.geometry.Point3D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by marting422 on 21.04.2016.
 */
class RotationBody extends WiredBody {

    public RotationBody (Curve curve, int turns, float x, float y, float z, float zx, float yz, float xy) {
        super(x, y, z, zx, yz, xy);

        ArrayList<Point2D.Float> points = curve.getPoints();
        for (int i = 0; i < points.size(); i++) { // for each curve point do rotation and add segment current to up and current to right

            Point2D.Float pc = points.get(i); // current 2d curve point
            Point2D.Float pu; // next (up) 2d curve point
            if (i < points.size() - 1)
                pu = points.get(i+1);
            else
                pu = null;

            Vec3f c = new Vec3f(0, pc.y, pc.x); // segment start
            Vec3f u; // up
            float rad = c.z;
            float radUp;
            if (pu != null) {
                u = new Vec3f(0, pu.y, pu.x);
                radUp = u.z;
            } else {
                u = null;
                radUp = 0;
            }

            float angle = 0;
            float angleStep = (float)(2*Math.PI/turns);
            for (int j = 0; j < turns; j++) {
                // segment up
                if (u != null) {
                    segments.add(new Segment(c, u));
                    u = new Vec3f(radUp*(float)Math.sin(angle), radUp*(float)Math.cos(angle), u.z);
                }
                Vec3f r = new Vec3f(rad*(float)Math.sin(angle), rad*(float)Math.cos(angle), c.z);
                segments.add(new Segment(c, r));
                c = r;
                angle += angleStep;
            }
        }
    }
}