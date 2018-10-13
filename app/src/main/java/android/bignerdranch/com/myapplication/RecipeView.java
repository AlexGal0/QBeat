package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeView extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);

        int index = getIntent().getIntExtra(MyRecipeFragment.TAG_RECIPE, -1);

        Receta receta = DataBase.getDataBase().getListReceta(User.id).get(index);

        ArrayList<Ingrediente>  listIngredients = receta.getIngredientes();
        ArrayList<Paso>         listStep       = receta.getPasos();


        TextView name = findViewById(R.id.name_recipe_view);
        TextView description = findViewById(R.id.description_recipe_view);

        LinearLayout ingredientsContainer   = findViewById(R.id.ingredients_container_recipe_view);
        LinearLayout stepContainer          = findViewById(R.id.step_container_recipe_view);

        name.setText(receta.getName());

        description.setText(receta.getDescription());

        for(Ingrediente ingrediente: listIngredients){
            TextView text = new TextView(this);
            text.setText(ingrediente.getName());
            ingredientsContainer.addView(text);
        }

        for(int i = 0; i < listStep.size(); i++){
            TextView text = new TextView(this);
            text.setText((i+1) + ": " + listStep.get(i).getDescription());
            stepContainer.addView(text);
        }

    }
}
