package ru.nsu.fit.g13201.azhbakov.model;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by Martin on 01.04.2016.
 */
public class Logic extends Observable {
    final String defaultSettings = "Iso/Data/settings.txt";

    Func2 function = new CircleFunc();
    float a = 10, b = 20, c = 10, d = 20;
    int k, m;
    int n;
    Color[] legendColors;
    Color isoColor;
    float[][] z;
    float[] isolevels;

    boolean showGrid = true;
    boolean showIsolines = true;
    boolean lerp = true;

    public Logic () {
        init (null);
    }

    public void init (File f) {
        if (f == null)
            f = new File(defaultSettings);
        try {
            readSettings(f);

            // Init grid function
            z = new float[k][m];
            float stepX = (b-a)/(k-1);
            float stepY = (d-c)/(m-1);
            float min = function.res(a, c);
            float max = min;
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < m; j++) {
                    z[i][j] = function.res(a + stepX*i, c + stepY*j);
                    System.out.println("z[" + i + "][" + j + "] == " + z[i][j]);
                    if (z[i][j] < min) {
                        min = z[i][j];
                    }
                    if (z[i][j] > max) {
                        max = z[i][j];
                    }
                }
            }

            // Init z levels
            isolevels = new float[n];
            float step = (max - min)/(n+1);
            System.out.println("max == " + max + ", min == " + min + ", step == " + step);
            for (int i = 0; i < n; i++) {
                isolevels[i] = min + (i+1)*step;
                System.out.println("Z" + i + ": " + isolevels[i]
                        + ", lower color: " + legendColors[i]);
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
        legendColors = new Color[n+1];
        for (int i = 0; i <= n; i++) {
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
        setChanged(); notifyObservers();
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
        setChanged(); notifyObservers();
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
        setChanged(); notifyObservers();
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
        setChanged(); notifyObservers();
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
        setChanged(); notifyObservers();
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
        setChanged(); notifyObservers();
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        setChanged(); notifyObservers();
    }

    public Color[] getLegendColors() {
        return legendColors;
    }

    public void setLegendColors(Color[] legendColors) {
        this.legendColors = legendColors;
        setChanged(); notifyObservers();
    }

    public Color getIsoColor() {
        return isoColor;
    }

    public void setIsoColor(Color isoColor) {
        this.isoColor = isoColor;
        setChanged(); notifyObservers();
    }

    public boolean showIsolines() {
        return showIsolines;
    }

    public void setShowIsolines(boolean showIsolines) {
        this.showIsolines = showIsolines;
        setChanged(); notifyObservers();
    }

    public boolean showGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid; setChanged(); notifyObservers();
    }

    public float[][] getZ() {
        return z;
    }

    public float[] getIsolevels() {
        return isolevels;
    }

    public boolean lerp() {
        return lerp;
    }

    public void setLerp(boolean lerp) {
        this.lerp = lerp;
        setChanged(); notifyObservers();
    }
}
