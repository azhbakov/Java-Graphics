package ru.nsu.fit.g13201.azhbakov;

import javax.swing.*;
import java.awt.*;

/**
 * Created by marting422 on 12.03.2016.
 */
public class PaintArea extends JPanel {
    int frameLeft, frameRight, frameUp, frameBottom;

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        if (frameBottom == 0 && frameUp == 0 && frameLeft == 0 && frameRight == 0) return;
        g2.drawRect(frameLeft, frameBottom, frameRight-frameLeft, frameUp-frameBottom);
    }

    public void setRect (int left, int right, int up, int bottom) {
        frameLeft = left;
        frameRight = right;
        frameUp = up;
        frameBottom = bottom;
        //System.out.println("left == " + frameLeft + " right == " + frameRight + " bottom == " + frameBottom + " up == " + frameUp);
        revalidate();
        repaint();
    }
}
