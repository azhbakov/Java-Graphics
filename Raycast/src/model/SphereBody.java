package model;

/**
 * Created by Martin on 19.05.2016.
 */
public class SphereBody extends OpticalBody {
    public Vec3f center;
    public float radius;

    public SphereBody (Vec3f center, float radius,
                   float kdr, float kdg, float kdb, float ksr, float ksg, float ksb, float power) {
        super(center.x, center.y, center.z, kdr, kdg, kdb, ksr, ksg, ksb, power);
        this.center = center;
        this.radius = radius;
        // up
        segments.add(new Segment(-radius, 0, 0, 0, radius, 0));
        segments.add(new Segment(radius, 0, 0, 0, radius, 0));
        segments.add(new Segment(0, 0, -radius, 0, radius, 0));
        segments.add(new Segment(0, 0, radius, 0, radius, 0));
        // down
        segments.add(new Segment(-radius, 0, 0, 0, -radius, 0));
        segments.add(new Segment(radius, 0, 0, 0, -radius, 0));
        segments.add(new Segment(0, 0, -radius, 0, -radius, 0));
        segments.add(new Segment(0, 0, radius, 0, -radius, 0));
        // sides
        segments.add(new Segment(-radius, 0, 0, 0, 0, radius));
        segments.add(new Segment(-radius, 0, 0, 0, 0, -radius));
        segments.add(new Segment(radius, 0, 0, 0, 0, radius));
        segments.add(new Segment(radius, 0, 0, 0, 0, -radius));
    }

    public SurfacePoint findIntersection (Vec3f from, Vec3f dir) {
        //boolean fromInside = false;
        //Vec3f toCenter = Vec3f.sub(center, from);
        //if (toCenter.length() < radius) fromInside = true;
        dir = dir.normalize();
        Vec3f k = Vec3f.sub(from, center);
        float b = Vec3f.dot(k, dir);
        float c = Vec3f.dot(k, k) - radius * radius;
        float d = b * b - c;

        if (d >= 0) {
            float sqrtfd = (float) Math.sqrt(d);
            // t, a == 1
            float t1 = -b + sqrtfd;
            float t2 = -b - sqrtfd;

            float min_t = Math.min(t1, t2);
            float max_t = Math.max(t1, t2);

            float t = (min_t >= 0) ? min_t : max_t;
            if (t < 0) return null;
            Vec3f surfacePoint = Vec3f.add(from, Vec3f.mul(dir, t));
            Vec3f normal = Vec3f.sub(surfacePoint, center).normalize();
            return new SurfacePoint(surfacePoint, normal, kdr, kdg, kdb, ksr, ksg, ksb, power);
        }
        return null;
    }
}
