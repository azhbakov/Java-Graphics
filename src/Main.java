import game_of_life.Game;
import view.GameWindow;
import view.HexWindow;
import view.MainWindow;

import java.awt.event.KeyEvent;

/**
 * Created by Martin on 10.02.2016.
 */
public class Main {
    public static void main (String[] args) throws Exception {
        Game game = new Game(15, 15);
        //game.print();
        /*System.out.println("-------------------");
        game.liven_cell(5,4);
        //game.tick();
        game.print();
        System.out.println("-------------------");
        game.tick();
        game.print();*/
        HexWindow hexWindow = new HexWindow(game);
        GameWindow gameWindow = new GameWindow (game);
        game.addObserver(gameWindow);
        game.addObserver(hexWindow);
    }

}
