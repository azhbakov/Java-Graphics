package view;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by marting422 on 15.02.2016.
 */
public class HexGenerator {
    //BufferedImage image;
    //BufferedImage dead;
    private int h, r, s, t, edge, cornerEdge;
    private int imageWidth;
    private int imageHeight;

    public HexGenerator(int hexSide, int edge) {
        this.edge = edge;
        setSide(hexSide);

        // find edge at corner
        cornerEdge = getHexVertexY(0, 0);
        setHeight(h + 2 * edge);
        cornerEdge -= getHexVertexY(0, 0);
        setSide(hexSide);

        imageWidth = edge + (h+edge+1);
        imageHeight = (cornerEdge + s+t) + t+cornerEdge + (1-hexSide&1);
        //setSize(imageWidth,imageHeight);
        //image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        //dead = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        //fillCanvas(Color.pink);

        //drawHex(image, edge+r, cornerEdge+s/2+t, Color.red);
        //drawHex(dead, edge+r, cornerEdge+s/2+t, Color.white);
    }

    /*public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, null, null);
        //g2.drawLine(0,0,100,100);
    }*/

    // Change hex prefab parameters of the grid
    public void setHeight (int height) {
        h = height;
        r = (int)Math.ceil((float)h/2);
        t = (int) (r * Math.tan(Math.PI/6));
        s = (int) Math.ceil(((float)r / Math.cos(Math.PI/6)));
    }

    // Change hex prefab parameters of the grid
    public void setSide (int side) {
        s = side;
        r = (int)(s * Math.cos(Math.PI/6));
        t = (int) (r * Math.tan(Math.PI/6));
        h = 2*r;
    }

    public Hex drawHex (Color hexColor) {
        int centerX = edge+r;
        int centerY = cornerEdge+s/2+t;
        BufferedImage hexImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        // Init Bresenham movement along up right edge
        XBresenhemIterator brInner = new XBresenhemIterator(getHexVertexX(centerX, 0), getHexVertexY(centerY, 0),
                getHexVertexX(centerX, 1), getHexVertexY(centerY, 1)); // go along inner up right edge
        int side = s;
        setHeight(h + 2*edge);
        XBresenhemIterator brOuter = new XBresenhemIterator(getHexVertexX(centerX, 0), getHexVertexY(centerY, 0),
                getHexVertexX(centerX, 1), getHexVertexY(centerY, 1)); // go along outer up right edge
        setSide (side);

        int innerEdgeY, outerEdgeY;
        innerEdgeY = centerY - brInner.getY();
        outerEdgeY = centerY - brOuter.getY();
        for (int x = 0; x <= r + edge; x++) {
            // Fill vertical line inside
            for (int y = 0; y <= innerEdgeY; y++) {
                setPixelInHex(hexImage, centerX, centerY, x, y, hexColor);
            }
            // Fill vertical line in edge
            if (innerEdgeY == outerEdgeY) setPixelInHex(hexImage, centerX, centerY, x, innerEdgeY, Color.black);
            for (int y = innerEdgeY+1; y <= outerEdgeY; y++) {
                setPixelInHex(hexImage, centerX, centerY, x, y, Color.black);
            }

            // Calc Y coordinates of next inner and outer edge
            if (brInner.isDone())
                innerEdgeY = -1;
            else
                innerEdgeY = centerY - brInner.getY();
            outerEdgeY = centerY - brOuter.getY();

            brInner.step();
            brOuter.step();
        }
        return new Hex(hexImage, h, r, s, t, edge, cornerEdge);
    }

    public void fillCanvas(BufferedImage image, Color c) {
        int color = c.getRGB();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setRGB(x, y, color);
            }
        }
        //repaint();
    }


    // Works only with x2-x1>y2-y1, positive dir, line goes right and down, stretched horizontally
    // Moves along line on demand according to Bresenham algorithm
    class XBresenhemIterator {
        int x1, y1, x2, y2;
        int x, y;
        int deltax, deltay, error, deltaerr;
        boolean done = false;

        XBresenhemIterator(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            x = x1;
            y = y1;

            done = false;
            deltax = (int)Math.abs(x2 - x1);
            deltay = (int)Math.abs(y2 - y1);
            error = 0;
            deltaerr = deltay;
        }

        public void step () {
            if (done) return;
            x++;
            error = error + deltaerr;
            if (2 * error >= deltax) {
                y = y + 1;
                error = error - deltax;
            }
            if (x == x2) done = true;
        }

        public int getX () {
            return x;
        }

        public int getY () {
            return y;
        }

        public boolean isDone () {
            return done;
        }
    }

    private void setPixelInHex (BufferedImage im, int centerX, int centerY, int dx, int dy, Color c) {
        // fills 4 pixels in one time (from 1st quarter to whole hex)
        im.setRGB(centerX+dx, centerY+dy, c.getRGB());
        im.setRGB(centerX+dx, centerY-dy, c.getRGB());
        im.setRGB(centerX-dx, centerY+dy, c.getRGB());
        im.setRGB(centerX-dx, centerY-dy, c.getRGB());
    }

    // Find X coordinate of vertex using hex center coordinates
    public int getHexVertexX (int centerX, int vertexNum) {
        switch (vertexNum) {
            case 0:
            case 3: return centerX;
            case 1:
            case 2: return centerX + r;
            case 4:
            case 5: return centerX - r;
            default: return -1;
        }
    }

    // Find Y coordinate of vertex using hex center coordinates
    public int getHexVertexY (int centerY, int vertexNum) {
        switch (vertexNum) {
            case 0: return centerY - s/2-t;
            case 1:
            case 5: return centerY - s/2;
            case 2:
            case 4: return centerY + s/2;
            case 3: return centerY + s/2+t;
            default: return -1;
        }
    }
}
