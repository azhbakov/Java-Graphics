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

    public Logic () {

    }

    public void loadBMP (File f) throws IOException, BadFileException {
        BufferedImage image = BMPReader.readBMP(f);
        if (image == null) {
            throw new BadFileException();
        }
        source = image;
        try {
            if (source.getWidth() > zoneSize.width || source.getHeight() > zoneSize.height) {
                if (source.getWidth() > source.getHeight()) {
                    imageA = Downscale.downscale(source, zoneSize.width,
                            zoneSize.width * source.getHeight()/source.getWidth());
                } else {
                    imageA = Downscale.downscale(source, zoneSize.height * source.getWidth()/source.getHeight(),
                            zoneSize.height);
                }
            } else {
                imageA = source;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        hasUnsavedChanges = false;
        currentFile = f;

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
}
