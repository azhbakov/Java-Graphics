package ru.nsu.fit.g13201.azhbakov;

import ru.nsu.fit.g13201.azhbakov.game_of_life.Game;
import ru.nsu.fit.g13201.azhbakov.view.GameWindow;

/**
 * Created by Martin on 10.02.2016.
 */
public class Main {
    public static void main (String[] args) throws Exception {
        Game game = new Game(25, 25);
        GameWindow gameWindow = new GameWindow (game);
    }

}
