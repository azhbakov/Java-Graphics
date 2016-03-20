package ru.nsu.fit.g13201.azhbakov.Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by Martin on 20.03.2016.
 */
public class Sharpening {

    public static BufferedImage sharpen (BufferedImage image) {
         int[][] op = {{0, -1, 0},
                    {-1, 5, -1},
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
                if (x == 0 || x == image.getWidth()-1 || y == 0 || y == image.getHeight()-1) {
                    res.setRGB(x, y, image.getRGB(x,y));
                    continue;
                }

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color col = new Color(image.getRGB(x+i, y+j));
                        r += col.getRed() * op[i+1][j+1];
                        g += col.getGreen() * op[i+1][j+1];
                        b += col.getBlue() * op[i+1][j+1];
                    }
                }
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
