package model;

import view.CameraScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;

/**
 * Created by Martin on 01.04.2016.
 */
public class Logic extends Observable {
    World w;
    Camera c;
    CameraScreen cs;
    int n = 6, m = 6, k = 4;

    public Logic () {
        try {
            //c = new Camera(null, 3,2,1, 3,0,2, 0,1,0);
            cs = new CameraScreen();
            c = new Camera(3,2,3, 0,0,0, 0,1,0);
            w = new World(c);

            //ArrayList<Segment> segments = new ArrayList<>();
            //segments.add(new Segment(0,0,0,1, 0,0,1,1));
            //w.addBody(new WiredBody(segments, 2, 0, 3, 0,90,0));
            ArrayList<Point2D.Float> markers = new ArrayList<>();
            markers.add(new Point2D.Float(0, 1));
            markers.add(new Point2D.Float(1, 1));
            markers.add(new Point2D.Float(2, 2));
            markers.add(new Point2D.Float(3, 2));
            //w.addBody(new RotationBody(markers, 8, 3,0,8, 0,0,0));
            w.addBody(new RotationBody(markers, n,m,k, 0,0,0, 0,0,0));
            w.addBody(new RotationBody(markers, n,m,k, 4,0,0, 0,0,0));

            render();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void render () {
        cs.setUVLines(c.calcWires(w.getBodies()));
    }

    public World getWorld () { return w; }

    public void notifyObservers () {
        setChanged();
        super.notifyObservers();
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        notifyObservers();
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
        notifyObservers();
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
        notifyObservers();
    }

    public void keyPressed (char k) {
        switch (k) {
            case 'w':
                w.translateCamera(new Vec3f(0,0,1));
                break;
            case 's':
                w.translateCamera(new Vec3f(0,0,-1));
                break;
            case 'a':
                w.translateCamera(new Vec3f(-1,0,0));
                break;
            case 'd':
                w.translateCamera(new Vec3f(1,0,0));
                break;
            case 'q':
                w.translateCamera(new Vec3f(0,1,0));
                break;
            case 'z':
                w.translateCamera(new Vec3f(0,-1,0));
                break;
        }
        render();
    }

    public CameraScreen getCameraScreen () {
        return cs;
    }
    public Camera getCamera () {
        return c;
    }
}
