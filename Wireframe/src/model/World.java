package model;

import javafx.geometry.Point3D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by marting422 on 21.04.2016.
 */
public class World {
    ArrayList<Body> bodies = new ArrayList<>();
    Camera mainCamera;

    public World ()

    public void addBody (Body body) {
        bodies.add(body);
    }

}
