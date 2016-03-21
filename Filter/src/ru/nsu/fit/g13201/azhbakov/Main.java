package ru.nsu.fit.g13201.azhbakov;

import ru.nsu.fit.g13201.azhbakov.model.Logic;
import ru.nsu.fit.g13201.azhbakov.view.AppWindow;

/**
 * Created by marting422 on 09.03.2016.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Logic logic = new Logic();
        AppWindow appWindow = new AppWindow(logic);
        /*JFrame frame = new JFrame();
        JLabel label = new JLabel(new ImageIcon(ImageIO.read(new File("Filter/Data/azaza.bmp"))));
        frame.add(label);
        frame.pack();
        frame.setVisible(true);*/
    }
}
