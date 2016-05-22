package model;

import view.AppWindow;
import view.CameraScreen;
import view.FileUtils;
import view.ProgressBar;

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
    boolean render = false;

    public Logic () {
        try {
            //c = new Camera(null, 3,2,1, 3,0,2, 0,1,0);
            cs = new CameraScreen(this);
            c = new Camera(0,0, 30, 0,0,0, 0,1,0);
            w = new World(c);

            float r = 1;
            w.addBody(new BoxBody(new Vec3f(-20,-6,-20), new Vec3f(20,-5,20), 0.4f,0.4f,0.4f, 0.4f,0.4f,0.4f, 1));
            //w.addBody(new BoxBody(new Vec3f(-r,-r,-r), new Vec3f(r,r,r), 0.4f,0.4f,0.4f, 0.1f,0.1f,0.1f, 1));
            w.addBody(new BoxBody(new Vec3f(-8,-5,-8), new Vec3f(-3,1,-4), 1f,1f,1f, 0.4f,0.4f,0.4f, 1));
            w.addBody(new LightSource(1.5f, 1, 3, 100, 100, 100));
            w.addBody(new LightSource(3, 5, -9, 255, 0, 0));
            w.addBody(new SphereBody(new Vec3f(0,0,0), 3, 1f,1f,1f, 1f,1f,1f, 100));
            //w.addBody(new TriangleBody(new Vec3f(5,0,0), new Vec3f(5,0,5), new Vec3f(0,0,5), 1f,1f,1f, 1f,1f,1f, 1));
            //w.addBody(new TriangleBody(new Vec3f(-5,0,-5), new Vec3f(5,0,-5), new Vec3f(5,2,5), 1f,1f,1f, 1f,1f,1f, 1));
            w.addBody(new QuadrangleBody(new Vec3f(-2,1,0), new Vec3f(2,1,0), new Vec3f(2,0,3), new Vec3f(-2,0,3), 1f,1f,1f, 1f,1f,1f, 1));

            AppWindow appWindow = new AppWindow(this);
            render();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void render () {
        if (render) {
            //ArrayList<ScreenPoint> res;// = c.calcLighting(w.getBodies(), w.getLigths(), cs.getWidth(), cs.getHeight());
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    cs.setScreenPoints(c.calcLighting(w.getBodies(), w.getLigths(), cs.getWidth(), cs.getHeight(), new ProgressBar()));
                }
            });
            t.start();
            //cs.setScreenPoints(res);
        } else {
            cs.setScreenPoints(null);
        }
        cs.setUVLines(c.calcWires(w.getBodies()));
    }
    public void notifyObservers () {
        setChanged();
        super.notifyObservers();
    }

    public void setRender (boolean value) {
        render = value;
        render();
    }
    public boolean isRendering () {
        return render;
    }

    //public void rightMouseMoved (float uvX, float uvY) {
