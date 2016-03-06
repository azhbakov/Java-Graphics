package ru.nsu.fit.g13201.azhbakov.game_of_life;

/**
 * Created by azhbakov on 01.03.2016.
 */
public class GameSettings {
    public int rows, cols, borderWidth, cellSize, n;
    public int[] x;
    public int[] y;

    public GameSettings(int rows, int cols, int borderWidth, int cellSize, int n, int[] x, int[] y) {
        this.rows = rows;
        this.cols = cols;
        this.borderWidth = borderWidth;
        this.cellSize = cellSize;
        this.n = n;
        this.x = x;
        this.y = y;
    }
}
