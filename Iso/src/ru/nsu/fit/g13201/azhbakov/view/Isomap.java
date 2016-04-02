package ru.nsu.fit.g13201.azhbakov.view;

import ru.nsu.fit.g13201.azhbakov.model.Logic;

import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Martin on 01.04.2016.
 */
public class Isomap extends JPanel implements Observer {
    Logic logic;
    int offset = 32;
    Dimension mapAreaSize;
    JLabel mousePos;
    float[] userLevel = new float[1];

    public Isomap (Logic logic, Dimension mapAreaSize) {
        this.mapAreaSize = mapAreaSize;
        setPreferredSize(new Dimension(mapAreaSize.width + 2*offset + 80, mapAreaSize.height + 2*offset));
        this.logic = logic;
        logic.addObserver(this);
        setLayout(new BorderLayout());
        mousePos = new JLabel("(-:-):-");
        add(mousePos, BorderLayout.SOUTH);
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (e.getX() < offset || mapAreaSize.width + offset-1 < e.getX() ||
                        e.getY() < offset || mapAreaSize.height + offset-1 < e.getY()) return;
                Point2D imagePoint = new Point2D.Float(e.getX()-offset, e.getY()-offset);
                Point2D modelPoint = imageToModel(imagePoint);
                 mousePos.setText("(" + String.format("%.2f", modelPoint.getX())
                         + ":" + String.format("%.2f", modelPoint.getY())
                         + "): " + String.format("%.2f", zFromUV((float) imagePoint.getX()/mapAreaSize.width, (float)imagePoint.getY()/mapAreaSize.height)));
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getX() < offset || mapAreaSize.width + offset-1 < e.getX() ||
                        e.getY() < offset || mapAreaSize.height + offset-1 < e.getY()) return;
                Point2D imagePoint = new Point2D.Float(e.getX()-offset, e.getY()-offset);
                Point2D modelPoint = imageToModel(imagePoint);
                float z = zFromUV((float) imagePoint.getX()/mapAreaSize.width, (float)imagePoint.getY()/mapAreaSize.height);
                mousePos.setText("(" + String.format("%.2f", modelPoint.getX())
                        + ":" + String.format("%.2f", modelPoint.getY())
                        + "): " + String.format("%.2f", z));
                userLevel[0] = z;
                repaint();
            }
        });
    }

    public void paint (Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        drawLegend(g2);
        fillMap(g2);
        drawCoordinates(g2);
        if (logic.showGrid()) {
            drawGrid(g2);
        }
        if (logic.showIsolines()) {
            drawIsolines(g2, logic.getIsolevels());
            drawIsolines(g2, userLevel);
        }
        //drawValues(g2);
    }

    private void drawLegend (Graphics2D g2) {
        float[] isolevels = logic.getIsolevels();
        Color[] legendColors = logic.getLegendColors();
        int x1 = offset + mapAreaSize.width + 50;
        int x2 = x1 + 40;
        int y1 = offset + 50;//+ mapAreaSize.height/2 - 140;
        int y2 = offset + mapAreaSize.height - 50;///2 + 140;

        int n = logic.getN();
        float stepY = (y2-y1)/(n+1);
        float zStep = isolevels[1]-isolevels[0];

        g2.drawRect(x1, y1, x2-x1, y2-y1);
        for (int i = 0; i < n; i++) {
            g2.drawString(String.format("%.2f", isolevels[i]), x1-35, y1+stepY+i*stepY);
        }

        for (int j = 0; j < mapAreaSize.height-100; j++) {
            float z = isolevels[0]-zStep + (float)j/(mapAreaSize.height-100)*zStep*(n+1);
            //if (i == 250 && j == 250) System.out.println(z);
            for (int k = 0; k < isolevels.length; k++) {
                if (!logic.lerp()) {
                    if (z < isolevels[k]) {
                        g2.setColor(legendColors[k]);
                        g2.drawLine(x1, y1+j, x2, y1+j);
                        break;
                    }
                    if (k == isolevels.length-1) {
                        g2.setColor(legendColors[k+1]);
                        g2.drawLine(x1, y1+j, x2, y1+j);
                    }
                } else {
                    Color c;
                    if (z < isolevels[k]) {
                        if (k == 0) c = legendColors[k];
                        else {
                            Color c1 = legendColors[k - 1];
                            Color c2 = legendColors[k];
                            int r = c1.getRed() + (int) ((z - isolevels[k - 1]) / (isolevels[k] - isolevels[k - 1]) * (c2.getRed() - c1.getRed()));
                            int g = c1.getGreen() + (int) ((z - isolevels[k - 1]) / (isolevels[k] - isolevels[k - 1]) * (c2.getGreen() - c1.getGreen()));
                            int b = c1.getBlue() + (int) ((z - isolevels[k - 1]) / (isolevels[k] - isolevels[k - 1]) * (c2.getBlue() - c1.getBlue()));
                            c = new Color(r, g, b);
                        }
                        g2.setColor(c);
                        g2.drawLine(x1, y1+j, x2, y1+j);
                        break;
                    }
                    if (k == isolevels.length - 1) { // if bigger then last Z
                        Color c1 = legendColors[k];
                        Color c2 = legendColors[k + 1];
                        int r = c1.getRed() + (int) ((z - isolevels[k]) / (isolevels[k] - isolevels[k - 1]) * (c2.getRed() - c1.getRed()));
                        int g = c1.getGreen() + (int) ((z - isolevels[k]) / (isolevels[k] - isolevels[k - 1]) * (c2.getGreen() - c1.getGreen()));
                        int b = c1.getBlue() + (int) ((z - isolevels[k]) / (isolevels[k] - isolevels[k - 1]) * (c2.getBlue() - c1.getBlue()));
                        if (r > 255) r = 255;
                        if (r < 0) r = 0;
                        if (g > 255) g = 255;
                        if (g < 0) g = 0;
                        if (b > 255) b = 255;
                        if (b < 0) b = 0;
                        c = new Color(r, g, b);
                        g2.setColor(c);
                        g2.drawLine(x1, y1+j, x2, y1+j);
                    }
                }
            }
        }
        g2.setColor(Color.black);
    }

    private void fillMap (Graphics2D g2) {
        float[] isolevels = logic.getIsolevels();
        Color[] legendColors = logic.getLegendColors();
        for (int i = 0; i < mapAreaSize.width; i++) {
            for (int j = 0; j < mapAreaSize.height; j++) {
                float z = zFromUV((float)i/mapAreaSize.width, (float)j/mapAreaSize.height);
                //if (i == 250 && j == 250) System.out.println(z);
                for (int k = 0; k < isolevels.length; k++) {
                    if (!logic.lerp()) {
                        if (z < isolevels[k]) {
                            g2.setColor(legendColors[k]);
                            g2.drawLine(offset+i, offset+j, offset+i, offset+j);
                            break;
                        }
                        if (k == isolevels.length-1) {
                            g2.setColor(legendColors[k+1]);
                            g2.drawLine(offset+i, offset+j, offset+i, offset+j);
                        }
                    } else {
                        Color c;
                        if (z < isolevels[k]) {
                            if (k == 0) c = legendColors[k];
                            else {
                                Color c1 = legendColors[k - 1];
                                Color c2 = legendColors[k];
                                int r = c1.getRed() + (int) ((z - isolevels[k - 1]) / (isolevels[k] - isolevels[k - 1]) * (c2.getRed() - c1.getRed()));
                                int g = c1.getGreen() + (int) ((z - isolevels[k - 1]) / (isolevels[k] - isolevels[k - 1]) * (c2.getGreen() - c1.getGreen()));
                                int b = c1.getBlue() + (int) ((z - isolevels[k - 1]) / (isolevels[k] - isolevels[k - 1]) * (c2.getBlue() - c1.getBlue()));
                                c = new Color(r, g, b);
                            }
                            g2.setColor(c);
                            g2.drawLine(offset + i, offset + j, offset + i, offset + j);
                            break;
                        }
                        if (k == isolevels.length - 1) { // if bigger then last Z
                            Color c1 = legendColors[k];
                            Color c2 = legendColors[k + 1];
                            int r = c1.getRed() + (int) ((z - isolevels[k]) / (isolevels[k] - isolevels[k - 1]) * (c2.getRed() - c1.getRed()));
                            int g = c1.getGreen() + (int) ((z - isolevels[k]) / (isolevels[k] - isolevels[k - 1]) * (c2.getGreen() - c1.getGreen()));
                            int b = c1.getBlue() + (int) ((z - isolevels[k]) / (isolevels[k] - isolevels[k - 1]) * (c2.getBlue() - c1.getBlue()));
                            if (r > 255) r = 255;
                            if (r < 0) r = 0;
                            if (g > 255) g = 255;
                            if (g < 0) g = 0;
                            if (b > 255) b = 255;
                            if (b < 0) b = 0;
                            c = new Color(r, g, b);
                            g2.setColor(c);
                            g2.drawLine(offset + i, offset + j, offset + i, offset + j);
                        }
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
        float argX = (b-a)*x;
        float argY = (d-c)*y;
        float f1, f2, f3, f4;
        f1 = z[(int)(argX/argStepX)][(int)(argY/argStepY)+1];
        f2 = z[(int)(argX/argStepX)+1][(int)(argY/argStepY)+1];
        f3 = z[(int)(argX/argStepX)][(int)(argY/argStepY)];
        f4 = z[(int)(argX/argStepX)+1][(int)(argY/argStepY)];
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
            g2.drawLine(offset + (int)(i*stepX), offset + 0, offset + (int)(i*stepX), offset+mapAreaSize.height);
        }
        for (int i = 0; i < logic.getM(); i++) {
            g2.drawLine(offset + 0, offset + (int)(i*stepY), offset+mapAreaSize.width, offset + (int)(i*stepY));
        }
    }

    private void drawCoordinates(Graphics2D g2) {
        float stepX = (float)mapAreaSize.width/(logic.getK()-1);
        float stepY = (float)mapAreaSize.height/(logic.getM()-1);
        float argStepX = (logic.getB()-logic.getA())/(logic.getK()-1);
        float argStepY = (logic.getD()-logic.getC())/(logic.getM()-1);
        for (int i = 0; i < logic.getK(); i++) {
            g2.drawString(String.format("%.2f", logic.getA() + i*argStepX), offset - 8 + i*stepX-5, offset - 8);
        }
        for (int i = 0; i < logic.getM(); i++) {
            if (i > 0) { // avoid overpainting zero
                g2.drawString(String.format("%.2f", logic.getC() + i * argStepY), 0, offset + i*stepY);
            }
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

    private void drawIsolines (Graphics2D g2, float[] isolevels) {
        float a = logic.getA();
        float c = logic.getC();
        float argStepX = (logic.getB()-logic.getA())/(logic.getK()-1);
        float argStepY = (logic.getD()-logic.getC())/(logic.getM()-1);
        for (int j = 0; j < logic.getM()-1; j++) {
            for (int i = 0; i < logic.getK()-1; i++) {
                float f1 = logic.getZ()[i][j+1];
                float f2 = logic.getZ()[i+1][j+1];
                float f3 = logic.getZ()[i][j];
                float f4 = logic.getZ()[i+1][j];

                for (int k = 0; k < isolevels.length; k++) { // iterate over z levels
                    float z = isolevels[k];
                    // Find intersections
                    ArrayList<Point2D> intersections = new ArrayList<>();
                    if (f1 < z && z <= f2 || f2 < z && z <= f1) {
                        intersections.add(new Point2D.Float(a+i*argStepX + argStepX * Math.abs(z-f1)/Math.abs(f1-f2),
                                c+(j+1)*argStepY));
                    }
                    if (f4 < z && z <= f2 || f2 < z && z <= f4) {
                        intersections.add(new Point2D.Float(a+(i+1)*argStepX,
                                c+j*argStepY + argStepY * Math.abs(z-f4)/Math.abs(f4-f2)));
                    }
                    if (f3 < z && z <= f4 || f4 < z && z <= f3) {
                        intersections.add(new Point2D.Float(a+i*argStepX + argStepX * Math.abs(z-f3)/Math.abs(f4-f3),
                                c+j*argStepY));
                    }
                    if (f1 < z && z <= f3 || f3 < z && z <= f1) {
                        intersections.add(new Point2D.Float(a+i*argStepX,
                                c+j*argStepY + argStepY * Math.abs(z-f3)/Math.abs(f3-f1)));
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

    public Point2D imageToModel (Point2D imagePoint) {
        float modelX = logic.getA() + (float)imagePoint.getX()/mapAreaSize.width*(logic.getB() - logic.getA());
        float modelY = logic.getC() + (float)imagePoint.getY()/mapAreaSize.height*(logic.getD() - logic.getC());
        return new Point2D.Float(modelX, modelY);
    }

    public void update (Observable observable, Object object) {
        repaint();
    }
}
