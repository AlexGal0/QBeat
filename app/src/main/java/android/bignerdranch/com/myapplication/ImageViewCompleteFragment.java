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

        int num = getIntent().getIntExtra(KEY_IMAGE, -1);
        byte[] bit = null;
        if(num == 1)
            bit = ProfileFragment.profileFragment.bit;
        else if(num == 2)
            bit = RecipeView.recipeView.bit;

        if(bit == null){
            finish();
        }
        else{
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bit, 0, bit.length));

        }
    }
}
