package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Created by marting422 on 11.02.2016.
 */
public class HexGrid extends JPanel{
    //private BufferedImage canvas;
    HexGenerator hexGenerator;
    //private int h, r, s, t, edge, cornerEdge;
    private int cols, rows;
    int stepX, stepY;

    public HexGrid (int cols, int rows, int hexSide, int edge) {
        //this.edge = edge;
        this.cols = cols;
        this.rows = rows;
        hexGenerator = new HexGenerator(hexSide, edge);

        stepX = hexGenerator.h + hexGenerator.edge+1;//(r+edge)*2;
        stepY = hexGenerator.s+ hexGenerator.t + hexGenerator.cornerEdge;

        int canvasWidth = hexGenerator.edge + (hexGenerator.h+ hexGenerator.edge+1) * cols;
        int canvasHeight = (hexGenerator.cornerEdge + hexGenerator.s+ hexGenerator.t) * rows + hexGenerator.t+ hexGenerator.cornerEdge + (1-hexSide&1);
        //canvas = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        setSize(canvasWidth, canvasHeight);
        //fillCanvas(Color.BLUE);
        drawGrid(cols, rows, Color.cyan);
        //drawHex(getCenterX(0,0), getCenterY(0, 0), Color.blue);
    }

    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //hex.repaint();
        //g2.drawImage(canvas, null, null);
    }

    public void drawGrid (int cols, int rows, Color hexColor) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols - (y & 1); x++) {
                drawHex(getCenterX(x, y), getCenterY(x, y), hexColor);
            }
        }
    }

    private void drawHex (int centerX, int centerY, Color hexColor) {
        //BufferedImage bi = deepCopy(hex.canvas);
        setLayout(null);
        JLabel l = new JLabel(new ImageIcon(hexGenerator.canvas));
        add(l);
        l.setSize(hexGenerator.canvas.getWidth(), hexGenerator.canvas.getHeight());
        l.setBounds(centerX-l.getWidth()/2, centerY-l.getHeight()/2, l.getWidth(), l.getHeight());
    }

    public void colorHex (int col, int row, Color hexColor) {
        drawHex(getCenterX(col,row), getCenterY(col, row), hexColor);
    }


    /*public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }*/

    public int getHexColumn (int pixelX, int pixelY) {
        return 0;
    }

    // Find X coordinate of hex center by its grid position
    private int getCenterX (int col, int row) {
        int startX;
        if ((row & 1) == 0) {
            startX = hexGenerator.edge + hexGenerator.r;
        } else {
            startX = hexGenerator.edge + hexGenerator.r + hexGenerator.r + hexGenerator.edge/2+1;
        }
        return startX + col * stepX;
    }

    // Find Y coordinate of hex center by its grid position
    private int getCenterY (int col, int row) {
        return hexGenerator.s/2+ hexGenerator.t+ hexGenerator.cornerEdge + row * stepY;
    }
}
