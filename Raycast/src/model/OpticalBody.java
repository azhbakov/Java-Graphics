package model;

/**
 * Created by Martin on 09.05.2016.
 */
public abstract class OpticalBody extends WiredBody {

    float kdr, kdg, kdb, ksr, ksg, ksb, power;

    public OpticalBody(float x, float y, float z,
                       float kdr, float kdg, float kdb, float ksr, float ksg, float ksb, float power) {
        super (x,y,z, 0,0,0);
        //System.out.println(x +" "+ y +" "+ z);
        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;
        this.ksr = ksr;
        this.ksg = ksg;
        this.ksb = ksb;
        this.power = power;
    }

    public abstract SurfacePoint findIntersection (Vec3f from, Vec3f dir);
}
