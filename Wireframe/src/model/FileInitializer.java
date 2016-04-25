package model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
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
        int n, m, k;
        float a, b, c, d;
        n = Integer.parseInt(lineScanner.next());
        m = Integer.parseInt(lineScanner.next());
        k = Integer.parseInt(lineScanner.next());
        a = Float.parseFloat(lineScanner.next());
        b = Float.parseFloat(lineScanner.next());
        c = Float.parseFloat(lineScanner.next());
        d = Float.parseFloat(lineScanner.next());

        float zf, zb, sw, sh;
        lineScanner = new Scanner(getLine());
        zf = Float.parseFloat(lineScanner.next());
        zb = Float.parseFloat(lineScanner.next());
        sw = Float.parseFloat(lineScanner.next());
        sh = Float.parseFloat(lineScanner.next());

        float[][] rm = new float[4][4];
        for (int i = 0; i < 3; i++) {
            lineScanner = new Scanner(getLine());
            for (int j = 0; j < 3; j++) {
                rm[i][j] = Float.parseFloat(lineScanner.next());
            }
            rm[i][3] = 0;
        }
        for (int i = 0; i < 3; i++) rm[3][i] = 0;
        rm[3][3] = 1;

        Color backgroundCol;
        lineScanner = new Scanner(getLine());
        backgroundCol = new Color(Integer.parseInt(lineScanner.next()), Integer.parseInt(lineScanner.next()), Integer.parseInt(lineScanner.next()));

        int bnum;
        lineScanner = new Scanner(getLine());
        bnum = Integer.parseInt(lineScanner.next());
        RotationBody[] bodies = new RotationBody[bnum];
        for (int nb = 0; nb < bnum; nb++) {
            lineScanner = new Scanner(getLine());
            Color bcol = new Color(Integer.parseInt(lineScanner.next()), Integer.parseInt(lineScanner.next()), Integer.parseInt(lineScanner.next()));
            lineScanner = new Scanner(getLine());
            Vec4f pos = new Vec4f(Float.parseFloat(lineScanner.next()), Float.parseFloat(lineScanner.next()), Float.parseFloat(lineScanner.next()), 1);

            float[][] bm = new float[4][4];
            for (int i = 0; i < 3; i++) {
                lineScanner = new Scanner(getLine());
                for (int j = 0; j < 3; j++) {
                    bm[i][j] = Float.parseFloat(lineScanner.next());
                }
                bm[i][3] = 0;
            }
            for (int i = 0; i < 3; i++) bm[3][i] = 0;
            bm[0][3] = pos.x;
            bm[1][3] = pos.y;
            bm[2][3] = pos.z;
            bm[3][3] = 1;

            lineScanner = new Scanner(getLine());
            int pnum = Integer.parseInt(lineScanner.next());
            ArrayList<Point2D.Float> markers = new ArrayList<>();
            for (int np = 0; np < pnum; np++) {
                lineScanner = new Scanner(getLine());
                markers.add(new Point2D.Float(Float.parseFloat(lineScanner.next()), Float.parseFloat(lineScanner.next())));
            }
            RotationBody r = new RotationBody(markers, n, m, k, pos.x, pos.y, pos.z, 0,0,0);
            r.setColor(bcol);
            r.setM(bm);
            bodies[nb] = r;
        }

        // everything is ok
        return new GameSettings(n, m, k, a, b, c, d, zf, zb, sw, sh, rm, backgroundCol, bodies);
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

