package model;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Vec4f {
    float x, y, z, w;
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
    public float length () {
        return (float)Math.sqrt(x*x + y*y + z*z);
    }
    public static float length (Vec4f a) {
        return (float)Math.sqrt(a.x*a.x + a.y*a.y + a.z*a.z);
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
        return this;
    }
}
