package model;

/**
 * Created by Martin on 19.05.2016.
 */
public class TriangleBody extends OpticalBody {
    Vec3f p1, p2, p3;

    public TriangleBody (Vec3f p1, Vec3f p2, Vec3f p3,
                       float kdr, float kdg, float kdb, float ksr, float ksg, float ksb, float power) {
        super((p1.x+p2.x+p3.x)/3, (p1.y+p2.y+p3.y)/3, (p1.z+p2.z+p3.z)/3,
                kdr, kdg, kdb, ksr, ksg, ksb, power);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        Vec4f lp1 = Vec4f.sub(new Vec4f(p1, 1), transform.position);
        Vec4f lp2 = Vec4f.sub(new Vec4f(p2, 1), transform.position);
        Vec4f lp3 = Vec4f.sub(new Vec4f(p3, 1), transform.position);
        segments.add(new Segment(lp1, lp2));
        segments.add(new Segment(lp2, lp3));
        segments.add(new Segment(lp1, lp3));
        Vec4f planeNormal = new Vec4f(Vec3f.cross(Vec3f.sub(p2, p1), Vec3f.sub(p3, p2)), 1).normalize();
        segments.add(new Segment(new Vec4f(0,0,0,1), planeNormal));
    }

    public SurfacePoint findIntersection (Vec3f from, Vec3f dir) {
        dir = dir.normalize();
        Vec3f planeNormal = Vec3f.cross(Vec3f.sub(p3, p2), Vec3f.sub(p2, p1)).normalize();
        if (Math.abs(Vec3f.dot(planeNormal, dir)) < 0.0001f || Vec3f.dot(planeNormal, dir) > 0) return null;
        float t = Vec3f.dot(Vec3f.sub(p1, from), planeNormal)/Vec3f.dot(planeNormal, dir);
        if (t < 0) {
            return null;
        }
        Vec3f pi = Vec3f.add(from, Vec3f.mul(dir, t)); // planeIntersection
        Vec3f v1 = Vec3f.cross(Vec3f.sub(p2, p1), Vec3f.sub(p2, pi));
        Vec3f v2 = Vec3f.cross(Vec3f.sub(p3, p2), Vec3f.sub(p3, pi));
        Vec3f v3 = Vec3f.cross(Vec3f.sub(p1, p3), Vec3f.sub(p1, pi));
        if (Math.signum(Vec3f.dot(v1, v2)) == Math.signum(Vec3f.dot(v1, v3)) &&
                Math.signum(Vec3f.dot(v1, v2)) == Math.signum(Vec3f.dot(v2, v3))) {
            return new SurfacePoint(pi, planeNormal, kdr, kdg, kdb, ksr, ksg, ksb, power);
        } else
            return null;
    }
}
