package model;

/**
 * Created by Martin on 08.05.2016.
 */
public class BoxBody extends OpticalBody {

    float ha, hb, hc;
    Vec3f min, max;

    public BoxBody (Vec3f min, Vec3f max,
                    float kdr, float kdg, float kdb, float ksr, float ksg, float ksb, float power) {
        super ((max.x+min.x)/2, (max.y+min.y)/2, (max.z+min.z)/2,
                kdr, kdg, kdb, ksr, ksg, ksb, power);
        this.min = min;
        this.max = max;
        ha = (max.x - min.x)/2;
        hb = (max.y - min.y)/2;
        hc = (max.z - min.z)/2;
        segments.add(new Segment(new Vec4f(ha, hb, hc, 1), new Vec4f(-ha, hb, hc, 1)));
        segments.add(new Segment(new Vec4f(ha, hb, hc, 1), new Vec4f(ha, hb, -hc, 1)));
        segments.add(new Segment(new Vec4f(-ha, hb, -hc, 1), new Vec4f(-ha, hb, hc, 1)));
        segments.add(new Segment(new Vec4f(-ha, hb, -hc, 1), new Vec4f(ha, hb, -hc, 1)));

        segments.add(new Segment(new Vec4f(ha, -hb, hc, 1), new Vec4f(-ha, -hb, hc, 1)));
        segments.add(new Segment(new Vec4f(ha, -hb, hc, 1), new Vec4f(ha, -hb, -hc, 1)));
        segments.add(new Segment(new Vec4f(-ha, -hb, -hc, 1), new Vec4f(-ha, -hb, hc, 1)));
        segments.add(new Segment(new Vec4f(-ha, -hb, -hc, 1), new Vec4f(ha, -hb, -hc, 1)));

        segments.add(new Segment(new Vec4f(ha, hb, hc, 1), new Vec4f(ha, -hb, hc, 1)));
        segments.add(new Segment(new Vec4f(ha, hb, -hc, 1), new Vec4f(ha, -hb, -hc, 1)));
        segments.add(new Segment(new Vec4f(-ha, hb, hc, 1), new Vec4f(-ha, -hb, hc, 1)));
        segments.add(new Segment(new Vec4f(-ha, hb, -hc, 1), new Vec4f(-ha, -hb, -hc, 1)));
    }

    public SurfacePoint findIntersection (Vec3f from, Vec3f dir) {
        dir.normalize();
        float tnear = Float.NEGATIVE_INFINITY;
        float tfar = Float.POSITIVE_INFINITY;
        Vec3f n = new Vec3f();

        // X plane
        if (dir.x == 0) {
            if (from.x < min.x || from.x < max.x) return null;
        }
        float t1 = (min.x - from.x)/dir.x;
        float t2 = (max.x - from.x)/dir.x;
        if (t2 < t1) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        if (t1 > tnear) {
            tnear = t1;
            if (from.x < min.x) {
                n.x = -1;n.y = 0;n.z = 0;
            } else {
                n.x = 1;n.y = 0;n.z = 0;
            }
        }
        if (t2 < tfar) tfar = t2;
        if (tnear > tfar) return null;
        if (tfar < 0) return null;

        // Y plane
        if (dir.y == 0) {
            if (from.y < min.y || from.y < max.y) return null;
        }
        t1 = (min.y - from.y)/dir.y;
        t2 = (max.y - from.y)/dir.y;
        if (t2 < t1) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        if (t1 > tnear) {
            tnear = t1;
            if (from.y < min.y) {
                n.x = 0;n.y = -1;n.z = 0;
            } else {
                n.x = 0;n.y = 1;n.z = 0;
            }
        }
        if (t2 < tfar) tfar = t2;
        if (tnear > tfar) return null;
        if (tfar < 0) return null;

        // Z plane
        if (dir.z == 0) {
            if (from.z < min.z || from.z < max.z) return null;
        }
        t1 = (min.z - from.z)/dir.z;
        t2 = (max.z - from.z)/dir.z;
        if (t2 < t1) {
            float temp = t1;
            t1 = t2;
            t2 = temp;
        }
        if (t1 > tnear) {
            tnear = t1;
            if (from.z < min.z) {
                n.x = 0;n.y = 0;n.z = -1;
            } else {
                n.x = 0;n.y = 0;n.z = 1;
            }
        }
        if (t2 < tfar) tfar = t2;
        if (tnear > tfar) return null;
        if (tfar < 0) return null;

        Vec3f pos = Vec3f.mul(dir, tnear);
        pos = Vec3f.add(from, pos);
        return new SurfacePoint(pos, n, kdr, kdg, kdb, ksr, ksg, ksb, power);
    }
}
