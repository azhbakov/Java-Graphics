package ru.nsu.fit.g13201.azhbakov.Model;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Martin on 12.03.2016.
 */
public class Grayscale {
    public static BufferedImage grayscale (BufferedImage image) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color old = new Color(image.getRGB(i, j));
                int y = (int)(0.2989*old.getRed() + 0.5870 * old.getGreen() + 0.1140 * old.getRed());
                Color gray = new Color(y, y, y);
                image.setRGB(i, j, gray.getRGB());
            }
        }
        return image;
    }
}
