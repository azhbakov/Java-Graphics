package ru.nsu.fit.g13201.azhbakov.game_of_life;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by azhbakov on 01.03.2016.
 */
public class FileInitializer {
    File file;
    Scanner scanner;

    public FileInitializer (File file) throws FileNotFoundException {
        this.file = file;
        scanner = new Scanner(file);
    }

    public GameSettings parseFile () {
        Scanner lineScanner = new Scanner(getLine());
        // get sizes
        int rows, cols;
        cols = Integer.parseInt(lineScanner.next());
        rows = Integer.parseInt(lineScanner.next());
        // get border width
        lineScanner = new Scanner(getLine());
        int borderWidth = Integer.parseInt(lineScanner.next());
        // get cell size
        lineScanner = new Scanner(getLine());
        int cellSize = Integer.parseInt(lineScanner.next());
        // get cell num
        lineScanner = new Scanner(getLine());
        int n = Integer.parseInt(lineScanner.next());
        // get cells
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            lineScanner = new Scanner(getLine());
            x[i] = Integer.parseInt(lineScanner.next());
            y[i] = Integer.parseInt(lineScanner.next());
        }

        // everything is ok
        return new GameSettings(rows,cols,borderWidth,cellSize, n, x,y);
    }

    private String getLine () { // ignore comments
        String line;
        while (true) {
            line = scanner.nextLine();
            int i = line.indexOf("//");
            if (i != -1) {
                line = line.substring(0, i);
            } else return line;
            if (!line.isEmpty()) return line;
        }
    }

}

