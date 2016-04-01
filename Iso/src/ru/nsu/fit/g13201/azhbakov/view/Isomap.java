package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.model.Logic;

import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Martin on 01.04.2016.
 */
public class Isomap extends JPanel implements Observer {
    Logic logic;
    int offset = 30;
    Dimension mapAreaSize;

    public Isomap (Logic logic, Dimension mapAreaSize) {
        this.mapAreaSize = mapAreaSize;
        setPreferredSize(new Dimension(mapAreaSize.width + 2*offset, mapAreaSize.height + 2*offset));
        this.logic = logic;
        logic.addObserver(this);
    }

    public void paint (Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        fillMap(g2);
        if (logic.showGrid()) {
            drawGrid(g2);
        }
        drawIsolines(g2);
        //drawValues(g2);
    }

    private void fillMap (Graphics2D g2) {
        float[] isolevels = logic.getIsolevels();
        Color[] legendColors = logic.getLegendColors();
        for (int i = 0; i < mapAreaSize.width; i++) {
            for (int j = 0; j < mapAreaSize.height; j++) {
                float z = zFromUV((float)i/mapAreaSize.width, (float)j/mapAreaSize.height);
                if (i == 250 && j == 250) System.out.println(z);
                for (int k = 0; k < isolevels.length; k++) {
//                    if (z < isolevels[k]) {
//                        g2.setColor(logic.getLegendColors()[k]);
//                        g2.drawLine(offset+i, offset+j, offset+i, offset+j);
//                        break;
//                    }
//                    if (k == isolevels.length-1) {
//                        g2.setColor(logic.getLegendColors()[k+1]);
//                        g2.drawLine(offset+i, offset+j, offset+i, offset+j);
//                    }
                    Color c = new Color(0,0,0);
                    if (z < isolevels[k]) {
                        if (k == 0) c = legendColors[k];
                        else {
                            Color c1 = legendColors[k-1];
                            Color c2 = legendColors[k];
                            int r = c1.getRed() + (int)((z-isolevels[k-1])/(isolevels[k]-isolevels[k-1])*(c2.getRed()-c1.getRed()));
                            int g = c1.getGreen() + (int)((z-isolevels[k-1])/(isolevels[k]-isolevels[k-1])*(c2.getGreen()-c1.getGreen()));
                            int b = c1.getBlue() + (int)((z-isolevels[k-1])/(isolevels[k]-isolevels[k-1])*(c2.getBlue()-c1.getBlue()));
                            c = new Color(r, g, b);
                        }
                        g2.setColor(c);
                        g2.drawLine(offset+i, offset+j, offset+i, offset+j);
                        break;
                    }
                    if (k == isolevels.length-1) { // if bigger then last Z
                        Color c1 = legendColors[k];
                        Color c2 = legendColors[k+1];
                        int r = c1.getRed() + (int)((z-isolevels[k])/(isolevels[k]-isolevels[k-1])*(c2.getRed()-c1.getRed()));
                        int g = c1.getGreen() + (int)((z-isolevels[k])/(isolevels[k]-isolevels[k-1])*(c2.getGreen()-c1.getGreen()));
                        int b = c1.getBlue() + (int)((z-isolevels[k])/(isolevels[k]-isolevels[k-1])*(c2.getBlue()-c1.getBlue()));
                        if (r > 255) r = 255; if (r < 0) r = 0;
                        if (g > 255) g = 255; if (g < 0) g = 0;
                        if (b > 255) b = 255; if (b < 0) b = 0;
                        c = new Color(r, g, b);
                        g2.setColor(c);
                        g2.drawLine(offset+i, offset+j, offset+i, offset+j);
                    }
                }
            }
        }
        g2.setColor(Color.black);
    }

    private float zFromUV (float x, float y) {
        float[][] z = logic.getZ();
        float a = logic.getA();
        float b = logic.getB();
        float c = logic.getC();
        float d = logic.getD();
        float argStepX = (logic.getB()-logic.getA())/(logic.getK()-1);
        float argStepY = (logic.getD()-logic.getC())/(logic.getM()-1);
        float argX = a + (b-a)*x;
        float argY = c + (d-c)*y;
        float f1 = z[(int)(argX/argStepX)][(int)(argY/argStepY)+1];
        float f2 = z[(int)(argX/argStepX)+1][(int)(argY/argStepY)+1];
        float f3 = z[(int)(argX/argStepX)][(int)(argY/argStepY)];
        float f4 = z[(int)(argX/argStepX)+1][(int)(argY/argStepY)];
        float fu = f1 + (f2-f1)/argStepX*(argX-(int)(argX/argStepX)*argStepX);
        float fd = f3 + (f4-f3)/argStepX*(argX-(int)(argX/argStepX)*argStepX);
//        if (x == 0.5f && y == 0.5f) {
//            System.out.println(argX + " " + argY + " " + f1 + " " + f2);
//        }
        return fd + (fu-fd)/argStepY*(argY-(int)(argY/argStepY)*argStepY);
    }

