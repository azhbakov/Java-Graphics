package ru.nsu.fit.g13201.azhbakov;

import ru.nsu.fit.g13201.azhbakov.view.hex.Hex;
import ru.nsu.fit.g13201.azhbakov.view.hex.HexGenerator;
import ru.nsu.fit.g13201.azhbakov.view.hex.SpanFilling;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Martin on 06.03.2016.
 */
public class SpanFillingTest {

    public static void main(String[] args) {
        HexGenerator.initGenerator(100, 1);
        Hex hex = HexGenerator.drawHex(Color.white);
        for (int i = 30; i < 50; i++) {
            hex.image.setRGB(i, 50, Color.black.getRGB());
        }
        for (int i = 60; i < 80; i++) {
            hex.image.setRGB(i, 50, Color.yellow.getRGB());
            hex.image.setRGB(i, 51, Color.yellow.getRGB());
            hex.image.setRGB(i, 52, Color.yellow.getRGB());
            hex.image.setRGB(i, 53, Color.yellow.getRGB());
        }
        SpanFilling.fill(hex.image, 50, 50, Color.red);


        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon(hex.image)));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
