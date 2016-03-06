package ru.nsu.fit.g13201.azhbakov.view.hex;

import ru.nsu.fit.g13201.azhbakov.game_of_life.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by marting422 on 11.02.2016.
 */
public class HexGrid extends JPanel implements Observer {
    Hex hex, deadHex;
    private int cols, rows;
    Game game;

    public HexGrid (final Game game) {
        super();
        setLayout(new FlowLayout());
        this.game = game;
        game.addObserver(this);
        setGridParameters(game.getWidth(), game.getHeight(), game.getCellSize(), game.getBorderWidth());
        AvoidXORGlitch.hexGrid = this;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                processMouse(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                AvoidXORGlitch.reset();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                processMouse(e);
            }
        });
    }

    private void processMouse (MouseEvent e) {
        int col = getHexCol(e.getX(), e.getY());
        int row = getHexRow(e.getX(), e.getY());
        if (row == -1 || col == -1) return;
        // inside hex coord:
        int localX = e.getX() - getImageX(row, col);
        int localY = e.getY() - getImageY(row, col);

        BelongToHex res = hex.insideHex(localX, localY);
        //System.out.println("Grid coord: " + row + " " + col);
        //System.out.println("Local coord: " + localX + " " + localY);
        //System.out.println("RES: " + res);
        int nextX;
        switch (res) {
            case RIGHT:
                nextX = col + (row & 1);
                break;
            case LEFT:
                nextX = col - (1 - (row & 1));
                break;
            case INSIDE:
                if (row >= rows || row < 0 || col >= cols - (row & 1)) return;
                switch (game.getClickMode()) {
                    case REPLACE:
                        AvoidXORGlitch.liven_cell(col, row);
                        return;
                    case XOR:
                        AvoidXORGlitch.switch_cell(col, row);
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
        // LEFT OR RIGHT
        if ((row == rows-1) || (nextX == cols - ((row+1) & 1))) return;
        res = hex.insideHex(Math.abs(getImageX(row+1, nextX) - e.getX()), Math.abs(getImageY(row+1,nextX) - e.getY()));
        //System.out.println("RES2: " + res);
        if (res == BelongToHex.INSIDE) {
            switch (game.getClickMode()) {
                case REPLACE:
                    AvoidXORGlitch.liven_cell(nextX, row+1);
                    return;
                case XOR:
                    AvoidXORGlitch.switch_cell(nextX, row+1);
                    return;
                default:
                    return;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.lightGray);
        g2.fillRect(0,0, getWidth(), getHeight());
        drawGrid(g2);
    }

    private void setGridParameters (int cols, int rows, int hexSide, int edge) {
        if (hex == null || hex.s != hexSide || hex.edge != edge || (this.cols != cols || this.rows != rows)) {
            HexGenerator.initGenerator(hexSide, edge);
            hex = HexGenerator.drawHex(new Color (0, 255, 110));
            deadHex = HexGenerator.drawHex(new Color(110,116,107));

            this.cols = cols;
            this.rows = rows;

            int panelWidth = hex.edge + (hex.h + hex.edge + 1) * cols;
            int panelHeight = (hex.cornerEdge + hex.s + hex.t) * rows + hex.t + hex.cornerEdge + (1 - hexSide & 1);
            setPreferredSize(new Dimension(panelWidth, panelHeight));
            revalidate();;
        }
    }

    public void drawGrid (Graphics2D g2) {
        setGridParameters(game.getWidth(), game.getHeight(), game.getCellSize(), game.getBorderWidth());
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols - (y & 1); x++) {
                drawHex(g2, x, y, game.isAlive(x, y));
            }
        }
    }

    private void drawHex (Graphics2D g2, final int col, final int row, boolean alive) { // called rarely, when sizes changed
        int centerX = getImageX(row, col);
        int centerY = getImageY(row, col);

        Hex h;
        if (alive) h = hex;
        else h = deadHex;
        g2.drawImage(h.image, centerX, centerY, null);

        // Show impact
        if (!game.getShowImpacts() || hex.s < 13) return;
        g2.setColor(Color.black);
        Font font = new Font("Serif", Font.BOLD, 16);
        g2.setFont(font);
        float impact = game.getImpact(col, row);
        if (impact == Math.round(impact)) {
            g2.drawString(String.format("%.0f", impact), centerX+hex.image.getWidth()/2-4, centerY+hex.image.getHeight()/2+8);
            //System.out.println(col + " " + row + " " + String.format("%.0f", impact) + " " + centerX + " " + centerY);
        } else {
            g2.drawString(String.format("%.1f", impact), centerX+hex.image.getWidth()/2-10, centerY+hex.image.getHeight()/2+8);
            //System.out.println(col + " " + row + " " + String.format("%.1f", impact) + " " + centerX + " " + centerY);
        }
    }


    public int getHexCol (int pixelX, int pixelY) {
        int overlap = hex.image.getWidth() - getImageX(0, 1);
        int step = hex.image.getWidth();
        int colPixel;
        int row = getHexRow(pixelX, pixelY);
        if ((row & 1) == 0) {
            colPixel = step;
        } else {
            colPixel = getImageX(1, 0) + step;
        }
        //System.out.println(overlap + " " + step);
        //System.out.print("Checking X " + pixelX + ": ");
        for (int i = 0; i < cols+1 - (row&1); i++) {
            //System.out.print(colPixel + " ");
            if (pixelX <= colPixel) {
                //System.out.println();
                return i;
            }
            colPixel = colPixel - overlap + step;
        }
        //System.out.println();
        return -1;
    }

    public int getHexRow (int pixelX, int pixelY) {
        int overlap = hex.t + hex.cornerEdge/* + (1 - hex.s & 1)*/;
        int step = hex.image.getHeight();
        int rowPixel = step;
        //System.out.println(overlap + " " + step);
        //System.out.print("Checking Y " + pixelY + ": ");
        for (int i = 0; i < rows; i++) {
            //System.out.print(rowPixel + " ");
            if (pixelY <= rowPixel) {
                //System.out.println();
                return i;
            }
            rowPixel = rowPixel - overlap + step;
        }
        //System.out.println();
        return -1;
    }

    private int getImageX (int row, int col) {
        int offset = hex.image.getWidth()/2 - hex.edge/2;
        return (row & 1) * offset + col * (hex.image.getWidth() - hex.edge);
    }
    private int getImageY(int row, int col) {
        //System.out.println(row + " " + col + " " + row * (hex.image.getWidth() - hex.cornerEdge));
        return row * (hex.image.getHeight() - (hex.t + hex.cornerEdge /*+ (1 - hex.s & 1)*/));
    }

    public void update (Observable observable, Object obj) {
            repaint();
    }

    // When we changing cell state we should check some things to avoid situation when
    // state changes on every mouse movement with XOR mode and LMB down
    static class AvoidXORGlitch {
        static HexGrid hexGrid;
        static int lastX;
        static int lastY;

        public static void liven_cell (int x, int y) {
            hexGrid.game.liven_cell(x,y);
            lastX = -1;
            lastY = -1;
        }
        public static void switch_cell (int x, int y) {
            if (x == lastX && y == lastY) {
                return;
            }
            hexGrid.game.switch_cell(x,y);
            lastX = x;
            lastY = y;
        }
        public static void reset () {
            lastX = -1;
            lastY = -1;
        }
    }
}
