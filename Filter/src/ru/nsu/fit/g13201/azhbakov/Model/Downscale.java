package ru.nsu.fit.g13201.azhbakov.Model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by marting422 on 11.03.2016.
 */
public class Downscale {
    public static BufferedImage downscale (BufferedImage source, int width, int height) {
        BufferedImage res = new BufferedImage(width, height, source.getType());

        float stepX = (float)source.getWidth()/width;
        float stepY = (float)source.getHeight()/height;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                res.setRGB(i,j, getColor(source, i*stepX, (i+1)*stepX, j*stepY, (j+1)*stepY));
            }
        }

        return res;
    }

    private static int getColor (BufferedImage image, float fromX, float toX, float fromY, float toY) {
        int c = 0, n = 0;
        //System.out.println("from x == " + fromX + " to x == " + toX + ", from y == " + fromY + " to y ==" + toY);
        //System.out.println("PIXELS: from x == " + (int)Math.ceil(fromX) + " to x == " + (int)Math.ceil(toX) + ", from y == " + (int)Math.ceil(fromY) + " to y ==" + (int)Math.ceil(toY));
        /*for (int i = (int)Math.ceil(fromX); i < (int)Math.ceil(toX); i++) {
            for (int j = (int)Math.ceil(fromY); j < (int)Math.ceil(toY); j++) {
                n++;
                c += image.getRGB(i,j);
            }
        }*/
        int x = ((int)Math.ceil(toX)-(int)Math.ceil(fromX))/2 + (int)Math.ceil(fromX);
        int y = ((int)Math.ceil(toY)-(int)Math.ceil(fromY))/2 + (int)Math.ceil(fromY);
        return image.getRGB(x, y);
        //return c/n;
    }
}
