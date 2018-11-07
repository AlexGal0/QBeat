package android.bignerdranch.com.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecipeView extends FragmentActivity {

    public static RecipeView recipeView;

    private ImageView recipeImage;
    private ProgressBar progressBar;
    private TextView fecha;

    public byte[] bit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeView = this;
        setContentView(R.layout.recipe_view);


        int index = getIntent().getIntExtra(MyRecipeFragment.TAG_RECIPE, -1);

        final Receta receta = DataBase.getDataBase().getListReceta().get(index);

        ArrayList<Ingrediente>  listIngredients = receta.getIngredientes();
        ArrayList<Paso>         listStep       = receta.getPasos();

        progressBar = findViewById(R.id.image_progress);
        recipeImage = findViewById(R.id.image_recipe_view);
        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeImage.setEnabled(false);
                Intent i = new Intent(RecipeView.this, ImageViewCompleteFragment.class);
                i.putExtra(ImageViewCompleteFragment.KEY_IMAGE, 2);
                startActivity(i);
            }
        });

        recipeImage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if(receta.getRecipeImage() == null)
            progressBar.setVisibility(View.GONE);
        else {
            if(receta.getImage() == null){
                StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Recetas Images/"+ receta.getRecipeImage());

                final long ONE_MEGABYTE = 1024 * 1024 * 2;
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bit = bytes;
                        receta.setImage(bytes);

                        recipeImage.setImageBitmap(Util.fixSize(bytes));
                        progressBar.setVisibility(View.GONE);
                        recipeImage.setVisibility(View.VISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
            else{
                bit = receta.getImage();
                recipeImage.setImageBitmap(Util.fixSize(receta.getImage()));
                progressBar.setVisibility(View.GONE);
                recipeImage.setVisibility(View.VISIBLE);
            }

        }

        final TextView name = findViewById(R.id.name_recipe_view);
        TextView description = findViewById(R.id.description_recipe_view);

        fecha = findViewById(R.id.fecha_recipe_view);
        fecha.setText(new SimpleDateFormat("yy/MM/dd HH:mm").format(receta.getCreate()));

        LinearLayout ingredientsContainer   = findViewById(R.id.ingredients_container_recipe_view);
        LinearLayout stepContainer          = findViewById(R.id.step_container_recipe_view);

        name.setText(receta.getName());

        description.setText(receta.getDescription());

        for(Ingrediente ingrediente: listIngredients){
            TextView text = new TextView(this);
            text.setPadding(5,2,5,2);
            text.setText(ingrediente.getName());
            ingredientsContainer.addView(text);
        }

        for(int i = 0; i < listStep.size(); i++){
            TextView text = new TextView(this);
            text.setPadding(5,25,5,5);
            text.setText((i+1) + ": " + listStep.get(i).getDescription());
            stepContainer.addView(text);
        }


        final Button delete = findViewById(R.id.delete_recipe_button);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                delete.setEnabled(false);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("¿Desea eliminar la receta: " + receta.getName() + "?");
                builder.setMessage("La receta se va a eliminar para siempre y no se podra recuperar");
                builder.setCancelable(false);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataBase.getDataBase().removeRecipe(receta);
                        if(receta.getRecipeImage() != null){
                            FirebaseStorage.getInstance().getReference().child("Recetas Images/" + receta.getRecipeImage()).delete();
                        }

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

    @Override
    protected void onResume() {
        super.onResume();
        recipeImage.setEnabled(true);
    }
}
