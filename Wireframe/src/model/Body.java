package model;

/**
 * Created by marting422 on 21.04.2016.
 */
public class Body {
    Transform transform;

    public Body (float x, float y, float z, float w, float zx, float yz, float xy, float rw) {
        transform = new Transform(new Vec4f(x, y, z, w), new Vec4f(zx, yz, xy, rw));
    }
    public Body (Vec4f pos, Vec4f rot) {
        transform = new Transform(pos, rot);
    }

    public void setPosition (float x, float y, float z) {
        transform.position.x = x;
        transform.position.y = y;
        transform.position.z = z;
    }
    public void setPosition (float x, float y, float z, float w) {
        transform.position.x = x;
        transform.position.y = y;
        transform.position.z = z;
        transform.position.w = w;
    }
    public void setPosition(Vec4f pos) {
        transform.position = pos;
    }

    public void setRotation (float zx, float yz, float xy) {
        transform.rotation.x = zx;
        transform.rotation.y = yz;
        transform.rotation.z = xy;
    }
    public void setRotation (float zx, float yz, float xy, float rw) {
        transform.rotation.x = zx;
        transform.rotation.y = yz;
        transform.rotation.z = xy;
        transform.rotation.w = rw;
    }
    public void setRotation (Vec4f rot) {
        transform.rotation = rot;
    }
    public float[][] getModel () {
        float a = transform.rotation.z;
        float b = transform.rotation.x;
        float y = transform.rotation.y;
        float[][] m = new float[4][4];
        m[0][0] = (float)(Math.cos(a)*Math.cos(y) - Math.sin(a)*Math.cos(b)*Math.sin(y));
        m[0][1] = (float)(-Math.cos(a)*Math.sin(y) - Math.sin(a)*Math.cos(b)*Math.cos(y));
        m[0][2] = (float)(Math.sin(a)*Math.sin(b));

        m[1][0] = (float)(Math.sin(a)*Math.cos(y) + Math.cos(a)*Math.cos(b)*Math.sin(y));
        m[1][1] = (float)(-Math.sin(a)*Math.sin(y) + Math.cos(a)*Math.cos(b)*Math.cos(y));
        m[1][2] = (float)(-Math.cos(a)*Math.sin(b));

        m[2][0] = (float)(Math.sin(b)*Math.sin(y));
        m[2][1] = (float)(Math.sin(b)*Math.cos(y));
        m[2][2] = (float)(Math.cos(b));
    }
}
