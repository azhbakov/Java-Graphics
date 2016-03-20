package ru.nsu.fit.g13201.azhbakov.Model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Martin on 20.03.2016.
 */
public class Stamping {
    public static BufferedImage stamp (BufferedImage image) {
        int[][] op = {{0, 1, 0},
                {-1, 0, 1},
                {0, -1, 0}};

        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

//        int[] weightedR = new int[9];
//        int[] weightedG = new int[9];
//        int[] weightedB = new int[9];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                int r = 0;
                int g = 0;
                int b = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int t1 = x+i;
                        int t2 = y+j;
                        if (t1 < 0) t1 = 0;
                        if (t1 >= image.getWidth()) t1 = image.getWidth()-1;
                        if (t2 < 0) t2 = 0;
                        if (t2 >= image.getHeight()) t2 = image.getHeight()-1;
                        Color col = new Color(image.getRGB(t1, t2));
                        r += col.getRed() * op[i+1][j+1];
                        g += col.getGreen() * op[i+1][j+1];
                        b += col.getBlue() * op[i+1][j+1];
                    }
                }
                r += 128;
                g += 128;
                b += 128;
                if (r > 255) r = 255;
                if (r < 0) r = 0;
                if (g > 255) g = 255;
                if (g < 0) g = 0;
                if (b > 255) b = 255;
                if (b < 0) b = 0;
                res.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        return res;
    }
}
