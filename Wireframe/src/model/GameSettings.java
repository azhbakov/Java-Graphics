package model;

import java.awt.*;

/**
 * Created by azhbakov on 01.03.2016.
 */
public class GameSettings {
    public int n, m, k;
    public float a, b, c, d;
    public float zf, zb, sw, sh;
    public float[][] rotMat;
    public Color backgroundCol;
    public RotationBody[] bodies;

    public GameSettings(int n, int m, int k, float a, float b, float c, float d,
                        float zf, float zb, float sw, float sh,
                        float[][] rotMat, Color backgroundCol, RotationBody[] bodies) {
        this.n = n;
        this.m = m;
        this.k = k;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.zf = zf;
        this.zb = zb;
        this.sw = sw;
        this.sh = sh;
        this.rotMat = rotMat;
        this.backgroundCol = backgroundCol;
        this.bodies = bodies;
    }
}
