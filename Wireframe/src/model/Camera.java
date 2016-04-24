package model;

import view.CameraScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Camera extends Body {
    //CameraScreen cameraScreen;
    Vec4f target;
    Vec4f up;
    float[][] m = new float[4][4]; // mcam
    float[][] mInverse = new float[4][4];
    float[][] p = new float[4][4];
    float sw = 40;
    float sh = 30;
    float zf = 1;
    float zb = 20;

    public Camera (/*CameraScreen cameraScreen,*/ float x, float y, float z, float tx, float ty, float tz, float ux, float uy, float uz) {
        super(x, y, z, 1, 0, 0, 0, 1);
        //this.cameraScreen = cameraScreen;
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
        float[][] mtemp = new float[4][4];
        mtemp[0][0] = i.x; mtemp[0][1] = i.y; mtemp[0][2] = i.z; mtemp[0][3] = 0;
        mtemp[1][0] = j.x; mtemp[1][1] = j.y; mtemp[1][2] = j.z; mtemp[1][3] = 0;
        mtemp[2][0] = k.x; mtemp[2][1] = k.y; mtemp[2][2] = k.z; mtemp[2][3] = 0;
        mtemp[3][0] = 0; mtemp[3][1] = 0; mtemp[3][2] = 0; mtemp[3][3] = 1;
        float[][] t = new float[4][4];
        t[0][0] = 1; t[0][1] = 0; t[0][2] = 0; t[0][3] = -transform.position.x;
        t[1][0] = 0; t[1][1] = 1; t[1][2] = 0; t[1][3] = -transform.position.y;
        t[2][0] = 0; t[2][1] = 0; t[2][2] = 1; t[2][3] = -transform.position.z;
        t[3][0] = 0; t[3][1] = 0; t[3][2] = 0; t[3][3] = 1;
        m = mulMat(mtemp, t);
        //printMat(m);

        mtemp[0][0] = i.x; mtemp[0][1] = j.x; mtemp[0][2] = k.x; mtemp[0][3] = 0;
        mtemp[1][0] = i.y; mtemp[1][1] = j.y; mtemp[1][2] = k.y; mtemp[1][3] = 0;
        mtemp[2][0] = i.z; mtemp[2][1] = j.z; mtemp[2][2] = k.z; mtemp[2][3] = 0;
        mtemp[3][0] = 0; mtemp[3][1] = 0; mtemp[3][2] = 0; mtemp[3][3] = 1;
        t[0][3] = -t[0][3];
        t[1][3] = -t[1][3];
        t[2][3] = -t[2][3];
        mInverse = mulMat(t, mtemp);
        //printMat(mInverse);
    }
    public float[][] getMCam () {
        return m;
    }

    private void calcP () {
//        p[0][0] = 2*zf/sw; p[0][1] = 0; p[0][2] = 0; p[0][3] = 0;
//        p[1][0] = 0; p[1][1] = 2*zf/sh; p[1][2] = 0; p[1][3] = 0;
//        p[2][0] = 0; p[2][1] = 0; p[2][2] = zb/(zb-zf); p[2][3] = -zf*zb/(zb-zf);
//        p[3][0] = 0; p[3][1] = 0; p[3][2] = 1; p[3][3] = 0;

        p[0][0] = zf/sw; p[0][1] = 0; p[0][2] = 0; p[0][3] = 1f/2;
        p[1][0] = 0; p[1][1] = zf/sh; p[1][2] = 0; p[1][3] = 1f/2;
        p[2][0] = 0; p[2][1] = 0; p[2][2] = 1/(zb-zf); p[2][3] = -zf/(zb-zf);
        p[3][0] = 0; p[3][1] = 0; p[3][2] = 0; p[3][3] = 1;
        //printMat(p);
    }

    private ArrayList<UVLine>  renderWire (WiredBody wiredBody) {
        ArrayList<UVLine> res = new ArrayList<>();
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

            //System.out.println("In model space: ");
            //s.p1.print();
            //s.p2.print();
            //System.out.println("In world space: ");
            //inWorld1.print();
            //inWorld2.print();
            //System.out.println("In camera space: ");
            //inCameraSpace1.print();
            //inCameraSpace2.print();
            //System.out.println("In projection space: ");
            //proj1.print();
            //proj2.print();

            res.add(new UVLine(proj1.x, proj1.y, proj2.x, proj2.y, wiredBody.getColor(), null));
        }
        return res;
    }

    private ArrayList<UVLine>  renderXyz (WiredBody wiredBody) {
        ArrayList<UVLine> res = new ArrayList<>();
        float[][] w = wiredBody.getM();
        Color[] c = {Color.red, Color.green, Color.blue};
        String[] str = {"X", "Y", "Z"};
        int n = 0;
        for (Segment s : wiredBody.getXyz()) {
            Vec4f inWorld1 = Vec4f.mulMat(w, s.p1);
            Vec4f inWorld2 = Vec4f.mulMat(w, s.p2);

            Vec4f inCameraSpace1 = Vec4f.mulMat(m, inWorld1);
            Vec4f inCameraSpace2 = Vec4f.mulMat(m, inWorld2);

            Vec4f proj1 = Vec4f.mulMat(p, inCameraSpace1);
            //proj1.div(proj1.w);
            Vec4f proj2 = Vec4f.mulMat(p, inCameraSpace2);
            //proj2.div(proj2.w);

            res.add(new UVLine(proj1.x, proj1.y, proj2.x, proj2.y, c[n], str[n]));
            n++;
        }
        return res;
    }

    public ArrayList<UVLine> calcWires (ArrayList<WiredBody> bodies) {
        ArrayList<UVLine> res = new ArrayList<>();
        for (WiredBody w : bodies) {
            res.addAll(renderWire(w));
            res.addAll(renderXyz(w));
        }
        return res;
    }

    public void translate (Vec3f dir) {
        Vec4f dir4 = new Vec4f(dir, 1);
        //dir4.print();
        //System.out.print("BEFORE:");
        //dir4.z = 1;
        //transform.position.print();
        Vec4f oldPos = transform.position;
        transform.position = Vec4f.mulMat(mInverse, dir4);
        //System.out.print("AFTER:");
        transform.position.print();

        target = target.add(target, Vec4f.sub(transform.position, oldPos));
//        transform.position.x += dir.x;
//        transform.position.y += dir.y;
//        transform.position.z += dir.z;
//        transform.position = Vec4f.mulMat(m, transform.position);
//        System.out.print("NEW:");
//        transform.position.print();
        //System.out.println();
        calcM();
        //calcP();
    }

    public float getSw() {
        return sw;
    }

    public void setSw(float sw) {
        this.sw = sw;
    }

    public float getSh() {
        return sh;
    }

    public void setSh(float sh) {
        this.sh = sh;
    }

    public float getZf() {
        return zf;
    }

    public void setZf(float zf) {
        this.zf = zf;
    }

    public float getZb() {
        return zb;
    }

    public void setZb(float zb) {
        this.zb = zb;
    }
}
