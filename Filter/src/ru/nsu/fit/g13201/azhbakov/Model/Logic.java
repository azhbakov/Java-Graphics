package ru.nsu.fit.g13201.azhbakov.Model;

import ru.nsu.fit.g13201.azhbakov.Model.BMP.BMPReader;
import ru.nsu.fit.g13201.azhbakov.Model.BMP.BadFileException;

import java.awt.*;
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

    int sourceLeft, sourceRight, sourceUp, sourceBottom;
    int frameLeft, frameRight, frameUp, frameBottom;

    int sobelThreshold = 255;
    int robertsThreshold = 255;


    public Logic () {

    }

    public void loadBMP (File f) throws IOException, BadFileException {
        BufferedImage image = BMPReader.readBMP(f);
        //System.out.println("LOADED");
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
        //System.out.println("SCALED");
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

        sourceBottom = bottom;
        sourceUp = up;
        sourceLeft = left;
        sourceRight = right;

        frameBottom = (int)((float)bottom/source.getHeight()*imageA.getHeight());
        frameLeft = (int)((float)left/source.getWidth()*imageA.getWidth());
        frameRight = (int)((float)right/source.getWidth()*imageA.getWidth());
        frameUp = (int)((float)up/source.getHeight()*imageA.getHeight());
        AtoB();
        setChanged();
        notifyObservers();
    }

    public void BtoC () {
        if (imageB == null) return;
        if (imageC == null) imageC = new BufferedImage(imageB.getWidth(), imageB.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < imageB.getWidth(); i++) {
            for (int j = 0; j < imageB.getHeight(); j++) {
                imageC.setRGB(i, j, imageB.getRGB(i, j));
            }
        }
        setChanged();
        notifyObservers();
    }

    public void CtoB () {
        if (imageB == null || imageC == null) return;
        //if (imageC == null) imageB = new BufferedImage(imageB.getWidth(), imageB.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < imageC.getWidth(); i++) {
            for (int j = 0; j < imageC.getHeight(); j++) {
                imageB.setRGB(i, j, imageC.getRGB(i, j));
            }
        }
        setChanged();
        notifyObservers();
    }

    public void AtoB () {
        imageB = new BufferedImage(sourceRight+1 - sourceLeft, sourceUp+1 - sourceBottom, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < sourceRight - sourceLeft+1; i++) {
            for (int j = 0; j < sourceUp - sourceBottom+1; j++) {
                imageB.setRGB(i, j, source.getRGB(sourceLeft + i, sourceBottom + j));
            }
        }
        setChanged();
        notifyObservers();
    }

    public void grayscale () {
        BtoC();
        if (imageC == null) return;
        Grayscale.grayscale(imageC);
        setChanged();
        notifyObservers();
    }

    public void negative () {
        BtoC();
        if (imageC == null) return;
        Negative.negative(imageC);
        setChanged();
        notifyObservers();
    }

    public void bilinearLerp () {
        BtoC();
        if (imageC == null) return;
        imageC = BilinearLerp.zoomX2(imageC);
        setChanged();
        notifyObservers();
    }

    public void ditheringFloyd () {
        BtoC();
        if (imageC == null) return;
        int[] newc = {0, 50, 100, 150, 200, 230, 255};//new int[25];
//        for (int i = 0; i < 25; i++) {
//            newc[i] = 10 * i;
//        }
        imageC = Dithering.FloydSteinberg(imageC, newc, newc, newc);
        setChanged();
        notifyObservers();
    }

    public void ditheringOrdered () {
        BtoC();
        if (imageC == null) return;
        int[] newc = {0, 50, 100, 150, 200, 230, 255};//new int[25];
//        for (int i = 0; i < 25; i++) {
//            newc[i] = 10 * i;
//        }
        imageC = Dithering.orderedDithering(imageC, newc, newc, newc);
        setChanged();
        notifyObservers();
    }

    public void sobel () {
        BtoC();
        if (imageC == null) return;
        imageC = EdgeDetection.SobelFilter(imageC, sobelThreshold);
        setChanged();
        notifyObservers();
    }

    public void roberts () {
        BtoC();
        if (imageC == null) return;
        imageC = EdgeDetection.RobertsFilter(imageC, robertsThreshold);
        setChanged();
        notifyObservers();
    }

    public void smoothing () {
        BtoC();
        if (imageC == null) return;
        imageC = Smoothing.smooth(imageC);
        setChanged();
        notifyObservers();
    }

    public void sharpening () {
        BtoC();
        if (imageC == null) return;
        imageC = Sharpening.sharpen(imageC);
        setChanged();
        notifyObservers();
    }

    public void stamping () {
        BtoC();
        if (imageC == null) return;
        imageC = Stamping.stamp(imageC);
        setChanged();
        notifyObservers();
    }

    public void aqua () {
        BtoC();
        if (imageC == null) return;
        imageC = Smoothing.smooth(imageC);
        imageC = Sharpening.sharpen(imageC);
        setChanged();
        notifyObservers();
    }

    public void gamma () {
        BtoC();
        if (imageC == null) return;
        imageC = GammaCorrection.gammaCorrection(imageC, 0.5f);
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
