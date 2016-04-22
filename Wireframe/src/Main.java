import model.*;
import view.AppWindow;
import view.BodySettingsWindow;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Martin on 01.04.2016.
 */
public class Main {

    public static void main(String[] args) {
        //Logic logic = new Logic();
        //AppWindow appWindow = new AppWindow(logic);
//        ArrayList<Point2D.Float> body = new ArrayList<>();
//        body.add(new Point2D.Float(1, 1));
//        BodySettingsWindow w = new BodySettingsWindow(body);
        Camera c = new Camera(null, 3,0,0, 3,0,2, 0,1,0);

        World w = new World(c);

        ArrayList<Segment> segments = new ArrayList<>();
        segments.add(new Segment(0,0,0,1, 0,0,1,1));

        w.addBody(new WiredBody(segments, 2, 0, 3, 0,90,0));
        w.render();
    }
}
