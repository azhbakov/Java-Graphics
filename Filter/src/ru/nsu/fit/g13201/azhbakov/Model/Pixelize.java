package ru.nsu.fit.g13201.azhbakov.model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Martin on 21.03.2016.
 */
public class Pixelize {
    static int size = 10;
    public static BufferedImage pixelize (BufferedImage image) {
        if (image.getWidth()<size || image.getHeight()<size) return null;
        BufferedImage res = new BufferedImage(image.getWidth()/size, image.getHeight()/size, image.getType());
        for (int y = 0; y < res.getHeight(); y++) {
            for (int x = 0; x < res.getWidth(); x++) {
                res.setRGB(x, y, avgColor(image, x*size, y*size).getRGB());
            }
        }
        return res;
    }

    public static BufferedImage unpixelize (BufferedImage image) {
        BufferedImage res = new BufferedImage(image.getWidth()*size, image.getHeight()*size, image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        res.setRGB(x * size + i, y * size + j, rgb);
                    }
                }
            }
        }
        return res;
    }

    private static Color avgColor (BufferedImage image, int x, int y) {
        int ex, ey;
        if (x+size < image.getWidth()) {
            ex = x+size;
        } else {
            ex = image.getWidth()-1;
        }
        if (y+size < image.getHeight()) {
            ey = y+size;
        } else {
            ey = image.getHeight()-1;
        }
        int mr = 0;
        int mg = 0;
        int mb = 0;
        for (int i = x; i < ex; i++) {
            for (int j = y; j < ey; j++) {
                Color c = new Color(image.getRGB(i, j));
                mr += c.getRed();
                mg += c.getGreen();
                mb += c.getBlue();
            }
        }
        int k = (ex-x)*(ey-y);
        return new Color(mr/k, mg/k, mb/k);
    }
}
