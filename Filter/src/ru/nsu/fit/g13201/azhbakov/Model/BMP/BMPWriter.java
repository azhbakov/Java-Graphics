package ru.nsu.fit.g13201.azhbakov.model.BMP;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Martin on 21.03.2016.
 */
public class BMPWriter {
    private static short BM = 19778;

    public static void saveBMP (File f, BufferedImage image) throws IOException, BadFileException {
        DataOutputStream writer = new DataOutputStream(new FileOutputStream(f));
        writeFileHeader(writer, image);
        writeInfoHeader(writer, image);
        write24Bitmap(writer, image);
        writer.close();
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
    private static void writeFileHeader (DataOutputStream writer, BufferedImage image) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2+4+2+2+4);
        buffer.putShort(BM);

        int lineLen;
        if (image.getWidth()%4 == 0) lineLen = image.getWidth()*3;
        else lineLen = image.getWidth()*3 + image.getWidth()%4;

        buffer.putInt(54+lineLen*image.getHeight()); // AA CALC SIZE
        buffer.putShort((short)0);
        buffer.putShort((short)0);
        buffer.putInt(54);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        writer.writeShort(buffer.getShort(0));
        writer.writeInt(buffer.getInt(2));
        writer.writeShort(buffer.getShort(6));
        writer.writeShort(buffer.getShort(8));
        writer.writeInt(buffer.getInt(10));
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
    private static void writeInfoHeader (DataOutputStream writer, BufferedImage image) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4+4+4+2+2+4+4+4+4+4+4);
        buffer.putInt(40); // structure size
        buffer.putInt(image.getWidth()); // width
        buffer.putInt(image.getHeight()); // height
        buffer.putShort((short)1); // planes
        buffer.putShort((short)24); // bitcount
        buffer.putInt(0); // compression
        int lineLen;
        if (image.getWidth()%4 == 0) lineLen = image.getWidth()*3;
        else lineLen = image.getWidth()*3 + image.getWidth()%4;
        buffer.putInt(image.getHeight()*lineLen+54); // size image
        buffer.putInt(0); // xppm
        buffer.putInt(0); // yppm
        buffer.putInt(0); // clrused
        buffer.putInt(0); // // clrimportant
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        writer.writeInt(buffer.getInt(0));
        writer.writeInt(buffer.getInt(4));
        writer.writeInt(buffer.getInt(8));
        writer.writeShort(buffer.getShort(12));
        writer.writeShort(buffer.getShort(14));
        writer.writeInt(buffer.getInt(16));
        writer.writeInt(buffer.getInt(20));
        writer.writeInt(buffer.getInt(24));
        writer.writeInt(buffer.getInt(28));
        writer.writeInt(buffer.getInt(32));
        writer.writeInt(buffer.getInt(36));
    }

    private static void write24Bitmap (DataOutputStream writer, BufferedImage image) throws IOException {
        byte blue, green, red;
        ByteBuffer buffer = ByteBuffer.allocate(3);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        //int extraBytes = width % 4;
        //byte[] ar = new byte[width*heigth];
        //reader.read(ar, 0, width*heigth);
        for (int j = image.getHeight()-1; j >= 0; j--) {
            for (int i = 0; i < image.getWidth(); i++) {
                //buffer.put(0, reader.readByte());
                //buffer.put(1, reader.readByte());
                //buffer.put(2, reader.readByte());
                //buffer.order(ByteOrder.LITTLE_ENDIAN);
                Color c = new Color(image.getRGB(i, j));
                buffer.put(0, (byte)c.getBlue());
                buffer.put(1, (byte)c.getGreen());
                buffer.put(2, (byte)c.getRed());
                writer.writeByte(buffer.get(0));
                writer.writeByte(buffer.get(1));
                writer.writeByte(buffer.get(2));
            }
            //reader.skipBytes(width % 4);
            for (int i = 0; i < image.getWidth()%4; i++) {
                writer.writeByte(0);
            }
        }
    }
}
