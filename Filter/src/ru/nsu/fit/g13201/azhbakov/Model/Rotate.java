package ru.nsu.fit.g13201.azhbakov.model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Martin on 20.03.2016.
 */
public class Rotate {
    public static BufferedImage rotate (BufferedImage image, double deg) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int centerX = image.getWidth()/2;
        int centerY = image.getHeight()/2;
        //deg = deg/180*Math.PI;
//        double[][] op = {{Math.cos(deg), -Math.sin(deg)},
//                {Math.sin(deg), Math.cos(deg)}};

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                float r = (float) Math.sqrt((x-centerX)*(x-centerX) + (y-centerY)*(y-centerY));
                double phi = deg/180*Math.PI + getCurrentDeg (x-centerX, y-centerY);
                int nx = centerX + (int)(Math.cos(phi)*r);
                int ny = centerY + (int)(Math.sin(phi)*r);
//                int nx = (int)((x-centerX) * Math.cos(deg) - (y-centerY) * Math.sin(deg) + centerX);
//                int ny = (int)((x-centerX) * Math.sin(deg) + (y-centerY) * Math.cos(deg) + centerY);
//                double i = (x-centerX)*1 - (y-centerY)*Math.tan(deg)/2;
//                double j = (x-centerX)*0 + (y-centerY)*1;
//                i = i*1 - j*0;
//                j = i*Math.sin(deg) + j*1;
//                i = i*1 - j*Math.tan(deg)/2;
//                j = i*0 + j*1;
//                int nx = (int)i;
//                int ny = (int)j;
                if (0 <= nx && nx < image.getWidth() && 0 <= ny && ny < image.getHeight()) {
                    res.setRGB(x, y, image.getRGB(nx, ny));
                } else {
                    res.setRGB(x, y, Color.white.getRGB());
                }
            }
        }
        return res;
    }

    private static double getCurrentDeg (int xm, int ym) { // xm == x - centerX, ym == y - centerY
        double deg = Math.PI/2;
        if (xm != 0)
            deg = Math.atan((Math.abs((double)ym/xm)));
        if (0 <= xm && 0 <= ym) return deg;
        if (xm <= 0 && 0 <= ym) return Math.PI - deg;
        if (xm <= 0 && ym <= 0) return Math.PI + deg;
        if (0 <= xm && ym <= 0) return Math.PI*2 - deg;
        return 0; // unreachable
    }
}
