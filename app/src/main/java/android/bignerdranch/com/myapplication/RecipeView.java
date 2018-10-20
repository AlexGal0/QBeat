package android.bignerdranch.com.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipeView extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);

        int index = getIntent().getIntExtra(MyRecipeFragment.TAG_RECIPE, -1);

        final Receta receta = DataBase.getDataBase().getListReceta(User.id).get(index);

        ArrayList<Ingrediente>  listIngredients = receta.getIngredientes();
        ArrayList<Paso>         listStep       = receta.getPasos();


        final TextView name = findViewById(R.id.name_recipe_view);
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


        final Button delete = findViewById(R.id.delete_recipe_button);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                delete.setEnabled(false);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("¿Desea eliminar la receta : " + receta.getName() + "?");
                builder.setMessage("La receta se va a eliminar para siempre y no se podra recuperar");
                builder.setCancelable(false);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataBase.getDataBase().removeRecipe(receta);
                        Toast.makeText(view.getContext(), "La receta ha sido eliminada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete.setEnabled(true);
                    }
                });

                builder.show();
            }
        });
    }
}
