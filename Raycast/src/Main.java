import model.*;
import view.AppWindow;

/**
 * Created by Martin on 01.04.2016.
 */
public class Main {

    public static void main(String[] args) {
        Logic logic = new Logic();
        AppWindow appWindow = new AppWindow(logic);
    }
}
