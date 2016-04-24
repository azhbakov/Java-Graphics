package model;

import javafx.geometry.Point3D;

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
        bodies.add(new WiredBody(0,0,0,0,0,0));
    }

    public void addBody (WiredBody body) {
        bodies.add(body);
//        setChanged();
//        notifyObservers();
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
//        setChanged();
//        notifyObservers();
    }

    public void translate (WiredBody b, Vec3f dir) {

    }
}
