package model;

import view.CameraScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;

/**
 * Created by Martin on 01.04.2016.
 */
public class Logic extends Observable {
    World w;
    Camera c;
    CameraScreen cs;

    public Logic () {
        try {
            //c = new Camera(null, 3,2,1, 3,0,2, 0,1,0);
            cs = new CameraScreen(this);
            c = new Camera(0,0,30, 0,0,0, 0,1,0);
            w = new World(c);

            float r = 1;
            w.addBody(new BoxBody(new Vec3f(-r,-r,-r), new Vec3f(r,r,r)));

            render();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void render () {
        cs.setUVLines(c.calcWires(w.getBodies()));
    }
    public void notifyObservers () {
        setChanged();
        super.notifyObservers();
    }

    //public void rightMouseMoved (float uvX, float uvY) {
//        //c.rotate(new Vec4f(uvX, uvY, 0, 1));
//        WiredBody b = w.getBody(activeBody);
//        if (b == null) return;
//        b.addRotation(uvX, uvY, 0);
//        render();
//    }
    public void leftMouseMoved (float uvX, float uvY) {
        c.rotate(new Vec4f(uvX, uvY, 0, 1));
        render();
    }
    public void wheelRotated (int m) {
        c.zoom(-m);
        render ();
    }
    public void ctrlWheelRotated (int m) {
        c.translate(new Vec3f(0,0,-1*m));
        render ();
    }
    public void keyPressed (char k) {
        switch (k) {
            case 'w':
                c.translate(new Vec3f(0,0,1));
                //c.rotate(new Vec4f(0, 0.1f, 0, 1));
                break;
            case 's':
                c.translate(new Vec3f(0,0,-1));
                //c.rotate(new Vec4f(0, -0.1f, 0, 1));
                break;
            case 'a':
                c.translate(new Vec3f(-1,0,0));
                //c.rotate(new Vec4f(-0.1f, 0, 0, 1));
                break;
            case 'd':
                c.translate(new Vec3f(1,0,0));
                //c.rotate(new Vec4f(0.1f, 0, 0, 1));
                break;
            case 'q':
                c.translate(new Vec3f(0,1,0));
                break;
            case 'z':
                c.translate(new Vec3f(0,-1,0));
                break;
        }
        render();
    }

    public void openFile (File f) throws FileNotFoundException {
//        FileInitializer fileInitializer = new FileInitializer(f);
//        GameSettings gs = fileInitializer.parseFile();
//        n = gs.n;
//        m = gs.m;
//        k = gs.k;
//        c.setM(gs.rotMat);
//        c.setZf(gs.zf);
//        c.setZb(gs.zb);
//        c.setSw(gs.sw);
//        c.setSh(gs.sh);
//        c.setZf(gs.zf);
//        cs.setBackgroundCol(gs.backgroundCol);
//        w.clear();
//        activeBody = 1;
//        for (RotationBody r : gs.bodies) {
//            w.addBody(r);
//        }
//        render();
    }
    public void saveFile (File f) throws FileNotFoundException, IOException {
//        PrintWriter w = new PrintWriter(new FileWriter(f));
//        w.print(n); w.print(' ');
//        w.print(m); w.print(' ');
//        w.print(k); w.print(' ');
//        w.print(0); w.print(' ');
//        w.print(0); w.print(' ');
//        w.print(0); w.print(' ');
//        w.print(0); w.print(' ');
//        w.println();
//        w.print(c.getZf()); w.print(' ');
//        w.print(c.getZb()); w.print(' ');
//        w.print(c.getSw()); w.print(' ');
//        w.print(c.getSh()); w.print(' ');
//        w.println();
//        float[][] m = c.getM();
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                w.print(m[i][j]); w.print(' ');
//            }
//            w.println();
//        }
//        w.print(cs.getBackgroundCol().getRed()); w.print(' ');
//        w.print(cs.getBackgroundCol().getGreen()); w.print(' ');
//        w.print(cs.getBackgroundCol().getBlue()); w.print(' ');
//        w.println();
//        w.print(this.w.getBodiesNum()-1);
//        w.println();
//        for (int nb = 1; nb <= this.w.getBodiesNum()-1; nb++) {
//            WiredBody b = this.w.getBody(nb);
//            w.print(b.getColor().getRed()); w.print(' ');
//            w.print(b.getColor().getGreen()); w.print(' ');
//            w.print(b.getColor().getBlue()); w.print(' ');
//            w.println();
//            w.print(b.getPosition().x); w.print(' ');
//            w.print(b.getPosition().y); w.print(' ');
//            w.print(b.getPosition().z); w.print(' ');
//            w.println();
//            m = b.getM();
//            for (int i = 0; i < 3; i++) {
//                for (int j = 0; j < 3; j++) {
//                    w.print(m[i][j]); w.print(' ');
//                }
//                w.println();
//            }
//            ArrayList<Point2D.Float> markers = ((RotationBody)b).getMarkers();
//            w.print(markers.size());
//            w.println();
//            for (Point2D.Float p : markers) {
//                w.print(p.x); w.print(' ');
//                w.print(p.y); w.println();
//            }
//        }
//        w.flush();
//        w.close();
    }

    public World getWorld () { return w; }
    public Camera getCamera () {
        return c;
    }
    public CameraScreen getCameraScreen () {
        return cs;
    }


}
