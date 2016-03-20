package ru.nsu.fit.g13201.azhbakov.Model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Martin on 20.03.2016.
 */
public class EdgeDetection {
    public static BufferedImage SobelFilter (BufferedImage image, int threshold) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int GX[][] = {{-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}};

        int GY[][]={{ 1, 2, 1},
            { 0, 0, 0},
            {-1,-2,-1}};

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                float gx = 0;
                float gy = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int t1 = x+i;
                        int t2 = y+j;
                        if (t1 < 0) t1 = 0;
                        if (t1 >= image.getWidth()) t1 = image.getWidth()-1;
                        if (t2 < 0) t2 = 0;
                        if (t2 >= image.getHeight()) t2 = image.getHeight()-1;
                        gx += getIntensity(image, t1, t2) * GX[i+1][j+1];
                        gy += getIntensity(image, t1, t2) * GY[i+1][j+1];
                    }
                }
                int nc;
                if ((int)Math.sqrt(gx*gx+gy*gy) > threshold) {
                    nc = 255;
                } else {
                    nc = (int)Math.sqrt(gx*gx+gy*gy);
                }
                res.setRGB(x, y, new Color(nc,nc,nc).getRGB());
            }
        }
        return res;
    }

    private static float getIntensity (BufferedImage i, int x, int y) {
        float lum[] = {0.299f, 0.587f, 0.114f};
        Color c = new Color(i.getRGB(x,y));
        return (c.getBlue()*lum[0] + c.getRed()*lum[1] + c.getGreen()*lum[2])/3;
    }

    public static BufferedImage RobertsFilter (BufferedImage image, int threshold) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int GX[][] = {{1, 0},
                {0, -1}};

        int GY[][]={{ 0, 1},
                { -1, 0}};

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                float gx = 0;
                float gy = 0;
                for (int i = 0; i <= 1; i++) {
                    for (int j = 0; j <= 1; j++) {
                        int t1 = x+i;
                        int t2 = y+j;
                        if (t1 < 0) t1 = 0;
                        if (t1 >= image.getWidth()) t1 = image.getWidth()-1;
                        if (t2 < 0) t2 = 0;
                        if (t2 >= image.getHeight()) t2 = image.getHeight()-1;
                        gx += getIntensity(image, t1, t2) * GX[i][j];
                        gy += getIntensity(image, t1, t2) * GY[i][j];
                    }
                }
                int nc;
                if ((int)Math.sqrt(gx*gx+gy*gy) > threshold) {
                    nc = 255;
                } else {
                    nc = (int)Math.sqrt(gx*gx+gy*gy);
                }
                res.setRGB(x, y, new Color(nc,nc,nc).getRGB());
            }
        }
        return res;
    }
}
