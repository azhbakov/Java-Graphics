package model;


/**
 * Created by marting422 on 21.04.2016.
 */
public class Transform {
    Vec4f position;
    Vec4f rotation;

    public Transform () {}

    public Transform (Vec4f pos, Vec4f rot) {
        position = pos;
        rotation = rot;
    }
}
