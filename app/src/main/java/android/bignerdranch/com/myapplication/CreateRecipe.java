package android.bignerdranch.com.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.primitives.Chars;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CreateRecipe extends FragmentActivity {
    public static final String TAG_INGREDIENTES = "android.qbeat.create_recipe.ingredientes";
    public static final String TAG_NUMBER_STEP = "android.qbeat.create_recipe.number_step";
    public static final String TAG_DESCRIPTION_STEP = "android.qbeat.create_recipe.description_step";
    private static final int RESULT_LOAD_IMAGE = 12;

    public static ArrayList<Paso> pasos;
    public static ArrayList<Ingrediente> ingredientes;


    private EditText name;
    private TextInputEditText description;
    private Button addIngredient;
    private Button addStep;
    private Button create;

    private ImageView recipeImage;
    private ProgressBar progressBar;

    private Uri imageUri;



    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipe);

        SetIngredientes.reset();
        pasos = new ArrayList<>();
        ingredientes = new ArrayList<>();


        name = findViewById(R.id.name_edit_text);


        description = findViewById(R.id.description_text);

        addIngredient = findViewById(R.id.add_ingredient_button);
        Log.i("INGREDIENTE", SetIngredientes.size()+ "");
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateRecipe.this, SelectIngredientActivity.class);

                startActivityForResult(i, Activity.RESULT_OK);
            }
        });

        addStep = findViewById(R.id.add_step);

        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = findViewById(R.id.step_container);

                Paso step = new Paso();
                pasos.add(step);
                View newStep = getLayoutInflater().inflate(R.layout.single_step_list, null);

                TextView numberStep = newStep.findViewById(R.id.number_step);
                numberStep.setText((linearLayout.getChildCount() + 1) + "");

                //Log.i("INGREDIENTE", linearLayout.getChildCount() + "");
                TextView descriptionStep = newStep.findViewById(R.id.description_step);

                descriptionStep.setText(step.getDescription());


                ImageButton removeButton = newStep.findViewById(R.id.remove_step);
                removeButton.setOnClickListener(new RemoveStepClick(linearLayout, newStep));

                ImageButton editButton = newStep.findViewById(R.id.edit_step);
                editButton.setOnClickListener(new EditStepClick(linearLayout, newStep, step));
                linearLayout.addView(newStep);
            }
        });


        progressBar = findViewById(R.id.progressBarCreateRecipe);
        progressBar.setVisibility(View.GONE);

        recipeImage = findViewById(R.id.image_create_recipe);

        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });




        create = findViewById(R.id.create_recipe_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                if(name.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "Nombre vacio", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
                else if(description.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Descricion vacia", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
                else if(ingredientes.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Ingredientes vacios", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
                else if(pasos.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Pasos vacios", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    final Receta receta = new Receta();
                    receta.setName(name.getText().toString());
                    receta.setDescription(description.getText().toString());
                    receta.setIngredientes(ingredientes);
                    receta.setPasos(pasos);
                    receta.setCreate(new Date(System.currentTimeMillis()));
                    receta.setChefName(DataBase.getDataBase().currentUser.getName());


                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();



                    if(imageUri != null){

                        final StorageReference riversRef = storageRef.child("Recetas Images/" + imageUri.getLastPathSegment() + System.currentTimeMillis());

                        receta.setRecipeImage(riversRef.getName());

                        ingredientes = null;
                        pasos = null;


                        final UploadTask uploadTask = riversRef.putFile(imageUri);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(CreateRecipe.this, "Fallo al crear la receta", Toast.LENGTH_SHORT).show();
                                DataBase.getDataBase().removeRecipe(receta);
                                progressBar.setVisibility(View.GONE);

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressBar.setVisibility(View.GONE);
                                Log.i("Upload Image", riversRef.getName());
                                Toast.makeText(getApplicationContext(), "Receta creada exitosamente!", Toast.LENGTH_SHORT).show();
                                DataBase.getDataBase().addRecipe(receta);
                                finish();
                            }
                        });
                    }
                    else{
                        DataBase.getDataBase().addRecipe(receta);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Receta creada exitosamente!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();

            InputStream iStream = null;
            byte[] bit = null;
            try {
                iStream = getContentResolver().openInputStream(imageUri);
                bit = Util.getBytes(iStream);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if(bit == null || bit.length > 1024 * 1024 * 2){
                Toast.makeText(this, "La imagen excede el tamaño", Toast.LENGTH_SHORT).show();
                imageUri = null;
                return;
            }
            recipeImage.setImageURI(imageUri);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SetIngredientes.removed != null)
          inflateIngrediente();

        updateStep();
        updateIngredient();
    }

    private void updateIngredient() {
        LinearLayout linearLayout = findViewById(R.id.container_ingredient);
        Collections.sort(ingredientes);

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            View view = linearLayout.getChildAt(i);

            TextView name_ingredient = view.findViewById(R.id.name_ingredient);
            name_ingredient.setText(ingredientes.get(i).getName());
        }
    }

    private void updateStep(){
        LinearLayout linearLayout = findViewById(R.id.step_container);

        for(int i = 0; i < linearLayout.getChildCount(); i++){
            View view = linearLayout.getChildAt(i);

            TextView number = view.findViewById(R.id.number_step);
            number.setText((i + 1) + "");

            TextView description = view.findViewById(R.id.description_step);

            //if(pasos.get(i).getDescription().trim().length() != 0)
                description.setText(pasos.get(i).getDescription());
        }
    }


    private void inflateIngrediente() {
        final Ingrediente ingrediente = SetIngredientes.removed;
        SetIngredientes.removed = null;
        Log.i("INGREDIENTE", "RESULT");

        Log.i("INGREDIENTE", ingrediente.getName()+ "");


        LinearLayout linearLayout = findViewById(R.id.container_ingredient);

        View view = getLayoutInflater().inflate(R.layout.single_ingredient, null);



        TextView name = view.findViewById(R.id.name_ingredient);
        name.setText(ingrediente.getName());

        ImageButton cancel = view.findViewById(R.id.remove_ingredient_button);

        linearLayout.addView(view);
        cancel.setOnClickListener(new RemoveIngredientClick(ingrediente, linearLayout, view));
        ingredientes.add(ingrediente);

    }

    private class RemoveIngredientClick implements View.OnClickListener{
        private  Ingrediente ingrediente;
        private  LinearLayout linearLayout;
        private  View root;
        public RemoveIngredientClick(Ingrediente ingrediente, LinearLayout linearLayout, View view){
            this.ingrediente = ingrediente;
            this.linearLayout = linearLayout;
            this.root = view;
        }
        @Override
        public void onClick(View view) {
            Log.i("INGREDIENTE", "BUTTON PRESSED");
            SetIngredientes.add(ingrediente);
            ingredientes.remove(ingrediente);
            linearLayout.removeView(root);
        }
    }

    private class RemoveStepClick implements View.OnClickListener{
        private  LinearLayout linearLayout;
        private  View root;
        public RemoveStepClick(LinearLayout linearLayout, View view){
            this.linearLayout = linearLayout;
            this.root = view;
        }
        @Override
        public void onClick(View view) {
            Log.i("INGREDIENTE", "BUTTON PRESSED");
            int index = linearLayout.indexOfChild(root);
            pasos.remove(index);
            linearLayout.removeView(root);
            updateStep();
        }
    }

    private class EditStepClick implements View.OnClickListener{
        private  LinearLayout linearLayout;
        private  View root;
        private  Paso step;
        public EditStepClick(LinearLayout linearLayout, View view, Paso step){
            this.linearLayout = linearLayout;
            this.root = view;
            this.step = step;
        }
        @Override
        public void onClick(View view) {
            Log.i("INGREDIENTE", "BUTTON PRESSED");

            Intent i = new Intent(linearLayout.getContext(), EditStepActivity.class);

            i.putExtra(TAG_NUMBER_STEP, linearLayout.indexOfChild(root));

            startActivity(i);
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRecipe.this);

        builder.setTitle("¿Desea salir?");
        builder.setMessage("Se perdera todo el progreso");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}
