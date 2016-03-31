package ru.nsu.fit.g13201.azhbakov;

import ru.nsu.fit.g13201.azhbakov.model.Logic;
import ru.nsu.fit.g13201.azhbakov.view.AppWindow;

/**
 * Created by Martin on 01.04.2016.
 */
public class Main {

    public static void main(String[] args) {
        Logic logic = new Logic();
        AppWindow appWindow = new AppWindow(logic);
    }
}
