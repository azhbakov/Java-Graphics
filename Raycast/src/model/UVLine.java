package model;

import java.awt.*;

/**
 * Created by marting422 on 25.04.2016.
 */
public class UVLine {
    float x1, x2, y1, y2;
    Color c;
    String s;

    public UVLine (float x1, float y1, float x2, float y2, Color c, String s) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.c = c;
        this.s = s;
    }

    public void drawLine (Graphics2D g2, int width, int height) {
        //width = getWidth();
        //height = getHeight();
        g2.setColor(c);
        g2.drawLine((int)(x1 * width), (int)((1-y1) * height),
                (int)(x2 * width), (int)((1-y2) * height));
        if (s != null) {
            g2.drawString(s, (int) (x2 * width), (int) ((1 - y2) * height));
        }
    }
}
