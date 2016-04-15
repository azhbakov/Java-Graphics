package model;

import java.awt.*;
import java.io.File;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by Martin on 01.04.2016.
 */
public class Logic extends Observable {
    final String defaultSettings = "Iso/Data/settings.txt";

    float a = 10, b = 20, c = 10, d = 20;
    int k, m;
    int n;

    boolean showGrid = true;
    boolean showIsolines = true;
    boolean lerp = true;

    public Logic () {
        try {
            readSettings(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void init () {

    }

    public void readSettings (File f) throws Exception {
        if (f == null)
            f = new File(defaultSettings);
        int k, m, n;
        Color[] legendColors;
        Color isoColor;
        Scanner reader = new Scanner(f);

        k = reader.nextInt();
        m = reader.nextInt();
        n = reader.nextInt();
        legendColors = new Color[n + 1];
        for (int i = 0; i <= n; i++) {
            legendColors[i] = new Color(reader.nextInt(), reader.nextInt(), reader.nextInt());
        }
        isoColor = new Color(reader.nextInt(), reader.nextInt(), reader.nextInt());
        this.k = k;
        this.m = m;
        this.n = n;

        init();
        setChanged(); notifyObservers();
    }


    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
        init();
        setChanged(); notifyObservers();
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
        init();
        setChanged(); notifyObservers();
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
        init();
        setChanged(); notifyObservers();
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
        init();
        //System.out.println("Set d " + d);
        setChanged(); notifyObservers();
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        if (k < 2) return;
        this.k = k;
        init();
        setChanged(); notifyObservers();
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        if (m < 2) return;
        this.m = m;
        init();
        setChanged(); notifyObservers();
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        init();
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

    public boolean lerp() {
        return lerp;
    }

    public void setLerp(boolean lerp) {
        this.lerp = lerp;
        setChanged(); notifyObservers();
    }
}
