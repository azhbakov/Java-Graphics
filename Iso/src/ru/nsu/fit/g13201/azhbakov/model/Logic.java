package ru.nsu.fit.g13201.azhbakov.model;

import java.awt.*;
import java.io.File;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by Martin on 01.04.2016.
 */
public class Logic extends Observable {
    final String defaultSettings = "Iso/Data/settings.txt";

    Func2 function = new SumFunc();
    float a = 0, b = 0, c = 1, d = 1;
    int k, m;
    int n;
    Color[] legendColors;
    Color isoColor;
    float[][] z;

    boolean showGrid = true;

    public Logic () {
        init (null);
    }

    public void init (File f) {
        if (f == null)
            f = new File(defaultSettings);
        try {
            readSettings(f);
            z = new float[k][m];

            float stepX = (b-a)/k;
            float stepY = (d-c)/m;
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < m; j++) {
                    z[i][j] = function.res(a + stepX*i, c + stepY*j);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void readSettings (File f) throws Exception {
        int k, m, n;
        Color[] legendColors;
        Color isoColor;
        Scanner reader = new Scanner(f);

        k = reader.nextInt();
        m = reader.nextInt();
        n = reader.nextInt();
        legendColors = new Color[n];
        for (int i = 0; i < n; i++) {
            legendColors[i] = new Color(reader.nextInt(), reader.nextInt(), reader.nextInt());
        }
        isoColor = new Color(reader.nextInt(), reader.nextInt(), reader.nextInt());
        this.k = k;
        this.m = m;
        this.n = n;
        this.legendColors = legendColors;
        this.isoColor = isoColor;
    }


    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Color[] getLegendColors() {
        return legendColors;
    }

    public void setLegendColors(Color[] legendColors) {
        this.legendColors = legendColors;
    }

    public Color getIsoColor() {
        return isoColor;
    }

    public void setIsoColor(Color isoColor) {
        this.isoColor = isoColor;
    }

    public boolean showGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public float[][] getZ() {
        return z;
    }
}
