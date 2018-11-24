package android.bignerdranch.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static Bitmap fixSize(byte[] bytes, int fix){
        Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        int X = bit.getWidth();
        int Y = Math.min(bit.getHeight(), fix);

        if(Y > X)
            bit = Bitmap.createBitmap(bit, 0, (Y-X) / 2, X, X);
        return bit;
    }

    public static Bitmap fixSize(byte[] bytes){
        Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        int X = bit.getWidth();
        int Y = bit.getHeight();

        if(Y > X)
            bit = Bitmap.createBitmap(bit, 0, (Y-X) / 2, X, X);
        return bit;
    }

    public static Bitmap fixSizeTo(byte[] bytes, int weight, int height){
        Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return Bitmap.createScaledBitmap(bit, weight,  height, true);
    }

    public static Bitmap scaleTo(byte[] bytes, int scale){
        Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        int width  = bit.getWidth();
        int height = bit.getHeight();

        double div = scale / (double) height;

        return Bitmap.createScaledBitmap(bit, (int)(width * div) ,  (int)(height * div), true);
    }


    public static Bitmap fixSizeRectangle(byte[] bytes) {
        Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        int X = bit.getWidth();
        int Y = bit.getHeight();

        int min = Math.min(X,Y);

        if(Y > X)
            bit = Bitmap.createBitmap(bit, 0, 0, min, min);
        return bit;
    }
}
