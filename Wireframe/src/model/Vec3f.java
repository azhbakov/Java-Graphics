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

    public Vec3f () {
        x = 0; y = 0; z = 0;
    }
}
