package ru.nsu.fit.g13201.azhbakov.game_of_life;

/**
 * Created by Martin on 09.02.2016.
 */
public class Cell {
    private boolean isAlive = false;

    public void liven () {
        isAlive = true;
    }
    public void kill () {
        isAlive = false;
    }
    public void switchState () {
        isAlive = !isAlive;
    }
    public boolean getState () {
        return isAlive;
    }
}
