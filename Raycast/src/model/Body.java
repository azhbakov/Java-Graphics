package model;

import java.util.ArrayList;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Body {
    Transform transform;
    ArrayList<Segment> xyz;
    float[][] m;

    public Body (float x, float y, float z, float w, float rx, float ry, float rz, float rw) {
        rx = rx/180 * (float) Math.PI;
        ry = ry/180 * (float) Math.PI;
        rz = rz/180 * (float) Math.PI;
        transform = new Transform(new Vec4f(x, y, z, w), new Vec4f(rx, ry, rz, rw));
        //m = new float[4][4];
        calcM();
        xyz = new ArrayList<>();
        xyz.add(new Segment(0,0,0,1, 1,0,0,1));
        xyz.add(new Segment(0,0,0,1, 0,1,0,1));
        xyz.add(new Segment(0,0,0,1, 0,0,1,1));
    }
    public Body (Vec4f pos, Vec4f rot) {
        rot.x = rot.x/180 * (float) Math.PI;
        rot.y = rot.y/180 * (float) Math.PI;
        rot.z = rot.z/180 * (float) Math.PI;
        transform = new Transform(pos, rot);
    }

    public void setPosition (float x, float y, float z) {
        transform.position.x = x;
        transform.position.y = y;
        transform.position.z = z;
        calcM();
    }
    public void setPosition (float x, float y, float z, float w) {
        transform.position.x = x;
        transform.position.y = y;
        transform.position.z = z;
        transform.position.w = w;
        calcM();
    }
    public void setPosition(Vec4f pos) {
        transform.position = pos;
        calcM();
    }
    public Vec4f getPosition () {
        return transform.position;
    }

    public void setRotation (float rx, float ry, float rz) {
        transform.rotation.x = rx;
        transform.rotation.y = ry;
        transform.rotation.z = rz;
        calcM();
    }
    public void setRotation (float rx, float ry, float rz, float rw) {
        transform.rotation.x = rx;
        transform.rotation.y = ry;
        transform.rotation.z = rz;
        transform.rotation.w = rw;
        calcM();
    }
    public void setRotation (Vec4f rot) {
        rot.x = rot.x/180 * (float) Math.PI;
        rot.y = rot.y/180 * (float) Math.PI;
        rot.z = rot.z/180 * (float) Math.PI;
        transform.rotation = rot;
        calcM();
    }
    public Vec4f getRotation () {
        return transform.rotation;
    }

    public void translate (Vec3f dir) {
//        float[][] m = new float[4][4];
//        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = dir.x;
//        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = dir.y;
//        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = dir.z;
//        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;
        m[0][3] = dir.x;
        m[1][3] = dir.y;
        m[2][3] = dir.z;
        transform.position = Vec4f.mulMat(m, transform.position);
        calcM();
        //transform.position = Vec4f.mulMat(m, transform.position);
        //transform.position.x += dir.x; transform.position.y += dir.y; transform.position.z += dir.z;
    }
    public void rotate (float rx, float ry, float rz) {
        transform.rotation.x += rx;
        transform.rotation.y += ry;
        transform.rotation.z += rz;
        calcM();
    }

    private void calcM () {
        /*float a = transform.rotation.z;
        float b = transform.rotation.y;
        float y = transform.rotation.x;
        //float[][] m = new float[4][4];
        m[0][0] = (float)(Math.cos(a)*Math.cos(y) - Math.sin(a)*Math.cos(b)*Math.sin(y));
        m[0][1] = (float)(-Math.cos(a)*Math.sin(y) - Math.sin(a)*Math.cos(b)*Math.cos(y));
        m[0][2] = (float)(Math.sin(a)*Math.sin(b));
        m[0][3] = transform.position.x;

        m[1][0] = (float)(Math.sin(a)*Math.cos(y) + Math.cos(a)*Math.cos(b)*Math.sin(y));
        m[1][1] = (float)(-Math.sin(a)*Math.sin(y) + Math.cos(a)*Math.cos(b)*Math.cos(y));
        m[1][2] = (float)(-Math.cos(a)*Math.sin(b));
        m[1][3] = transform.position.y;

        m[2][0] = (float)(Math.sin(b)*Math.sin(y));
        m[2][1] = (float)(Math.sin(b)*Math.cos(y));
        m[2][2] = (float)(Math.cos(b));
        m[2][3] = transform.position.z;

        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;*/
        float a = transform.rotation.x;
        float[][] mx = new float[4][4];
        mx[0][0] = 1; mx[0][1] = 0; mx[0][2] = 0; mx[0][3] = 0;
        mx[1][0] = 0; mx[1][1] = (float) Math.cos(a); mx[1][2] = -(float) Math.sin(a); mx[1][3] = 0;
        mx[2][0] = 0; mx[2][1] = (float) Math.sin(a); mx[2][2] = (float) Math.cos(a); mx[2][3] = 0;
        mx[3][0] = 0; mx[3][1] = 0; mx[3][2] = 0; mx[3][3] = 1;

        a = transform.rotation.y;
        float[][] my = new float[4][4];
        my[0][0] = (float) Math.cos(a); my[0][1] = 0; my[0][2] = (float) Math.sin(a); my[0][3] = 0;
        my[1][0] = 0; my[1][1] = 1; my[1][2] = 0; my[1][3] = 0;
        my[2][0] = -(float) Math.sin(a); my[2][1] = 0; my[2][2] = (float) Math.cos(a); my[2][3] = 0;
        my[3][0] = 0; my[3][1] = 0; my[3][2] = 0; my[3][3] = 1;

        a = transform.rotation.z;
        float[][] mz = new float[4][4];
        mz[0][0] = (float) Math.cos(a); mz[0][1] = -(float) Math.sin(a); mz[0][2] = 0; mz[0][3] = 0;
        mz[1][0] = (float) Math.sin(a); mz[1][1] = (float) Math.cos(a); mz[1][2] = 0; mz[1][3] = 0;
        mz[2][0] = 0; mz[2][1] = 0; mz[2][2] = 1; mz[2][3] = 0;
        mz[3][0] = 0; mz[3][1] = 0; mz[3][2] = 0; mz[3][3] = 1;

        float[][] yx = mulMat(my, mx);
        float[][] zyx = mulMat(mz, yx);
        zyx[0][3] = transform.position.x;
        zyx[1][3] = transform.position.y;
        zyx[2][3] = transform.position.z;
        //printMat(zyx);
        m = zyx;
    }
    public float[][] getM () {
        return m;
    }
    public ArrayList<Segment> getXyz () {
        return xyz;
    }

    public static void printMat (float[][] m) {
        System.out.println("Matrix:");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static float[][] mulMat (float[][] l, float[][] r) {
        float[][] res = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                res[i][j] = l[i][0]*r[0][j] + l[i][1]*r[1][j] + l[i][2]*r[2][j] + l[i][3]*r[3][j];
            }
        }
        return res;
    }

    public void setM (float[][] m) {
        this.m = m;
    }
}
