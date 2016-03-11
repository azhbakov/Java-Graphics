package ru.nsu.fit.g13201.azhbakov.Model;

import ru.nsu.fit.g13201.azhbakov.Model.BMP.BMPReader;
import ru.nsu.fit.g13201.azhbakov.Model.BMP.BadFileException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

/**
 * Created by marting422 on 11.03.2016.
 */
public class Logic extends Observable {
    public final static Dimension zoneSize = new Dimension(350, 350);
    public final static int border = 10;

    File currentFile = null;

    boolean hasUnsavedChanges = false;

    BufferedImage source;
    BufferedImage imageA, imageB, imageC;

    int frameLeft, frameRight, frameUp, frameBottom;

    public Logic () {

    }

    public void loadBMP (File f) throws IOException, BadFileException {
        BufferedImage image = BMPReader.readBMP(f);
        BufferedImage mini = null;
        if (image == null) {
            throw new BadFileException();
        }
        try {
            if (image.getWidth() > zoneSize.width || image.getHeight() > zoneSize.height) {
                if (image.getWidth() > image.getHeight()) {
                    mini = Downscale.downscale(image, zoneSize.width,
                            zoneSize.width * image.getHeight()/image.getWidth());
                } else {
                    mini = Downscale.downscale(image, zoneSize.height * image.getWidth()/image.getHeight(),
                            zoneSize.height);
                }
            } else {
                mini = image;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        clear();
        currentFile = f;
        source = image;
        imageA = mini;

        setChanged();
        notifyObservers();
    }

    public void selectArea (int x, int y) {
        int xs = (int)((float)x/imageA.getWidth()*source.getWidth());
        int ys = (int)((float)y/imageA.getHeight()*source.getHeight());
        int left, right, up, bottom;

        if (source.getWidth() <= zoneSize.width) { // <?
            left = 0;
            right = source.getWidth()-1;
        } else {
            if (xs <= zoneSize.width/2) { // right will not overlap
                left = xs - zoneSize.width/2;
                if (left < 0) left = 0;
                right = left + zoneSize.width;
            } else { // left will not overlap
                right = xs + zoneSize.width/2;
                if (right >= source.getWidth()) right = source.getWidth()-1;
                left = right - zoneSize.width;
            }
        }

        if (source.getHeight() <= zoneSize.getHeight()) { // <?
            bottom = 0;
            up = source.getHeight()-1;
        } else {
            if (ys <= zoneSize.height/2) { // up will not overlap
                bottom = ys - zoneSize.height/2;
                if (bottom < 0) bottom = 0;
                up = bottom + zoneSize.height;
            } else { // bottom will not overlap
                up = ys + zoneSize.height/2;
                if (up >= source.getHeight()) up = source.getHeight()-1;
                bottom = up - zoneSize.height;
            }
        }
        //System.out.println("left == " + left + " right == " + right + " bottom == " + bottom + " up == " + up);

        frameBottom = (int)((float)bottom/source.getHeight()*imageA.getHeight());
        frameLeft = (int)((float)left/source.getWidth()*imageA.getWidth());
        frameRight = (int)((float)right/source.getWidth()*imageA.getWidth());
        frameUp = (int)((float)up/source.getHeight()*imageA.getHeight());
        setChanged();
        notifyObservers();
    }

    public File saveToFile(File f) throws IOException {

        currentFile = f;
        hasUnsavedChanges = false;
        setChanged();
        notifyObservers();
        return f;
    }

    public void clear () {
        currentFile = null;
        hasUnsavedChanges = false;
        imageA = null;
        imageB = null;
        imageC = null;
        frameUp = frameBottom = frameLeft = frameRight = 0;
        setChanged();
        notifyObservers();
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public BufferedImage getSource() {
        return source;
    }

//    public void setSource(BufferedImage source) {
//        this.source = source;
//    }

    public BufferedImage getImageA() {
        return imageA;
    }

//    public void setImageA(BufferedImage imageA) {
//        this.imageA = imageA;
//    }

    public BufferedImage getImageB() {
        return imageB;
    }

//    public void setImageB(BufferedImage imageB) {
//        this.imageB = imageB;
//    }

    public BufferedImage getImageC() {
        return imageC;
    }

//    public void setImageC(BufferedImage imageC) {
//        this.imageC = imageC;
//    }

    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public int getFrameLeft() {
        return frameLeft;
    }

    public int getFrameRight() {
        return frameRight;
    }

    public int getFrameUp() {
        return frameUp;
    }

    public int getFrameBottom() {
        return frameBottom;
    }
}
