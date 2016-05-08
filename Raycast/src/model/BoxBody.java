package model;

/**
 * Created by Martin on 08.05.2016.
 */
public class BoxBody extends WiredBody {

    float ha, hb, hc;

    public BoxBody (Vec3f min, Vec3f max) {
        super ((max.x+min.x)/2, (max.y+min.y)/2, (max.z+min.z)/2, 0,0,0);
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
}
