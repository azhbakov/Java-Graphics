package ru.nsu.fit.g13201.azhbakov.model.BMP;

/**
 * Created by marting422 on 10.03.2016.
 */
public class BitmapInfoHeader {
    final int biSize;
    final int biWidth;
    final int biHeight;
    final short biPlanes;
    final short biBitCount;
    final int biCompression;
    final int biSizeImage;
    final int biXPelsPerMeter;
    final int biYPelsPerMeter;
    final int biClrUsed;
    final int biClrImportant;

    public BitmapInfoHeader(int biSize, int biWidth, int biHeight, short biPlanes,
                            short biBitCount, int biCompression, int biSizeImage,
                            int biXPelsPerMeter, int biYPelsPerMeter, int biClrUsed, int biClrImportant) {
        this.biSize = biSize;
        this.biWidth = biWidth;
        this.biHeight = biHeight;
        this.biPlanes = biPlanes;
        this.biBitCount = biBitCount;
        this.biCompression = biCompression;
        this.biSizeImage = biSizeImage;
        this.biXPelsPerMeter = biXPelsPerMeter;
        this.biYPelsPerMeter = biYPelsPerMeter;
        this.biClrUsed = biClrUsed;
        this.biClrImportant = biClrImportant;
    }

    public void print () {
        System.out.println("BITMAPINFOHEADER:");
        System.out.println("Structure size: " + biSize);
        System.out.println("Image width: " + biWidth);
        System.out.println("Image height: " + biHeight);
        System.out.println("Number of planes: " + biPlanes);
        System.out.println("Bits per pixel: " + biBitCount);
        System.out.println("Compression: " + biCompression);
        System.out.println("Bitmap size: " + biSizeImage);
        System.out.println("Device res X: " + biXPelsPerMeter);
        System.out.println("Device res Y: " + biYPelsPerMeter);
        System.out.println("Used colors from table: " + biClrUsed);
        System.out.println("Important colors from table: " + biClrImportant);
    }
}
