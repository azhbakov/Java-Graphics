package model;

import view.CameraScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by Martin on 01.04.2016.
 */
public class Logic extends Observable {
    World w;
    Camera c;

    public Logic () {
        try {
            //c = new Camera(null, 3,2,1, 3,0,2, 0,1,0);
            c = new Camera(null, 3,2,3, 0,0,0, 0,1,0);

            w = new World();

            //ArrayList<Segment> segments = new ArrayList<>();
            //segments.add(new Segment(0,0,0,1, 0,0,1,1));
            //w.addBody(new WiredBody(segments, 2, 0, 3, 0,90,0));
            ArrayList<Point2D.Float> markers = new ArrayList<>();
            markers.add(new Point2D.Float(0, 1));
            markers.add(new Point2D.Float(1, 1));
            markers.add(new Point2D.Float(2, 2));
            markers.add(new Point2D.Float(3, 2));
            //w.addBody(new RotationBody(markers, 8, 3,0,8, 0,0,0));
            w.addBody(new RotationBody(markers, 8, 0,0,0, 0,0,0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void render () {
        for (WiredBody b : w.getBodies()) {
            c.renderWire(b);
            c.renderXyz(b);
        }
    }

    public void updateCameraScreen () {
        render();
    }

    public void addCameraScreen (CameraScreen cameraScreen) {
        c.setCameraScreen(cameraScreen);
    }

    public World getWorld () { return w; }

    public void notifyObservers () {
        setChanged();
        super.notifyObservers();
    }

}
