package model;

import java.util.ArrayList;

/**
 * Created by marting422 on 22.04.2016.
 */
public class WiredBody extends Body {
    ArrayList<Segment> segments;
    public WiredBody (float x, float y, float z, float zx, float yz, float xy) {
        super(x, y, z, 1, zx, yz, xy, 1);
        segments = new ArrayList<>();
    }

    public ArrayList<Segment> getSegments () {
        return segments;
    }
}
