package model;

import view.BodySettingsWindow;

import java.awt.*;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Camera extends Body {
    Graphics2D g2;
    Vec4f target;
    Vec4f up;
    float[][] m = new float[4][4];

    public Camera (Graphics2D g2, float x, float y, float z, float tx, float ty, float tz, float ux, float uy, float uz) {
        super(x, y, z, 1, 0, 0, 0, 1);
        this.g2 = g2;
        target = new Vec4f(tx, ty, tz, 1);
        up = new Vec4f(ux, uy, uz, 1);
        calcM();
    }

    private void calcM () {
        Vec4f k = Vec4f.sub(transform.position, target).div(Vec4f.sub(target, transform.position).length());
        Vec4f i = Vec4f.cross(up, k);
        Vec4f j = Vec4f.cross(k, i);
        m[0][0] = i.x; m[0][1] = i.y; m[0][2] = i.z; m[0][3] = 0;
        m[1][0] = j.x; m[1][1] = j.y; m[1][2] = j.z; m[0][3] = 0;
        m[2][0] = k.x; m[2][1] = k.y; m[2][2] = k.z; m[0][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;
    }
    public float[][] getM () {
        return m;
    }

    public void renderWire (WiredBody wiredBody) {
        for (Segment s : wiredBody.getSegments()) {

        }
    }
}
