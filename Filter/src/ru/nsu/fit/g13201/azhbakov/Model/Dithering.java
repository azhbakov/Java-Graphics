package ru.nsu.fit.g13201.azhbakov.model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by marting422 on 15.03.2016.
 */
public class Dithering {

    public static BufferedImage FloydSteinberg (BufferedImage image, int[] reds, int[] greens, int[] blues) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        //float[][] fr = new float[image.getWidth()][image.getHeight()];
        //float[][] fg = new float[image.getWidth()][image.getHeight()];
        //float[][] fb = new float[image.getWidth()][image.getHeight()];
        for (int y = 0; y <image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color old = new Color(image.getRGB(x, y));
                int pr = trunc (reds, old.getRed());
                int pg = trunc (greens, old.getGreen());
                int pb = trunc (blues, old.getBlue());
                int er = old.getRed() - pr;
                int eg = old.getGreen() - pg;
                int eb = old.getBlue() - pb;

                int newr, newg, newb;
                //fr[x][y] += old.getRed();
               // fg[x][y] += old.getGreen();
                //fb[x][y] += old.getBlue();
                res.setRGB(x, y, new Color(pr, pg, pb).getRGB());

                if (x < image.getWidth()-1) {
                    old = new Color(image.getRGB(x + 1, y));
                    newr = trunc(reds, (int)((float)old.getRed() + (float)7 * er / 16));
                    newg = trunc(greens, (int)((float)old.getGreen() + (float)7 * eg / 16));
                    newb = trunc(blues, (int)((float)old.getBlue() + (float)7 * eb / 16));
                    res.setRGB(x + 1, y, new Color(newr,
                            newg,
                            newb).getRGB());
                }

                if (x > 0 && y < image.getHeight()-1) {
                    old = new Color(image.getRGB(x-1, y+1));
                    newr = trunc(reds, (int)((float)old.getRed() + (float)3*er/16));
                    newg = trunc(greens, (int)((float)old.getGreen() + (float)3*eg/16));
                    newb = trunc(blues, (int)((float)old.getBlue() + (float)3*eb/16));
                    res.setRGB(x-1, y+1, new Color(newr,
                            newg,
                            newb).getRGB());
                }

                if (y < image.getHeight()-1) {
                    old = new Color(image.getRGB(x, y + 1));
                    newr = trunc(reds, (int)((float)old.getRed() + (float)5 * er / 16));
                    newg = trunc(greens, (int)((float)old.getGreen() + (float)(5 * eg / 16)));
                    newb = trunc(blues, (int)((float)old.getBlue() + (float)5 * eb / 16));
                    res.setRGB(x, y + 1, new Color(newr,
                            newg,
                            newb).getRGB());
                }

                if (x < image.getWidth()-1 && y < image.getHeight()-1) {
                    old = new Color(image.getRGB(x + 1, y + 1));
                    newr = trunc(reds, (int)((float)old.getRed() + (float)1 * er / 16));
                    newg = trunc(greens, (int)((float)old.getGreen() + (float)1 * eg / 16));
                    newb = trunc(blues, (int)((float)old.getBlue() + (float)1 * eb / 16));
                    res.setRGB(x + 1, y + 1, new Color(newr,
                            newg,
                            newb).getRGB());
                }
            }
        }
        return res;
    }

    private static int trunc (int[] newc, int oldc) {
        int min = oldc, ind = 0;
        for (int i = 0; i < newc.length; i++) {
            if (Math.abs(oldc-newc[i]) < min) {
                min = Math.abs(oldc-newc[i]);
                ind = i;
            }
//            if (newc[i] < oldc && oldc-newc[i] < min) {
//                min = oldc - newc[i];
//                ind = i;
//            }
        }
        return newc[ind];
    }

    public static BufferedImage orderedDithering (BufferedImage image, int[] reds, int[] greens, int[] blues) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int matSize = 4;
        int k = 1/(matSize*matSize + 1);
        int[][] tMat = {{1,9,3,11},
                        {13, 5, 15, 7},
                        {4, 12, 2, 10},
                        {16, 8, 14, 6}};
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color old = new Color(image.getRGB(x, y));
                int red = old.getRed() + old.getRed() * tMat[x%4][y%4] * k;
                int green = old.getGreen() + old.getRed() * tMat[x%4][y%4] * k;
                int blue = old.getBlue() + old.getRed() * tMat[x%4][y%4] * k;

                red = trunc(reds, red);
                green = trunc(greens, green);
                blue = trunc(blues, blue);

                res.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return res;
    }
}
