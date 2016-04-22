package model;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Segment {
    public Vec4f p1, p2;
    public Segment (Vec4f p1, Vec4f p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    public Segment (float x1, float y1, float z1, float w1, float x2, float y2, float z2, float w2) {
        p1 = new Vec4f(x1, y1, z1, w1);
        p2 = new Vec4f(x2, y2, z2, w2);
    }
}