//        //c.rotate(new Vec4f(uvX, uvY, 0, 1));
//        WiredBody b = w.getBody(activeBody);
//        if (b == null) return;
//        b.addRotation(uvX, uvY, 0);
//        render();
//    }
    public void leftMouseMoved (float uvX, float uvY) {
        if (isRendering()) return;
        c.rotate(new Vec4f(uvX, uvY, 0, 1));
        render();
    }
    public void wheelRotated (int m) {
        if (isRendering()) return;
        c.zoom(-m);
        render ();
    }
    public void ctrlWheelRotated (int m) {
        if (isRendering()) return;
        c.translate(new Vec3f(0,0,-1*m));
        render ();
    }
    public void keyPressed (char k) {
        if (isRendering()) return;
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

    public void openFile (File f) throws FileNotFoundException, NoSuchElementException {
        String name = f.getName();
        //System.out.println(name);
        String ext = name.substring(name.lastIndexOf('.')+1);
        //System.out.println(ext);
        switch (ext) {
            case "scene":
                openSceneFile(f);
                break;
            default:
                throw new NoSuchElementException();
        }
    }

    public void openSceneFile (File file) throws FileNotFoundException, NoSuchElementException {
        FileInitializer f = new FileInitializer(file);
        Scanner s;
        // Read header
        Vec3f ambientColor;
        int nl;
        s = new Scanner(f.getLine());
        ambientColor = new Vec3f(s.nextInt(), s.nextInt(), s.nextInt());
        s = new Scanner(f.getLine());
        nl = s.nextInt();
        Vec3f[] lightsPos = new Vec3f[nl];
        Color[] lightsCol = new Color[nl];
        for (int i = 0; i < nl; i++) {
            s = new Scanner(f.getLine());
            lightsPos[i] = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
            lightsCol[i] = new Color(s.nextInt(), s.nextInt(), s.nextInt());
            System.out.println(lightsCol[i].getRed()+" "+lightsCol[i].getGreen()+" "+lightsCol[i].getBlue());
        }
        // Read scene
        ArrayList<OpticalBody> bodies = new ArrayList<>();
        String str;
        while ((str = f.getLine()) != null) {
            s = new Scanner(str);
            String type = s.next();
            System.out.println("LINE: " +str);
            switch (type) {
                case "SPHERE":
                    s = new Scanner(f.getLine());
                    Vec3f center = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    float radius = s.nextFloat();
                    s = new Scanner(f.getLine());
                    Vec3f kd = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    Vec3f ks = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    float power = s.nextFloat();
                    bodies.add(new SphereBody(center, radius, kd.x, kd.y, kd.z, ks.x, ks.y, ks.z, power));
                    System.out.println(kd.x+" "+ kd.y+" "+ kd.z+" "+ks.x+" "+ ks.y+" "+ ks.z);
                    break;
                case "BOX":
                    s = new Scanner(f.getLine());
                    Vec3f min = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    Vec3f max = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    kd = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    ks = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    power = s.nextFloat();
                    bodies.add(new BoxBody(min, max, kd.x, kd.y, kd.z, ks.x, ks.y, ks.z, power));
                    System.out.println(kd.x+" "+ kd.y+" "+ kd.z+" "+ks.x+" "+ ks.y+" "+ ks.z);
                    break;
                case "TRIANGLE":
                    s = new Scanner(f.getLine());
                    Vec3f p1 = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    Vec3f p2 = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    Vec3f p3 = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    kd = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    ks = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    power = s.nextFloat();
                    bodies.add(new TriangleBody(p1, p2, p3, kd.x, kd.y, kd.z, ks.x, ks.y, ks.z, power));
                    System.out.println(kd.x+" "+ kd.y+" "+ kd.z+" "+ks.x+" "+ ks.y+" "+ ks.z);
                    break;
                case "QUADRANGLE":
                    s = new Scanner(f.getLine());
                    p1 = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    p2 = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    p3 = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    Vec3f p4 = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    s = new Scanner(f.getLine());
                    kd = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    ks = new Vec3f(s.nextFloat(), s.nextFloat(), s.nextFloat());
                    power = s.nextFloat();
                    bodies.add(new QuadrangleBody(p1, p2, p3, p4, kd.x, kd.y, kd.z, ks.x, ks.y, ks.z, power));
                    System.out.println(kd.x+" "+ kd.y+" "+ kd.z+" "+ks.x+" "+ ks.y+" "+ ks.z);
                    break;
                default:
                    throw new NoSuchElementException();
            }
        }
        // Init
        w.clear();
        c.ambientColor = ambientColor.div(255);
        for (int i = 0; i < nl; i++) {
            w.addBody(new LightSource(lightsPos[i], lightsCol[i]));
            //System.out.println(lightsCol[i].getRed()+" "+lightsCol[i].getGreen()+" "+lightsCol[i].getBlue());
        }
        for (OpticalBody b : bodies) {
            w.addBody(b);
        }
        render();
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
