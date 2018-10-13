package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class SelectIngredientActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_view);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = new SelectIngredient();

        manager.beginTransaction().add(R.id.ingredient_container, fragment)
                    .commit();

    }

    @Override
    public void onBackPressed() {
        Log.i("INGREDIENTE", "BACK PRESSED");
        super.onBackPressed();
    }
}
