package ru.nsu.fit.g13201.azhbakov.model;

/**
 * Created by Martin on 01.04.2016.
 */
public class CircleFunc implements Func2{
    public float res(float x, float y) {
        //if (x < 15) return x+10;
        return (float) Math.sqrt(x*x + y*y);
    }
}
