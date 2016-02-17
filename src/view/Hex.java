package view;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by marting422 on 16.02.2016.
 */
public class Hex extends JComponent {
    public final int h, r, s, t, edge, cornerEdge;
    private final int centerX, centerY;
    public BufferedImage image;
    public ImageIcon icon;

    private Hex () {
        this.h = 0;
        this.r = 0;
        this.s = 0;
        this.t = 0;
        this.edge = 0;
        this.cornerEdge = 0;
        centerX = 0;
        centerY = 0;
    }
    public Hex (BufferedImage bi, int h, int r, int s, int t, int edge, int cornerEdge) {
        this.h = h;
        this.r = r;
        this.s = s;
        this.t = t;
        this.edge = edge;
        this.cornerEdge = cornerEdge;
        centerX = edge+r;
        centerY = cornerEdge+s/2+t;
        image = bi;
        icon = new ImageIcon(bi);
    }

    public BelongToHex insideHex (int pixelX, int pixelY) {
        boolean right;
        boolean up;
        //System.out.println("\nRaw: "+pixelX +" "+ pixelY);
        //System.out.println("Center: "+centerX +" "+ centerY);
        if (pixelX > centerX) right = true;
        else right = false;
        if (pixelY > centerY) up = true;
        else up = false;
        //System.out.println("Right: "+right);
        //System.out.println("Up: "+up);

        // Mirror pixel
        pixelX = (int)Math.abs(pixelX - centerX);
        if (up) pixelY = 2*centerY - pixelY;

        final float sqrt3 = (float)(Math.sqrt(3f));
        if (pixelY > cornerEdge + pixelX/sqrt3 && pixelX < r)
            return BelongToHex.INSIDE;

        if (pixelX/sqrt3 <= pixelY && pixelY < cornerEdge + pixelX/sqrt3)
            return BelongToHex.EDGE;

        if (pixelY < pixelX/sqrt3)
            if (right)
                return BelongToHex.RIGHT;
            else
                return BelongToHex.LEFT;

        return BelongToHex.EDGE;
        //System.out.println("Pixel X = " + pixelX + ", r = " + r);
        //System.out.println("Pixel Y = " + pixelY + ", edge = " + (cornerEdge + pixelX/sqrt3));
        //System.out.println("Res: " + res);
        //return res; // TODO
    }

    // Find X coordinate of vertex using hex center coordinates
    public int getHexVertexX (int vertexNum) {
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
    public int getHexVertexY (int vertexNum) {
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
