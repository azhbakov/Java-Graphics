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
    ArrayList<WiredBody> bodies = new ArrayList<>();
    Camera mainCamera;

    public World (Camera camera) {
        mainCamera = camera;
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

    public void addBody (WiredBody body) {
        bodies.add(body);
    }
    public void removeBody (WiredBody body) {
        bodies.remove(body);
    }

    public int getBodiesNum () {
        return bodies.size();
    }
    public WiredBody getBody (int index) {
        try {
            return bodies.get(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public ArrayList<WiredBody> getBodies () {
        return bodies;
    }

    public void translateCamera (Vec3f dir) {
        mainCamera.translate(dir);
    }

    public void clear () {
        int s = bodies.size();
        for (int i = 1; i < s; i++) {
            bodies.remove(1);
        }
    }
}
