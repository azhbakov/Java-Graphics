package model;

import java.util.ArrayList;

/**
 * Created by marting422 on 22.04.2016.
 */
public class WiredBody extends Body {
    ArrayList<Segment> segments;
    public WiredBody (float x, float y, float z, float rx, float ry, float rz) {
        super(x, y, z, 1, rx, ry, rz, 1);
        segments = new ArrayList<>();
    }
    public WiredBody (ArrayList<Segment> segments, float x, float y, float z, float rx, float ry, float rz) {
        super(x, y, z, 1, rx, ry, rz, 1);
        this.segments = segments;
    }

    public ArrayList<Segment> getSegments () {
        return segments;
    }
}
