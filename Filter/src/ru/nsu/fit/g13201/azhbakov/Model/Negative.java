package ru.nsu.fit.g13201.azhbakov.model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Martin on 13.03.2016.
 */
public class Negative {
    public static BufferedImage negative (BufferedImage image) {
        BufferedImage res = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color old = new Color(image.getRGB(i, j));
                //int y = (int)(0.2989*old.getRed() + 0.5870 * old.getGreen() + 0.1140 * old.getRed());
                Color negative = new Color(255-old.getRed(), 255-old.getGreen(), 255-old.getBlue());
                res.setRGB(i, j, negative.getRGB());
            }
        }
        return res;
    }
}
