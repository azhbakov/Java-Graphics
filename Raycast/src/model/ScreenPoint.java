package model;

import java.awt.*;

/**
 * Created by Martin on 09.05.2016.
 */
public class ScreenPoint {
    int x, y;
    Color c;

    public ScreenPoint(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }

    public void drawPoint (Graphics2D g2, int width, int height) {
        //width = getWidth();
        //height = getHeight();
        g2.setColor(c);
        g2.drawLine(x, height-y, x, height-y);
    }
}
