package model;

import java.awt.*;

/**
 * Created by Martin on 09.05.2016.
 */
public class LightSource extends Body {

    Color c;

    public LightSource (float x, float y, float z, int r, int g, int b) {
        super(x,y,z,1, 0,0,0,1);
        c = new Color(r, g, b);
    }
    public LightSource (Vec3f pos, Color color) {
        super(new Vec4f(pos, 1), new Vec4f(0,0,0,1));
        c = color;
    }

    public Color getColor () {
        return c;
    }
    public void setColor (Color c) {
        this.c = c;
    }
}
