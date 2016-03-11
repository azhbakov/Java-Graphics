package ru.nsu.fit.g13201.azhbakov.Model.BMP;

/**
 * Created by marting422 on 10.03.2016.
 */
public class BitmapFileHeader {
    final short bfType;
    final int bfSize;
    final short bfReserved1;
    final short bfReserved2;
    final int bfOffBits;

    public BitmapFileHeader(short bfType, int bfSize, short bfReserved1, short bfReserved2, int bfOffBits) {
        this.bfType = bfType;
        this.bfSize = bfSize;
        this.bfReserved1 = bfReserved1;
        this.bfReserved2 = bfReserved2;
        this.bfOffBits = bfOffBits;
    }

    public void print () {
        System.out.println("BITMAPFILEHEADER:");
        System.out.println("BM: " + bfType);
        System.out.println("File size: " + bfSize);
        System.out.println("Reserved1: " + bfReserved1);
        System.out.println("Reserved2: " + bfReserved2);
        System.out.println("Bitmap offset: " + bfOffBits);
    }
}
