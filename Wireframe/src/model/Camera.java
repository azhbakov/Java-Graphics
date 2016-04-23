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
    float[][] m = new float[4][4]; // mcam
    float[][] p = new float[4][4];
    float sw = 4;
    float sh = 3;
    float zf = 0.1f;
    float zb = 10;

    public Camera (Graphics2D g2, float x, float y, float z, float tx, float ty, float tz, float ux, float uy, float uz) {
        super(x, y, z, 1, 0, 0, 0, 1);
        this.g2 = g2;
        target = new Vec4f(tx, ty, tz, 1);
        up = new Vec4f(ux, uy, uz, 1);
        calcM();
        calcP();
    }

    private void calcM () {
        Vec4f k = Vec4f.sub(target, transform.position).div(Vec4f.sub(target, transform.position).length()); // todo check
//        System.out.println("k:");
//        k.print();
        Vec4f i = Vec4f.cross(up, k);
//        System.out.println("I:");
//        i.print();
        i = i.div(i.length());
//        System.out.println("i:");
//        i.print();
        Vec4f j = Vec4f.cross(k, i);
//        System.out.println("j:");
//        j.print();

        m[0][0] = i.x; m[0][1] = i.y; m[0][2] = i.z; m[0][3] = 0;
        m[1][0] = j.x; m[1][1] = j.y; m[1][2] = j.z; m[1][3] = 0;
        m[2][0] = k.x; m[2][1] = k.y; m[2][2] = k.z; m[2][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        float[][] t = new float[4][4];
        t[0][0] = 1; t[0][1] = 0; t[0][2] = 0; t[0][3] = -transform.position.x;
        t[1][0] = 0; t[1][1] = 1; t[1][2] = 0; t[1][3] = -transform.position.y;
        t[2][0] = 0; t[2][1] = 0; t[2][2] = 1; t[2][3] = -transform.position.z;
        t[3][0] = 0; t[3][1] = 0; t[3][2] = 0; t[3][3] = 1;

        m = mulMat(m, t);
        //printMat(m);
    }
    public float[][] getMCam () {
        return m;
    }

    private void calcP () {
        p[0][0] = 2*zf/sw; p[0][1] = 0; p[0][2] = 0; p[0][3] = 0;
        p[1][0] = 0; p[1][1] = 2*zf/sh; p[1][2] = 0; p[1][3] = 0;
        p[2][0] = 0; p[2][1] = 0; p[2][2] = zb/(zb-zf); p[2][3] = -zf*zb/(zb-zf);
        p[3][0] = 0; p[3][1] = 0; p[3][2] = 1; p[3][3] = 0;
        printMat(p);
    }

    public void renderWire (WiredBody wiredBody) {
        float[][] w = wiredBody.getM();
        for (Segment s : wiredBody.getSegments()) {
            Vec4f inWorld1 = Vec4f.mulMat(w, s.p1);
            Vec4f inWorld2 = Vec4f.mulMat(w, s.p2);

            Vec4f inCameraSpace1 = Vec4f.mulMat(m, inWorld1);
            Vec4f inCameraSpace2 = Vec4f.mulMat(m, inWorld2);

            Vec4f proj1 = Vec4f.mulMat(p, inCameraSpace1);
            //proj1.div(proj1.w);
            Vec4f proj2 = Vec4f.mulMat(p, inCameraSpace2);
            //proj2.div(proj2.w);

            System.out.println("In model space: ");
            s.p1.print();
            s.p2.print();
            System.out.println("In world space: ");
            inWorld1.print();
            inWorld2.print();
            System.out.println("In camera space: ");
            inCameraSpace1.print();
            inCameraSpace2.print();
            System.out.println("In projection space: ");
            proj1.print();
            proj2.print();
        }
    }
}
