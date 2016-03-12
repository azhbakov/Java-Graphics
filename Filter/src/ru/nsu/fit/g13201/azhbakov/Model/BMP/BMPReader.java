package ru.nsu.fit.g13201.azhbakov.Model.BMP;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by marting422 on 10.03.2016.
 */
public class BMPReader {
    private static short BM = 19778;

    public static BufferedImage readBMP (File f) throws IOException, BadFileException {
        DataInputStream reader = new DataInputStream(new FileInputStream(f));
        BitmapFileHeader bitmapFileHeader = readFileHeader(reader);
        if (bitmapFileHeader.bfType != BM) {
            System.err.println("Bad file: not BM, but " + bitmapFileHeader.bfType);
            throw new BadFileException();
        }
        if (bitmapFileHeader.bfSize != f.length()) {
            System.err.println("Bad file: wrong file size, got " + bitmapFileHeader.bfSize + " instead of " + f.length());
            throw new BadFileException();
        }
        if (bitmapFileHeader.bfReserved1 != 0 || bitmapFileHeader.bfReserved2 != 0) {
            System.err.println("Bad file: wrong reserved spaces");
            throw new BadFileException();
        }
        BitmapInfoHeader bitmapInfoHeader = readInfoHeader(reader);

        bitmapFileHeader.print();
        System.out.println();
        bitmapInfoHeader.print();

        //reader.reset();
        //reader.skipBytes(bitmapFileHeader.bfOffBits);
        return createImage(bitmapInfoHeader.biWidth, bitmapInfoHeader.biHeight,
                read24Bitmap(reader, bitmapInfoHeader.biWidth, bitmapInfoHeader.biHeight));
    }

    /*
        typedef struct tagBITMAPFILEHEADER
         {
           WORD    bfType;
           DWORD   bfSize;
           WORD    bfReserved1;
           WORD    bfReserved2;
           DWORD   bfOffBits;
         } BITMAPFILEHEADER, *PBITMAPFILEHEADER;
     */
    private static BitmapFileHeader readFileHeader (DataInputStream reader) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2+4+2+2+4);
        buffer.putShort(reader.readShort());
        buffer.putInt(reader.readInt());
        buffer.putShort(reader.readShort());
        buffer.putShort(reader.readShort());
        buffer.putInt(reader.readInt());
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        short bfType = buffer.getShort(0);
        int bfSize = buffer.getInt(2);
        short bfReserved1 = buffer.getShort(6);
        short bfReserved2 = buffer.getShort(8);
        int bfOffBits = buffer.getInt(10);
        BitmapFileHeader bfh = new BitmapFileHeader(bfType, bfSize, bfReserved1, bfReserved2, bfOffBits);
        return bfh;
    }

    /*
      typedef struct tagBITMAPINFOHEADER
         {
           DWORD  biSize;
           LONG   biWidth;
           LONG   biHeight;
           WORD   biPlanes;
           WORD   biBitCount;
           DWORD  biCompression;
           DWORD  biSizeImage;
           LONG   biXPelsPerMeter;
           LONG   biYPelsPerMeter;
           DWORD  biClrUsed;
           DWORD  biClrImportant;
         } BITMAPINFOHEADER, *PBITMAPINFOHEADER;
     */
    private static BitmapInfoHeader readInfoHeader (DataInputStream reader) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4+4+4+2+2+4+4+4+4+4+4);
        buffer.putInt(reader.readInt()); // size
        buffer.putInt(reader.readInt()); // width
        buffer.putInt(reader.readInt()); // height
        buffer.putShort(reader.readShort()); // planes
        buffer.putShort(reader.readShort()); // bitcount
        buffer.putInt(reader.readInt()); // compression
        buffer.putInt(reader.readInt()); // size image
        buffer.putInt(reader.readInt()); // xppm
        buffer.putInt(reader.readInt()); // yppm
        buffer.putInt(reader.readInt()); // clrused
        buffer.putInt(reader.readInt()); // // clrimportant
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int biSize = buffer.getInt(0);
        int biWidth = buffer.getInt(4);
        int biHeight = buffer.getInt(8);
        short biPlanes = buffer.getShort(12);
        short biBitCount = buffer.getShort(14);
        int biCompression = buffer.getInt(16);
        int biSizeImage = buffer.getInt(20);
        int biXPelsPerMeter = buffer.getInt(24);
        int biYPelsPerMeter = buffer.getInt(28);
        int biClrUsed = buffer.getInt(32);
        int biClrImportant = buffer.getInt(36);

        BitmapInfoHeader bih = new BitmapInfoHeader(biSize, biWidth, biHeight, biPlanes,
                                                    biBitCount, biCompression, biSizeImage,
                                                    biXPelsPerMeter, biYPelsPerMeter, biClrUsed, biClrImportant);
        return bih;
    }

//    private static Color[] read24Bitmap (DataInputStream reader, int width, int heigth) throws IOException {
//        byte blue, green, red;
//        Color[] colors = new Color[width*heigth];
//        ByteBuffer buffer = ByteBuffer.allocate(1+1+1);
//        //int extraBytes = width % 4;
//        //byte[] ar = new byte[width*heigth];
//        //reader.read(ar, 0, width*heigth);
//        for (int j = heigth-1; j >= 0; j--) {
//            for (int i = 0; i < width; i++) {
//                buffer.put(0, reader.readByte());
//                buffer.put(1, reader.readByte());
//                buffer.put(2, reader.readByte());
//                buffer.order(ByteOrder.LITTLE_ENDIAN);
//
//                blue = buffer.get(0);
//                green = buffer.get(1);
//                red = buffer.get(2);
//                colors[j*width+i] = new Color(Byte.toUnsignedInt(red), Byte.toUnsignedInt(green), Byte.toUnsignedInt(blue));
//            }
//            reader.skipBytes(width % 4);
//        }
//        return colors;
//    }
    private static Color[] read24Bitmap (DataInputStream reader, int width, int height) throws IOException {
        byte blue, green, red;
        Color[] colors = new Color[width*height];
        ByteBuffer buffer = ByteBuffer.allocate((width*3+(width % 4))*height);

        reader.read(buffer.array(), 0, (width*3+(width % 4))*height);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        //int extraBytes = width % 4;
        //byte[] ar = new byte[width*heigth];
        //reader.read(ar, 0, width*heigth);
        int n = 0;
        for (int j = height-1; j >= 0; j--) {
            for (int i = 0; i < width; i++) {
                //buffer.put(0, reader.readByte());
                //buffer.put(1, reader.readByte());
                //buffer.put(2, reader.readByte());
                //buffer.order(ByteOrder.LITTLE_ENDIAN);

                blue = buffer.get(n); n++;
                green = buffer.get(n); n++;
                red = buffer.get(n); n++;
                colors[j*width+i] = new Color(Byte.toUnsignedInt(red), Byte.toUnsignedInt(green), Byte.toUnsignedInt(blue));
            }
            //reader.skipBytes(width % 4);
            n = n + width % 4;
        }
        return colors;
    }

    private static BufferedImage createImage (int width, int height, Color[] colors) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                bufferedImage.setRGB(i, j, colors[j*width + i].getRGB());
            }
        }
        return bufferedImage;
    }
}
