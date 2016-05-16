package model;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Vec3f {
    float x, y, z;
    public Vec3f (float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f (Vec3f v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vec3f (Vec4f v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vec3f () {
        x = 0; y = 0; z = 0;
    }

    public float length () {
        return (float)Math.sqrt(x*x + y*y + z*z);
    }

    public Vec3f normalize () {
        float l = length();
        return new Vec3f(x/l, y/l, z/l);
    }

    public static Vec3f reverse (Vec3f v) {
        return new Vec3f(-v.x, -v.y, -v.z);
    }

    public static float dot (Vec3f a, Vec3f b) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }

    public static Vec3f add (Vec3f a, Vec3f b) {
        return new Vec3f(a.x + b.x , a.y + b.y, a.z + b.z);
    }
    public static Vec3f sub (Vec3f a, Vec3f b) {
        return new Vec3f(a.x - b.x , a.y - b.y, a.z - b.z);
    }
    public static Vec3f mul (Vec3f a, Vec3f b) { return new Vec3f(a.x*b.x, a.y*b.y, a.z*b.z); }

    public Vec3f mul (float n) {
        x *= n;
        y *= n;
        z *= n;
        return this;
    }
    public static Vec3f mul (Vec3f v, float n) {
        return new Vec3f(v.x*n, v.y*n, v.z*n);
    }
    public Vec3f div (float n) {
        x /= n;
        y /= n;
        z /= n;
        return this;
    }
    public static Vec3f div (Vec3f v, float n) {
        return new Vec3f(v.x/n, v.y/n, v.z/n);
    }

    public void print () {
        System.out.println("[" + x + ", " + y + ", " + z  + "]");
    }
}
