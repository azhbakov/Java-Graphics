package ru.nsu.fit.g13201.azhbakov.Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by Martin on 20.03.2016.
 */
public class Smoothing {
    public static BufferedImage smooth (BufferedImage image) {
        /*int[][] op = {{1, 2, 1},
                    {2, 4, 2},
                    {1, 2, 1}};*/

        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int[] weightedR = new int[9];
        int[] weightedG = new int[9];
        int[] weightedB = new int[9];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                if (x == 0 || x == image.getWidth()-1 || y == 0 || y == image.getHeight()-1) {
                    res.setRGB(x, y, image.getRGB(x,y));
                    continue;
                }

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        Color col = new Color(image.getRGB(x+i, y+j));
                        int ind = (i+1)*3 + (j+1);
                        weightedR[ind] = col.getRed();// * op[i+1][j+1];
                        weightedG[ind] = col.getGreen();// * op[i+1][j+1];
                        weightedB[ind] = col.getBlue();// * op[i+1][j+1];
                    }
                }
                // red
                Arrays.sort(weightedR);
                int red = weightedR[4];
                // green
                Arrays.sort(weightedG);
                int green = weightedG[4];
                // blue

                Arrays.sort(weightedB);
                int blue = weightedB[4];
                res.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }

        return res;
    }
}
