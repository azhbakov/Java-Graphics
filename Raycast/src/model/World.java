package model;

import javafx.geometry.Point3D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by marting422 on 21.04.2016.
 */
public class World {
    ArrayList<Body> bodies = new ArrayList<>();
    ArrayList<LightSource> lights = new ArrayList<>();

    public World (Camera camera) {
        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(new Segment(new Vec4f(10, 7, 10, 1), new Vec4f(-10, 7, 10, 1)));
        segments.add(new Segment(new Vec4f(10, 7, 10, 1), new Vec4f(10, 7, -10, 1)));
        segments.add(new Segment(new Vec4f(-10, 7, -10, 1), new Vec4f(-10, 7, 10, 1)));
        segments.add(new Segment(new Vec4f(-10, 7, -10, 1), new Vec4f(10, 7, -10, 1)));

        segments.add(new Segment(new Vec4f(10, -7, 10, 1), new Vec4f(-10, -7, 10, 1)));
        segments.add(new Segment(new Vec4f(10, -7, 10, 1), new Vec4f(10, -7, -10, 1)));
        segments.add(new Segment(new Vec4f(-10, -7, -10, 1), new Vec4f(-10, -7, 10, 1)));
        segments.add(new Segment(new Vec4f(-10, -7, -10, 1), new Vec4f(10, -7, -10, 1)));

        segments.add(new Segment(new Vec4f(10, 7, 10, 1), new Vec4f(10, -7, 10, 1)));
        segments.add(new Segment(new Vec4f(10, 7, -10, 1), new Vec4f(10, -7, -10, 1)));
        segments.add(new Segment(new Vec4f(-10, 7, 10, 1), new Vec4f(-10, -7, 10, 1)));
        segments.add(new Segment(new Vec4f(-10, 7, -10, 1), new Vec4f(-10, -7, -10, 1)));
        WiredBody cube = new WiredBody(segments, 0,0,0,0,0,0);
        cube.setColor(Color.darkGray);
        bodies.add(cube);
    }

    public void addBody (Body body) {
        bodies.add(body);
        if (body instanceof LightSource) {
            lights.add((LightSource) body);
        }
    }
    public void removeBody (Body body) {
        bodies.remove(body);
    }

    public int getBodiesNum () {
        return bodies.size();
    }
    public Body getBody (int index) {
        try {
            return bodies.get(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public ArrayList<Body> getBodies () {
        return bodies;
    }
    public ArrayList<LightSource> getLigths () {
        return lights;
    }

    public void clear () {
        int s = bodies.size();
        for (int i = 1; i < s; i++) {
            bodies.remove(1);
        }
        lights.clear();
    }
}
