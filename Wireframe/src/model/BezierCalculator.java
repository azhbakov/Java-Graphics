package model;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by marting422 on 24.04.2016.
 */
public class BezierCalculator {
    public static Point2D.Float[] getBSplinePoints (Point2D.Float[] g) {
        if (g.length < 4) return null;
        float[][] M = {{-1, 3, -3, 1},
                {3, -6, 3, 0},
                {-3, 0, 3, 0},
                {1, 4, 1, 0}};
        float k = 1f/6;
        int n = 15;
        Point2D.Float[] res = new Point2D.Float[n];
        for (int np = 0; np < n; np++) {
            float px = 0;
            float py = 0;
            for (int i = 0; i < 4; i++) {
                float x = 0;
                float y = 0;
                for (int j = 0; j < 4; j++) {
                    x += M[i][j] * g[j].x;
                    y += M[i][j] * g[j].y;
                }
                //System.out.println(x);
                x *= Math.pow(1f/(n-1) * np, 3-i) * k;
                y *= Math.pow(1f/(n-1) * np, 3-i) * k;
                px += x;
                py += y;
            }
            res[np] = new Point2D.Float(px, py);
        }
        return res;
    }

    public static Point[] getBSplinePoints (Point[] g) {
        float[][] M = {{-1, 3, -3, 1},
                {3, -6, 3, 0},
                {-3, 0, 3, 0},
                {1, 4, 1, 0}};
        float k = 1f/6;
        int n = 5;
        Point[] res = new Point[n];
        for (int np = 0; np < n; np++) {
            float px = 0;
            float py = 0;
            for (int i = 0; i < 4; i++) {
                float x = 0;
                float y = 0;
                for (int j = 0; j < 4; j++) {
                    x += M[i][j] * g[j].x;
                    y += M[i][j] * g[j].y;
                }
                //System.out.println(x);
                x *= Math.pow(1f/(n-1) * np, 3-i) * k;
                y *= Math.pow(1f/(n-1) * np, 3-i) * k;
                px += x;
                py += y;
            }
            res[np] = new Point((int)px, (int)py);
        }
        return res;
    }

    public static Curve getBSplineCurve (Point2D.Float[] g) {
        if (g.length < 4) return null;
        Curve res = new Curve();
        for (int i = 1; i < g.length-2; i++) {
            Point2D.Float[] arg = {g[i-1],
                    g[i],
                    g[i+1],
                    g[i+2]};
            Point2D.Float[] p = BezierCalculator.getBSplinePoints(arg);

            for (int j = 1; j < p.length; j++) {
                res.addPoint(p[j]);
                //System.out.println(j + ": " + p[j].x + " " + p[j].y);
            }
        }
        return res;
    }
}
