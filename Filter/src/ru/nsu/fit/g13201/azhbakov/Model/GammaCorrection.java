package ru.nsu.fit.g13201.azhbakov.model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Martin on 20.03.2016.
 */
public class GammaCorrection {
    public static BufferedImage gammaCorrection (BufferedImage image, double gamma) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color c = new Color(image.getRGB(x,y));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();

                r = (int)(Math.pow((double)r/255, gamma) * 255);
                g = (int)(Math.pow((double)g/255, gamma) * 255);
                b = (int)(Math.pow((double)b/255, gamma) * 255);

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
