package android.bignerdranch.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

public class ImageViewCompleteFragment extends FragmentActivity {
    public static final String KEY_IMAGE = "android.bignerdranch.com.myapplication.KEY";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_complete_view);

        ImageView imageView = findViewById(R.id.image_complete);

        byte[] bit = getIntent().getByteArrayExtra(KEY_IMAGE);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bit, 0, bit.length));
    }
}
