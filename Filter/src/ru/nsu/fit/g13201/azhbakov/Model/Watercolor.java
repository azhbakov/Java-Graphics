package ru.nsu.fit.g13201.azhbakov.Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by Martin on 20.03.2016.
 */
public class Watercolor {
    public static BufferedImage watercolor (BufferedImage image) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int[] weightedR = new int[25];
        int[] weightedG = new int[25];
        int[] weightedB = new int[25];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        int t1 = x+i;
                        int t2 = y+j;
                        if (t1 < 0) t1 = 0;
                        if (t1 >= image.getWidth()) t1 = image.getWidth()-1;
                        if (t2 < 0) t2 = 0;
                        if (t2 >= image.getHeight()) t2 = image.getHeight()-1;

                        Color col = new Color(image.getRGB(t1, t2));

                        int ind = (i+2)*5 + (j+2);
                        weightedR[ind] = col.getRed();
                        weightedG[ind] = col.getGreen();
                        weightedB[ind] = col.getBlue();
                    }
                }
                // red
                Arrays.sort(weightedR);
                int r = weightedR[13];
                // green
                Arrays.sort(weightedG);
                int g = weightedG[13];
                // blue
                Arrays.sort(weightedB);
                int b = weightedB[13];


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
