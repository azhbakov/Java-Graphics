package view;

import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Martin on 18.04.2016.
 */
public class WorldWindow extends JPanel {
    //ArrayList<RotationBody> bodies = new ArrayList<>();

    public void paint (Graphics g) {
        super.paint(g);
        revalidate();
        Graphics2D g2 = (Graphics2D) g;

    }

}
