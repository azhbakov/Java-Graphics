import model.Logic;
import view.AppWindow;
import view.BodySettingsWindow;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Martin on 01.04.2016.
 */
public class Main {

    public static void main(String[] args) {
        Logic logic = new Logic();
        AppWindow appWindow = new AppWindow(logic);
//        ArrayList<Point2D.Float> body = new ArrayList<>();
//        body.add(new Point2D.Float(1, 1));
//        BodySettingsWindow w = new BodySettingsWindow(body);
    }
}
