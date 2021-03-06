package model;

import view.ProgressBar;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Camera extends Body {
    //CameraScreen cameraScreen;
    Vec4f target;
    Vec4f up;
    float[][] m = new float[4][4]; // use this to display world coordinates to the model coordinates
    float[][] mInverse = new float[4][4]; // use this to display model coordinates to world coordinates
    float[][] p = new float[4][4];
    float sw = 40;
    float sh = 30;
    float zf = 20;
    float zb = 50;
    public int rayMax = 2;
    public Vec3f ambientColor = new Vec3f(0.1f, 0.1f, 0.1f);
    public float gamma = 1f;
    public int quality = 2;

    public Camera (float x, float y, float z, float tx, float ty, float tz, float ux, float uy, float uz) {
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

//        Vec4f minusEye = new Vec4f(-transform.position.x, -transform.position.y, -transform.position.z, 1);
//        mtemp[0][0] = i.x; mtemp[0][1] = i.y; mtemp[0][2] = i.z; mtemp[0][3] = Vec4f.dot(i, minusEye);
//        mtemp[1][0] = j.x; mtemp[1][1] = j.y; mtemp[1][2] = j.z; mtemp[1][3] = Vec4f.dot(j, minusEye);
//        mtemp[2][0] = k.x; mtemp[2][1] = k.y; mtemp[2][2] = k.z; mtemp[2][3] = Vec4f.dot(k, minusEye);
//        mtemp[3][0] = 0; mtemp[3][1] = 0; mtemp[3][2] = 0; mtemp[3][3] = 1;
//        m = mtemp;
//        printMat(m);

        mtemp[0][0] = i.x; mtemp[0][1] = j.x; mtemp[0][2] = k.x; mtemp[0][3] = 0;
        mtemp[1][0] = i.y; mtemp[1][1] = j.y; mtemp[1][2] = k.y; mtemp[1][3] = 0;
        mtemp[2][0] = i.z; mtemp[2][1] = j.z; mtemp[2][2] = k.z; mtemp[2][3] = 0;
        mtemp[3][0] = 0; mtemp[3][1] = 0; mtemp[3][2] = 0; mtemp[3][3] = 1;
        t[0][3] = -t[0][3];
        t[1][3] = -t[1][3];
        t[2][3] = -t[2][3];
        mInverse = mulMat(t, mtemp);
        //printMat(mInverse);
//        Vec3f asd = reflect(new Vec3f(10,-1, 5), new Vec3f(0,1,0));
//        asd.print();
    }
    public float[][] getM () {
        return m;
    }

    private void calcP () {
        p[0][0] = 2*zf/sw; p[0][1] = 0; p[0][2] = 0; p[0][3] = 0;
        p[1][0] = 0; p[1][1] = 2*zf/sh; p[1][2] = 0; p[1][3] = 0;
        p[2][0] = 0; p[2][1] = 0; p[2][2] = zb/(zb-zf); p[2][3] = -zf*zb/(zb-zf);
        p[3][0] = 0; p[3][1] = 0; p[3][2] = 1; p[3][3] = 0;

//        p[0][0] = zf/sw; p[0][1] = 0; p[0][2] = 0; p[0][3] = 1f/2;
//        p[1][0] = 0; p[1][1] = zf/sh; p[1][2] = 0; p[1][3] = 1f/2;
//        p[2][0] = 0; p[2][1] = 0; p[2][2] = 1/(zb-zf); p[2][3] = -zf/(zb-zf);
//        p[3][0] = 0; p[3][1] = 0; p[3][2] = 0; p[3][3] = 1;

//        float fovX = (float)Math.PI*0.8f;
//        float fovY = (float)Math.PI*0.8f;
//        p[0][0] = 1f/(float) Math.tan(fovX/2); p[0][1] = 0; p[0][2] = 0; p[0][3] = 0;
//        p[1][0] = 0; p[1][1] = 1f/(float) Math.tan(fovY/2); p[1][2] = 0; p[1][3] = 0;
//        p[2][0] = 0; p[2][1] = 0; p[2][2] = -(zb+zf)/(zb-zf); p[2][3] = -2*(zb*zf)/(zb-zf);
//        p[3][0] = 0; p[3][1] = 0; p[3][2] = -1; p[3][3] = 0;

//        p[0][0] = 2f*zf/sw; p[0][1] = 0; p[0][2] = 0; p[0][3] = 0;
//        p[1][0] = 0; p[1][1] = 2f*zf/sh; p[1][2] = 0; p[1][3] = 0;
//        p[2][0] = 0; p[2][1] = 0; p[2][2] = -(zb+zf)/(zb-zf); p[2][3] = -2*zb*zf/(zb-zf);
//        p[3][0] = 0; p[3][1] = 0; p[3][2] = -1; p[3][3] = 0;
        //printMat(p);
    }

    public ArrayList<ScreenPoint> calcLighting (ArrayList<Body> bodies, ArrayList<LightSource> lights, int width, int height, ProgressBar pBar) {
        ArrayList<ScreenPoint> res = new ArrayList<>();
        ArrayList<Vec3f> colors = new ArrayList<>();
        float max = 0;

        Vec4f toNearPlane = Vec4f.sub(target, transform.position).normalize().mul(zf);
        Vec4f r = Vec4f.cross(toNearPlane, up).normalize().mul(-sw/width); // - to make right from left
        Vec4f u = new Vec4f(up).mul(sh/height);
        float step;
        switch (quality) {
            case 1:
                step = 2;
                break;
            case 2:
                step = 1;
                break;
            default:
                step = 0.5f;
                break;
        }
        for (float x = 0; x < width; x+=step) {
            for (float y = 0; y < height; y+=step) {
                pBar.updateBar((float)(x*height+y)/(width*height*2));
                Vec3f from = new Vec3f(transform.position);
                Vec4f dr = new Vec4f(r).mul(x-width/2); //-width/2 because toNearPlane points to center of near plane
                Vec4f du = new Vec4f(u).mul(y-height/2);
                Vec3f dir = new Vec3f(Vec4f.add(Vec4f.add(dr, du), toNearPlane));

                Vec3f c = calcPixelLighting(bodies, lights, from, dir);
                float maxColor = Math.max(Math.max(c.x, c.y), c.z);
                if (maxColor > max) max = maxColor;
                colors.add(c);
//                if (c != null) {
//                    res.add(new ScreenPoint(x, y, c));
//                }
            }
        }
        //System.out.println(max);
        if (quality == 1) step = 2;
        else step = 1;
        int m = 1;
        if (quality == 3) m = 2;
        for (int x = 0; x < width; x+=step) {
            for (int y = 0; y < height; y+=step) {
                pBar.updateBar(0.5f+(float)(x*height+y)/(width*height*2));
                Vec3f c = colors.get((int)(height/step*x/step*m*m+y/step*m));
                if (quality == 3) {
                    //y++;
                    Vec3f c2 = colors.get((int)(height*x*m*m+y*m+1));
                    //y++;
                    Vec3f c3 = colors.get((int)(height*x*m*m+y*m+2));
                    //y++;
                    Vec3f c4 = colors.get((int)(height*x*m*m+y*m+3));
                    c = Vec3f.add(c, c2);
                    c = Vec3f.add(c, c3);
                    c = Vec3f.add(c, c4);
                    c.div(4);
                }
                c.div(max); // normalize
                c = gammaCorrection(c);
                int red = (int)(c.x * 255 +0.5f);
                int g = (int)(c.y * 255 +0.5f);
                int b = (int)(c.z * 255 +0.5f);
//                if (red > 255) red = 255;
//                if (g > 255) g = 255;
//                if (b > 255) b = 255;
                res.add(new ScreenPoint(x, y, new Color(red, g, b)));
                if (quality == 1) {
                    res.add(new ScreenPoint(x + 1, y, new Color(red, g, b)));
                    res.add(new ScreenPoint(x, y + 1, new Color(red, g, b)));
                    res.add(new ScreenPoint(x + 1, y + 1, new Color(red, g, b)));
                }
            }
        }
        return res;
    }

    private Vec3f calcPixelLighting (ArrayList<Body> bodies, ArrayList<LightSource> lights, Vec3f from, Vec3f dir) {
        Vec3f c = shootRay(bodies, null, lights, from, dir, 1);
//        float maxColor = Math.max(Math.max(c.x, c.y), c.z);
//        if (maxColor == 0)
//            return Color.black;
//        c.div(maxColor); // normalize
//        c = gammaCorrection(c);
//        int r = (int)(c.x * 255 +0.5f);
//        int g = (int)(c.y * 255 +0.5f);
//        int b = (int)(c.z * 255 +0.5f);
//        System.out.println(r + " " + g + " " + b);
        //c.print();
        return c;
    }

    private Vec3f shootRay (ArrayList<Body> bodies, Body noIntersectBody, ArrayList<LightSource> lights, Vec3f from, Vec3f dir, int count) {
        SurfacePoint closestHit = null;
        OpticalBody currentBody = null;

        for (Body b : bodies) { // find intersection closest to camera
            if (b == noIntersectBody) continue;
            if (!(b instanceof OpticalBody)) continue;
            OpticalBody o = (OpticalBody)b;
            SurfacePoint hit = o.findIntersection(from, dir);
            if (hit != null) {
                if (closestHit == null) {
                    closestHit = hit;
                    currentBody = o;
                } else {
                    Vec3f toHit = Vec3f.sub(hit.position, from);
                    Vec3f toClosestHit = Vec3f.sub(closestHit.position, from);
                    if (toHit.length() < toClosestHit.length()) {
                        closestHit = hit;
                        currentBody = o;
                    }
                }
                continue;
            }
        }
        Vec3f res = new Vec3f(0,0,0);
        if (closestHit == null) {
            return res; // return black
        }
        for (LightSource l : lights) {
            //return lightSourceImpact(l, hit);
            Vec3f sourceImpact = new Vec3f(0,0,0);
            if (!isLightSourceVisible(bodies, currentBody, l, closestHit)) continue;
            sourceImpact = addColor(sourceImpact, diffuseImpact(l, closestHit));
            sourceImpact = addColor(sourceImpact, specularImpact(l, closestHit, dir));

            Vec3f toSource = Vec3f.sub(new Vec3f(l.getPosition()), closestHit.position);
            float distToSource = toSource.length();
            sourceImpact.div(distToSource);
            res = addColor(res, sourceImpact);
        }
        // Ambient
        res.x += currentBody.kdr * ambientColor.x;
        res.y += currentBody.kdg * ambientColor.y;
        res.z += currentBody.kdb * ambientColor.z;
        // Secondary rays
        if (count != rayMax) {
            Vec3f newDir = reflect(dir, closestHit.normal);
            //if (newDir == null) return res;
            Vec3f refraction = shootRay(bodies, currentBody, lights, closestHit.position, newDir, count + 1);
            refraction.x *= currentBody.ksr;
            refraction.y *= currentBody.ksg;
            refraction.z *= currentBody.ksb;
            if (refraction != null) {
                if (noIntersectBody != null) {
                    Vec3f toSource = Vec3f.sub(from, closestHit.position);
                    float dist = toSource.length();
                    if (dist < 1) dist = 1;
                    refraction.div(dist);
                }
                res = addColor(res, refraction);
            }
        }
        return res;
    }

    private Vec3f addColor (Vec3f c1, Vec3f c2) {
        return Vec3f.add(c1, c2);
    }
    private Color addColor (Color c1, Color c2) {
        int r = c1.getRed() + c2.getRed();
        if (r > 255) r = 255; if (r < 0) r = 0;
        int g = c1.getGreen() + c2.getGreen();
        if (g > 255) g = 255; if (g < 0) g = 0;
        int b = c1.getBlue() + c2.getBlue();
        if (b > 255) b = 255; if (b < 0) b = 0;
        return new Color(r, g, b);
    }
    private float normalizeColor (int c) {
        return (float)c/255f;
    }
    private Vec3f gammaCorrection (Vec3f c) {
        return new Vec3f((float)Math.pow(c.x, gamma),
        (float)Math.pow(c.y, gamma),
        (float)Math.pow(c.z, gamma));
    }
    private boolean isLightSourceVisible (ArrayList<Body> bodies, Body currentBody,  LightSource l, SurfacePoint p) {
        for (Body b : bodies) { // find intersection closest to camera
            if (b == currentBody) continue;
            if (!(b instanceof OpticalBody)) continue;
            OpticalBody o = (OpticalBody) b;
            Vec3f from = p.position;
            Vec3f toSource = Vec3f.sub(new Vec3f(l.transform.position), p.position);
            SurfacePoint hit = o.findIntersection(from, toSource);
            if (hit != null) return false;
        }
        return true;
    }
    private Vec3f reflect (Vec3f v, Vec3f normal) {
        float dot = Vec3f.dot(normal, Vec3f.reverse(v));
        //if (dot < 0) return null;
        Vec3f n = new Vec3f(normal).mul(dot);
        //n.print();
        Vec3f temp = Vec3f.add(v, n);
        return Vec3f.add(n, temp);
    }

    private Vec3f diffuseImpact (LightSource l, SurfacePoint p) {
        Vec3f toSource = Vec3f.sub(new Vec3f(l.getPosition()), p.position);
        float dot = Vec3f.dot(toSource, p.normal);
        if (Vec3f.dot(toSource, p.normal) < 0) return new Vec3f(0,0,0);
        float r = p.kdr * dot * normalizeColor(l.getColor().getRed());
        float g = p.kdg * dot * normalizeColor(l.getColor().getGreen());
        float b = p.kdb * dot * normalizeColor(l.getColor().getBlue());
        //System.out.println(r + " " + g + " " + b);
        return new Vec3f(r, g, b);
    }

    private Vec3f specularImpact(LightSource l, SurfacePoint p, Vec3f dir) { // dir to surface from camera
        Vec3f toSource = Vec3f.sub(new Vec3f(l.getPosition()), p.position);
        if (Vec3f.dot(toSource, p.normal) < 0) return new Vec3f(0,0,0);
        Vec3f h = Vec3f.add(toSource, Vec3f.reverse(dir)).normalize();
        float dot = Vec3f.dot(h, p.normal);
        //System.out.println(dot);
        dot = (float)Math.pow(dot, p.power);
        //System.out.println(dot);
        float r = p.ksr * dot * normalizeColor(l.getColor().getRed());
        float g = p.ksg * dot * normalizeColor(l.getColor().getGreen());
        float b = p.ksb * dot * normalizeColor(l.getColor().getBlue());
        //System.out.println(r + " " + g + " " + b);
        return new Vec3f(r, g, b);
    }

    public ArrayList<UVLine> calcWires (ArrayList<Body> bodies) {
        ArrayList<UVLine> res = new ArrayList<>();
        for (Body b : bodies) {
            res.addAll(renderXyz(b));
            if (!(b instanceof WiredBody)) continue;
            WiredBody w = (WiredBody)b;
            res.addAll(renderWiredBody(w));
        }
        return res;
    }
    private ArrayList<UVLine>  renderWiredBody (WiredBody wiredBody) {
        ArrayList<UVLine> res = new ArrayList<>();
        float[][] w = wiredBody.getM();
        for (Segment s : wiredBody.getSegments()) {
            Vec4f inWorld1 = Vec4f.mulMat(w, s.p1);
            Vec4f inWorld2 = Vec4f.mulMat(w, s.p2);

            Vec4f inCameraSpace1 = Vec4f.mulMat(m, inWorld1);
            Vec4f inCameraSpace2 = Vec4f.mulMat(m, inWorld2);

            Vec4f proj1 = Vec4f.mulMat(p, inCameraSpace1);
            proj1.div(proj1.w);
            proj1.w = 1;
            //proj1.z = normalize(proj1.z);
            Vec4f proj2 = Vec4f.mulMat(p, inCameraSpace2);
            proj2.div(proj2.w);
            proj2.w = 1;
            //proj2.z = normalize(proj2.z);

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

            clip(proj1, proj2);
//            if (proj1.z > 1f || proj2.z > 1f || proj1.z < -1f || proj2.z < -1f) {
//                System.out.println(proj1.z + " " + proj2.z);
//                continue;
//            }
            proj1.x = normalize(proj1.x);
            proj1.y = normalize(proj1.y);
            proj2.x = normalize(proj2.x);
            proj2.y = normalize(proj2.y);
            res.add(new UVLine(proj1.x, proj1.y, proj2.x, proj2.y, wiredBody.getColor(), null));
        }
        return res;
    }
    private ArrayList<UVLine>  renderXyz (Body body) {
        ArrayList<UVLine> res = new ArrayList<>();
        float[][] w = body.getM();
        Color[] c = {Color.red, Color.green, Color.blue};
        String[] str = {"X", "Y", "Z"};
        int n = 0;
        for (Segment s : body.getXyz()) {
            Vec4f inWorld1 = Vec4f.mulMat(w, s.p1);
            Vec4f inWorld2 = Vec4f.mulMat(w, s.p2);

            Vec4f inCameraSpace1 = Vec4f.mulMat(m, inWorld1);
            Vec4f inCameraSpace2 = Vec4f.mulMat(m, inWorld2);

            Vec4f proj1 = Vec4f.mulMat(p, inCameraSpace1);
            proj1.div(proj1.w);
            proj1.w = 1;
            Vec4f proj2 = Vec4f.mulMat(p, inCameraSpace2);
            proj2.div(proj2.w);
            proj2.w = 1;
            clip(proj1, proj2);
//            if (proj1.z > 1f || proj2.z > 1f || proj1.z < -1f || proj2.z < -1f) {
//                continue;
//            }
            proj1.x = normalize(proj1.x);
            proj1.y = normalize(proj1.y);
            proj2.x = normalize(proj2.x);
            proj2.y = normalize(proj2.y);
            res.add(new UVLine(proj1.x, proj1.y, proj2.x, proj2.y, c[n], str[n]));
            n++;
        }
        return res;
    }

    private void clip (Vec4f p1, Vec4f p2) {
        Vec4f n, f;
        if (p1.z < p2.z) {
            n = p1;
            f = p2;
        } else {
            n = p2;
            f = p1;
        }
        float xcn, ycn, xcf, ycf;
        if (n.z < -1f) {
            //xcn =(f.x-n.x)*(1f-n.z)/(f.z-n.z)+n.z;
            //ycn =(f.y-n.y)*(1f-n.z)/(f.z-n.z)+n.z;
            xcn =(f.x-n.x)*(-1f-n.z)/(f.z-n.z)+n.x;
            ycn =(f.y-n.y)*(-1f-n.z)/(f.z-n.z)+n.y;
        } else {
            xcn = n.x;
            ycn = n.y;
        }
        if (f.z > 1f) {
            xcf = f.x - (f.x-n.x)*(f.z-1)/(f.z-n.z);
            ycf = f.y - (f.y-n.y)*(f.z-1)/(f.z-n.z);
            //System.out.println("FAR " + f.z);
        } else {
            xcf = f.x;
            ycf = f.y;
            //System.out.println("OK " + f.z);
        }
        if ((f.z < -1f && n.z < -1f) || (f.z > 1f && n.z > 1f)) {
            xcn = ycn = xcf = ycf = 2;
        }
        n.x = xcn;
        n.y = ycn;
        f.x = xcf;
        f.y = ycf;
    }
    private float normalize (float x) {
        //if (x < -1f || x > 1f) return x;
        return (x+1)/2f;
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
        //transform.position.print();

        target = target.add(target, Vec4f.sub(transform.position, oldPos));

//        transform.position.x += dir.x;
//        transform.position.y += dir.y;
//        transform.position.z += dir.z;
//        transform.position = Vec4f.mulMat(m, transform.position);
//        System.out.print("NEW:");
//        transform.position.print();
        //System.out.println();
        calcM();
        //System.out.println("BEFORE");
        //up.print();
        up = Vec4f.mulMat(mInverse, new Vec4f(0,1,0,1));
        up = Vec4f.sub(up, transform.position);
        //System.out.println("AFTER");
        //up.print();
        calcP();
    }
    public void rotate (Vec4f dir) {
//        System.out.println();
        //dir.print();
        Vec4f u = new Vec4f(up);
        u.div(u.length());
//        //u.mul(dir.y);
        u.w = 1;
//        u.print();
//
        Vec4f r = Vec4f.cross(Vec4f.sub(target,transform.position).div(Vec4f.sub(target,transform.position).length()), u);
        r.div(r.length());
//        //r.mul(dir.y);
        r.w = 1;
//        r.print();

        //Vec4f total = Vec4f.add(u.mul(dir.y), r.mul(dir.x));
        applyRotation(r.mul(dir.y));
        applyRotation(u.mul(dir.x));
    }
    public void applyRotation (Vec4f axis) {
        transform.rotation.x = axis.x;
        transform.rotation.y = axis.y;
        transform.rotation.z = axis.z;
//        calcM();
//        m[0][3] = 0;
//        m[1][3] = 0;
//        m[2][3] = 0;
        float a = transform.rotation.x;
        float[][] mx = new float[4][4];
        mx[0][0] = 1; mx[0][1] = 0; mx[0][2] = 0; mx[0][3] = 0;
        mx[1][0] = 0; mx[1][1] = (float) Math.cos(a); mx[1][2] = -(float) Math.sin(a); mx[1][3] = 0;
        mx[2][0] = 0; mx[2][1] = (float) Math.sin(a); mx[2][2] = (float) Math.cos(a); mx[2][3] = 0;
        mx[3][0] = 0; mx[3][1] = 0; mx[3][2] = 0; mx[3][3] = 1;
        transform.position = Vec4f.mulMat(mx, transform.position);
        up = Vec4f.mulMat(mx, up);

        a = transform.rotation.y;
        float[][] my = new float[4][4];
        my[0][0] = (float) Math.cos(a); my[0][1] = 0; my[0][2] = (float) Math.sin(a); my[0][3] = 0;
        my[1][0] = 0; my[1][1] = 1; my[1][2] = 0; my[1][3] = 0;
        my[2][0] = -(float) Math.sin(a); my[2][1] = 0; my[2][2] = (float) Math.cos(a); my[2][3] = 0;
        my[3][0] = 0; my[3][1] = 0; my[3][2] = 0; my[3][3] = 1;
        transform.position = Vec4f.mulMat(my, transform.position);
        up = Vec4f.mulMat(my, up);

        a = transform.rotation.z;
        float[][] mz = new float[4][4];
        mz[0][0] = (float) Math.cos(a); mz[0][1] = -(float) Math.sin(a); mz[0][2] = 0; mz[0][3] = 0;
        mz[1][0] = (float) Math.sin(a); mz[1][1] = (float) Math.cos(a); mz[1][2] = 0; mz[1][3] = 0;
        mz[2][0] = 0; mz[2][1] = 0; mz[2][2] = 1; mz[2][3] = 0;
        mz[3][0] = 0; mz[3][1] = 0; mz[3][2] = 0; mz[3][3] = 1;
        transform.position = Vec4f.mulMat(mz, transform.position);
        up = Vec4f.mulMat(mz, up);
        calcM();
    }

    public float getSw() {
        return sw;
    }
    public void setSw(float sw) {
        if (sw < 0 || sw > 255) return;
        this.sw = sw;
        calcP();
    }
    public float getSh() {
        return sh;
    }
    public void setSh(float sh) {
        if (sh < 0 || sh > 255) return;
        this.sh = sh;
        calcP();
    }
    public float getZf() {
        return zf;
    }
    public void setZf(float zf) {
        if (zf < 0 || zf > 255) return;
        this.zf = zf;
        calcP();
    }
    public float getZb() {
        return zb;
    }
    public void setZb(float zb) {
        if (zb < 0 || zb > 255) return;
        this.zb = zb;
        calcP();
    }
    public void zoom (int m) {
        if (zb + m > 255 || zf + m <= 0) return;
        else {
            zb += m;
            zf += m;
        }
        calcP();
    }
}
