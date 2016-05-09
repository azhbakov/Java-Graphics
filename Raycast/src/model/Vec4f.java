package model;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Vec4f {
    public float x, y, z, w;
    public Vec4f () {
        x = 0; y = 0; z = 0; w = 1;
    }
    public Vec4f (float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Vec4f (Vec4f v4f) {
        x = v4f.x;
        y = v4f.y;
        z = v4f.z;
        w = v4f.w;
    }
    public Vec4f (Vec3f v3f, float w) {
        x = v3f.x;
        y = v3f.y;
        z = v3f.z;
        this.w = w;
    }
    public static Vec4f cross (Vec4f a, Vec4f b) {
        return new Vec4f(a.z*b.y - a.y*b.z, a.x*b.z - a.z*b.x, a.y*b.x - a.x*b.y, 1);
    }
    public static Vec4f add (Vec4f a, Vec4f b) {
        return new Vec4f(a.x + b.x , a.y + b.y, a.z + b.z, 1);
    }
    public static Vec4f sub (Vec4f a, Vec4f b) {
        return new Vec4f(a.x - b.x , a.y - b.y, a.z - b.z, 1);
    }
    public static float dot (Vec4f a, Vec4f b) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }
    public float length () {
        return (float)Math.sqrt(x*x + y*y + z*z);
    }
    public static float length (Vec4f a) {
        return (float)Math.sqrt(a.x*a.x + a.y*a.y + a.z*a.z);
    }
    public Vec4f normalize () {
        float l = length();
        return new Vec4f(x/l, y/l, z/l, w);
    }
    public static Vec4f reverse (Vec4f a) {
        return new Vec4f(-a.x, -a.y, -a.z, 1);
    }
    public Vec4f mul (float n) {
        x *= n;
        y *= n;
        z *= n;
        return this;
    }
    public Vec4f div (float n) {
        x /= n;
        y /= n;
        z /= n;
        w /= n;
        return this;
    }
    public static Vec4f mulMat (float[][] m, Vec4f v) {
        Vec4f n = new Vec4f();
        float s;
        s = m[0][0]*v.x + m[0][1]*v.y + m[0][2]*v.z + m[0][3]*v.w;
        n.x = s;
        s = m[1][0]*v.x + m[1][1]*v.y + m[1][2]*v.z + m[1][3]*v.w;
        n.y = s;
        s = m[2][0]*v.x + m[2][1]*v.y + m[2][2]*v.z + m[2][3]*v.w;
        n.z = s;
        s = m[3][0]*v.x + m[3][1]*v.y + m[3][2]*v.z + m[3][3]*v.w;
        n.w = s;
        return n;
    }
    public void print () {
        System.out.println("[" + x + ", " + y + ", " + z + ", " + w + "]");
    }
}
