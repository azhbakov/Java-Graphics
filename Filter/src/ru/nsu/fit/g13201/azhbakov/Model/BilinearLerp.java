package ru.nsu.fit.g13201.azhbakov.model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Martin on 13.03.2016.
 */
public class BilinearLerp {

    /* Source - https://ru.wikibooks.org/ */
    public static BufferedImage zoomX2(BufferedImage image) {
        int oldw = image.getWidth()/2;
        int oldh = image.getHeight()/2;
        int neww = image.getWidth();
        int newh = image.getHeight();
        int startX = image.getWidth()/4;
        int startY = image.getHeight()/4;

        int h, w;
        float t;
        float u;
        float tmp;
        float d1, d2, d3, d4;
        Color p1, p2, p3, p4;

        int red, green, blue;
        BufferedImage res = new BufferedImage(neww, newh, BufferedImage.TYPE_3BYTE_BGR);

        for (int j = 0; j < newh; j++) {
            tmp = (float) (j) / (float) (newh - 1) * (oldh - 1) + startY;
            h = (int) Math.floor(tmp);
            if (h < 0 + startY) {
                h = 0 + startY;
            } else {
                if (h >= startY + oldh - 1) {
                    h = startY + oldh - 2;
                }
            }
            u = tmp - h;

            for (int i = 0; i < neww; i++) {

                tmp = (float) (i) / (float) (neww - 1) * (oldw - 1) + startX;
                w = (int) Math.floor(tmp);
                if (w < 0 + startX) {
                    w = 0 + startX;
                } else {
                    if (w >= startX + oldw - 1) {
                        w = startX + oldw - 2;
                    }
                }
                t = tmp - w;

			/* Weights */
                d1 = (1 - t) * (1 - u);
                d2 = t * (1 - u);
                d3 = t * u;
                d4 = (1 - t) * u;

			/* Lerp between */
                p1 = new Color (image.getRGB(w, h));
                p2 = new Color (image.getRGB(w, h+1));
                p3 = new Color (image.getRGB(w+1, h+1));
                p4 = new Color (image.getRGB(w+1, h));

                blue = (int)(p1.getBlue() * d1 + p2.getBlue()*d2 + p3.getBlue()*d3 + p4.getBlue()*d4);
                green = (int)(p1.getGreen() * d1 + p2.getGreen()*d2 + p3.getGreen()*d3 + p4.getGreen()*d4);
                red = (int)(p1.getRed() * d1 + p2.getRed()*d2 + p3.getRed()*d3 + p4.getRed()*d4);

                res.setRGB(i, j, new Color(red,green,blue).getRGB());
            }
        }
        return res;
    }

    private static int bilinearLerp (int c1, int c2, int c3, int c4,
                                      int left, int right, int bottom, int up,
                                      int x, int y) throws Exception{
        if (x < left || right <= x || y < bottom || up <= y) throw new Exception();
        //int r1 = c3 * (right-x)/(right-left) + c4 * (x-left)/(right-left);
        //int r2 = c2 * (right-x)/(right-left) + c1 * (x-left)/(right-left);
        //int res = r2 * (up-y)/(up-bottom) + r1 * (y-bottom)/up-bottom;
        int r1 = linearLerp(c4, c3, left, right, x);
        int r2 = linearLerp(c1, c2, left, right, x);
        int res = linearLerp(r1, r2, bottom, up, y);
        return res;
    }

    private static int linearLerp (int c1, int c2,
                                   int left, int right,
                                   int x) {
        return c1 * (x-left)/(right-left) + c2 * (right-x)/(right-left);
    }
}
