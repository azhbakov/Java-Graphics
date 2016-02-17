import game_of_life.Game;
import view.HexWindow;

/**
 * Created by Martin on 10.02.2016.
 */
public class Main {
    public static void main (String[] args) {
        Game game = new Game(15, 15);
        //game.print();
        /*System.out.println("-------------------");
        game.liven_cell(5,4);
        //game.tick();
        game.print();
        System.out.println("-------------------");
        game.tick();
        game.print();*/
        HexWindow mainWindow = new HexWindow(game);
        game.addObserver(mainWindow);
    }
}
