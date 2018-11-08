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

    public static Bitmap fixSize(byte[] bytes){
        Bitmap bit = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        int X = bit.getWidth();
        int Y = bit.getHeight();

        if(Y > X)
            bit = Bitmap.createBitmap(bit, 0, (Y-X) / 2, X, X);
        return bit;
    }
}
