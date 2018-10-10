package android.bignerdranch.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashSet;
import java.util.Set;

public class CreateRecipe extends FragmentActivity {
    private Set<Ingrediente> setIngredients = new HashSet<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipe);

        Button addIngredient = findViewById(R.id.add_ingredient_button);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateRecipe.this, SelectIngredient.class);
            }
        });
    }
}
