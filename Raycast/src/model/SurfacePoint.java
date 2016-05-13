package model;

/**
 * Created by Martin on 12.05.2016.
 */
public class SurfacePoint {
    public Vec3f position;
    public Vec3f normal;
    public float kdr, kdg, kdb, ksr, ksg, ksb, power;

    public SurfacePoint(Vec3f position, Vec3f normal, float kdr, float kdg, float kdb, float ksr, float ksg, float ksb, float power) {
        this.position = position;
        this.normal = normal;
        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;
        this.ksr = ksr;
        this.ksg = ksg;
        this.ksb = ksb;
        this.power = power;
    }
}
