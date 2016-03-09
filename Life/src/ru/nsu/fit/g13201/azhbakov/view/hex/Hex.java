package ru.nsu.fit.g13201.azhbakov.view.hex;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by marting422 on 16.02.2016.
 */
public class Hex extends JComponent {
    public final int h, r, s, t, edge, cornerEdge;
    private final int centerX, centerY;
    public final BufferedImage image;

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
    }

    public BelongToHex insideHex (int pixelX, int pixelY) {
        boolean right;
        boolean up;

        if (pixelX > centerX) right = true;
        else right = false;
        if (pixelY > centerY) up = true;
        else up = false;

        // Mirror pixel to 1 quarter
        pixelX = (int) Math.abs(pixelX - centerX);
        if (up) pixelY = 2 * cornerEdge + s + 2 * t - pixelY;

        final float sqrt3 = (float) (Math.sqrt(3f));
        if (pixelY > cornerEdge + pixelX / sqrt3 && pixelX < r)
            return BelongToHex.INSIDE;

        if ((pixelX - edge / 2) / sqrt3 <= pixelY && pixelY <= cornerEdge + pixelX / sqrt3){
            return BelongToHex.EDGE;
        }

        if (pixelY < (pixelX - edge/2)/sqrt3)
            if (right && up)
                return BelongToHex.RIGHT;
            else if (up)
                return BelongToHex.LEFT;

        return BelongToHex.EDGE;
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
