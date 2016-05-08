package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by marting422 on 22.04.2016.
 */
public class WiredBody extends Body {
    ArrayList<Segment> segments = new ArrayList<>();;
    Color c = Color.black;
    public WiredBody (float x, float y, float z, float rx, float ry, float rz) {
        super(x, y, z, 1, rx, ry, rz, 1);
    }
    public WiredBody (ArrayList<Segment> segments, float x, float y, float z, float rx, float ry, float rz) {
        super(x, y, z, 1, rx, ry, rz, 1);
        this.segments = segments;
    }

    public void setColor (Color c) {
        this.c = c;
    }
    public Color getColor () {
        return c;
    }

    public ArrayList<Segment> getSegments () {
        return segments;
    }
}
