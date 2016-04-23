package model;

import javafx.geometry.Point3D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by marting422 on 21.04.2016.
 */
public class World {
    ArrayList<WiredBody> bodies = new ArrayList<>();
    //Camera mainCamera;

//    public World (Camera camera) {
//        mainCamera = camera;
//    }

    public void addBody (WiredBody body) {
        bodies.add(body);
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
}