    private void drawGrid(Graphics2D g2) {
        float stepX = (float)mapAreaSize.width/(logic.getK()-1);
        float stepY = (float)mapAreaSize.height/(logic.getM()-1);
        float argStepX = (logic.getB()-logic.getA())/(logic.getK()-1);
        float argStepY = (logic.getD()-logic.getC())/(logic.getM()-1);
        for (int i = 0; i < logic.getK(); i++) {
            g2.drawString(String.format("%.2f", logic.getA() + i*argStepX), offset - 8 + i*stepX, offset - 8);
            g2.drawLine(offset + (int)(i*stepX), offset + 0, offset + (int)(i*stepX), offset+mapAreaSize.height);
        }
        for (int i = 0; i < logic.getM(); i++) {
            if (i > 0) { // avoid overpainting zero
                g2.drawString(String.format("%.2f", logic.getC() + i * argStepY), 0, offset + 6 + i*stepY);
            }
            g2.drawLine(offset + 0, offset + (int)(i*stepY), offset+mapAreaSize.width, offset + (int)(i*stepY));
        }
    }

    private void drawValues (Graphics2D g2) {
        float argStepX = (logic.getB()-logic.getA())/(logic.getK()-1);
        float argStepY = (logic.getD()-logic.getC())/(logic.getM()-1);
        for (int j = 0; j < logic.getM(); j++) {
            for (int i = 0; i < logic.getK(); i++) {
                g2.drawString(String.format("%.2f", logic.getZ()[i][j]), (float) modelToImage(new Point2D.Float(i * argStepX, j * argStepY)).getX(),
                        (float) modelToImage(new Point2D.Float(i * argStepX, j * argStepY)).getY());
            }
        }
    }

    private void drawIsolines (Graphics2D g2) {
        float argStepX = (logic.getB()-logic.getA())/(logic.getK()-1);
        float argStepY = (logic.getD()-logic.getC())/(logic.getM()-1);
        for (int j = 0; j < logic.getM()-1; j++) {
            for (int i = 0; i < logic.getK()-1; i++) {
                float f1 = logic.getZ()[i][j+1];
                float f2 = logic.getZ()[i+1][j+1];
                float f3 = logic.getZ()[i][j];
                float f4 = logic.getZ()[i+1][j];

                for (int k = 0; k < logic.getN(); k++) { // iterate over z levels
                    float z = logic.getIsolevels()[k];
                    // Find intersections
                    ArrayList<Point2D> intersections = new ArrayList<>();
                    if (f1 < z && z < f2 || f2 < z && z < f1) {  // TODO fix equals
                        intersections.add(new Point2D.Float(i*argStepX + argStepX * Math.abs(z-f1)/Math.abs(f1-f2),
                                (j+1)*argStepY));
                    }
                    if (f4 < z && z < f2 || f2 < z && z < f4) {  // TODO fix equals
                        intersections.add(new Point2D.Float((i+1)*argStepX,
                                j*argStepY + argStepY * Math.abs(z-f4)/Math.abs(f4-f2)));
                    }
                    if (f3 < z && z < f4 || f4 < z && z < f3) {  // TODO fix equals
                        intersections.add(new Point2D.Float(i*argStepX + argStepX * Math.abs(z-f3)/Math.abs(f4-f3),
                                j*argStepY));
                    }
                    if (f1 < z && z < f3 || f3 < z && z < f1) {  // TODO fix equals
                        intersections.add(new Point2D.Float(i*argStepX,
                                j*argStepY + argStepY * Math.abs(z-f3)/Math.abs(f3-f1)));
                    }

                    for (int q = 0; q < intersections.size()-1; q++) {
                        Point2D from = modelToImage(intersections.get(q));
                        Point2D to = modelToImage(intersections.get(q+1));
                        g2.drawLine((int)from.getX(), (int)from.getY(), (int)to.getX(), (int)to.getY());
                    }
                }
            }
        }
    }

    public Point2D modelToImage (Point2D modelPoint) {
        return new Point2D.Float(offset + (float)(modelPoint.getX()-logic.getA())/(logic.getB()-logic.getA()) * mapAreaSize.width,
                offset + (float)(modelPoint.getY()-logic.getC())/(logic.getD()-logic.getC()) * mapAreaSize.height);
    }

    public void update (Observable observable, Object object) {
        repaint();
    }
}
