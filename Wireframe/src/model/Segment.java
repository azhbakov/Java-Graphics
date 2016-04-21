package model;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Segment {
    public Vec3f p1, p2;
    public Segment (Vec3f p1, Vec3f p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    public Segment (float x1, float y1, float z1, float x2, float y2, float z2) {
        p1 = new Vec3f(x1, y1, z1);
        p2 = new Vec3f(x2, y2, z2);
    }
}
