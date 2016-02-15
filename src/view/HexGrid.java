package view;

import game_of_life.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Created by marting422 on 11.02.2016.
 */
public class HexGrid extends JPanel{
    //private BufferedImage canvas;
    HexGenerator hexGenerator;
    Hex aliveHex, deadHex;
    //private int h, r, s, t, edge, cornerEdge;
    private int cols, rows;
    int stepX, stepY;
    JLabel[][] grid;
    Game game;

    public HexGrid (Game game, int cols, int rows, int hexSide, int edge) {
        //this.edge = edge;
        this.game = game;
        this.cols = cols;
        this.rows = rows;
        hexGenerator = new HexGenerator(hexSide, edge);
        aliveHex = hexGenerator.drawHex(Color.pink);
        deadHex = hexGenerator.drawHex(Color.cyan);

        stepX = deadHex.h + deadHex.edge+1;//(r+edge)*2;
        stepY = deadHex.s+ deadHex.t + deadHex.cornerEdge;

        grid = new JLabel[cols][rows];

        int panelWidth = deadHex.edge + (deadHex.h+ deadHex.edge+1) * cols;
        int panelHeight = (deadHex.cornerEdge + deadHex.s+ deadHex.t) * rows + deadHex.t+ deadHex.cornerEdge + (1-hexSide&1);
        //canvas = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        setSize(panelWidth, panelHeight);
        setLayout(null);
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
    }

    public void drawGrid (int cols, int rows, Color hexColor) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols - (y & 1); x++) {
                drawHex(x, y, hexColor);
            }
        }
    }

    private void drawHex (final int col, final int row, Color hexColor) {
        int centerX = getCenterX(col, row);
        int centerY = getCenterY(col, row);

        final JLabel l = new JLabel(new ImageIcon(deadHex.image));
        grid[col][row] = l;
        add(l);
        l.setSize(deadHex.image.getWidth(), deadHex.image.getHeight());
        l.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                getHexColumn(e.getX(), e.getY());
                game.switch_cell(col, row);
                //l.setIcon(new ImageIcon(deadHex.image));
            }

            public void mousePressed(MouseEvent e) {

            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });
        l.setBounds(centerX-l.getWidth()/2, centerY-l.getHeight()/2, l.getWidth(), l.getHeight());
    }

    public void livenHex (int col, int row) {
        if (grid[col][row] == null) return;
        grid[col][row].setIcon(new ImageIcon(aliveHex.image));
        //drawHex(getCenterX(col,row), getCenterY(col, row), hexColor);
    }

    public void killHex (int col, int row) {
        if (grid[col][row] == null) return;
        grid[col][row].setIcon(new ImageIcon(deadHex.image));
        //drawHex(getCenterX(col,row), getCenterY(col, row), hexColor);
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
        System.out.println(pixelX);
        return pixelX;
    }

    // Find X coordinate of hex center by its grid position
    private int getCenterX (int col, int row) {
        int startX;
        if ((row & 1) == 0) {
            startX = deadHex.edge + deadHex.r;
        } else {
            startX = deadHex.edge + deadHex.r + deadHex.r + deadHex.edge/2+1;
        }
        return startX + col * stepX;
    }

    // Find Y coordinate of hex center by its grid position
    private int getCenterY (int col, int row) {
        return deadHex.s/2+ deadHex.t+ deadHex.cornerEdge + row * stepY;
    }
}
