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
import android.widget.SeekBar;
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
    public static final String TAG_TYPE = "android.qbeat.create_recipe.type";
    public static final String TAG_NUMBER_STEP = "android.qbeat.create_recipe.number_step";
    public static final String TAG_DESCRIPTION_STEP = "android.qbeat.create_recipe.description_step";
    private static final int RESULT_LOAD_IMAGE = 12;

    private int type;


    private EditText name;
    private TextInputEditText description;
    private Button addIngredient;
    private Button addStep;
    private Button addTag;

    private Button create;
    private Button update;

    private SeekBar dificultPicker;
    private SeekBar timePicker;


    TextView dificultView;
    TextView timeView;



    private ImageView recipeImage;
    private ProgressBar progressBar;
    private ImageButton removeImage;
    private LinearLayout tagContainer;


    private Uri imageUri;

    private Receta receta;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipe);

        type = getIntent().getIntExtra(TAG_TYPE, -1);

        create = findViewById(R.id.create_recipe_button);
        update = findViewById(R.id.update_recipe_button);

        DataBase.getDataBase().currTags = DataBase.getDataBase().getTags();
        if(type == 0){
            receta = new Receta(DataBase.getDataBase().currentUser.id);
            DataBase.getDataBase().rest = DataBase.getDataBase().getListIngredients();
            create.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);

        }
        else if(type == 1){
            receta = DataBase.getDataBase().receta;
            DataBase.getDataBase().rest = DataBase.getDataBase().getListIngredients();
            DataBase.getDataBase().rest.removeAll(receta.getIngredientes());

            create.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
        }

        DataBase.getDataBase().receta = receta;
        name = findViewById(R.id.name_edit_text);
        description = findViewById(R.id.description_text);
        addIngredient = findViewById(R.id.add_ingredient_button);
        removeImage = findViewById(R.id.remove_image_button);


        dificultPicker = findViewById(R.id.picker_dificult);
        dificultView = findViewById(R.id.dificult_view_create_recipe);

        dificultPicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dificultView.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                receta.setDificult(seekBar.getProgress());
            }
        });


        timePicker = findViewById(R.id.picker_time);
        timeView = findViewById(R.id.time_view_create_recipe);

        timePicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timeView.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                receta.setTime(seekBar.getProgress());
            }
        });

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredient.setEnabled(false);
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
                receta.addPaso(step);
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

        addTag = findViewById(R.id.add_tag);

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagContainer = findViewById(R.id.tags_container);

                Intent i = new Intent(CreateRecipe.this, SelectTag.class);
                startActivity(i);
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




        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create.setEnabled(false);

                if(name.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Nombre vacio", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);
                }
                else if(description.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Descricion vacia", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);
                }
                else if(receta.getIngredientes().size() == 0) {
                    Toast.makeText(getApplicationContext(), "Ingredientes vacios", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);
                }
                else if(receta.getPasos().size() == 0) {
                    Toast.makeText(getApplicationContext(), "Pasos vacios", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);
                }
                else if(receta.getTags() == null) {
                    Toast.makeText(getApplicationContext(), "Tag vacio", Toast.LENGTH_SHORT).show();
                    update.setEnabled(true);
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();


                    receta.setName(name.getText().toString());
                    receta.setDescription(description.getText().toString());
                    receta.setCreate(new Date(System.currentTimeMillis()));
                    receta.setChefName(DataBase.getDataBase().currentUser.getName());


                    if(imageUri != null){

                        final StorageReference riversRef = storageRef.child("Recetas Images/" + imageUri.getLastPathSegment() + System.currentTimeMillis());

                        receta.setRecipeImage(riversRef.getName());
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


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update.setEnabled(false);

                if(name.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Nombre vacio", Toast.LENGTH_SHORT).show();
                    update.setEnabled(true);
                }
                else if(description.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Descricion vacia", Toast.LENGTH_SHORT).show();
                    update.setEnabled(true);
                }
                else if(receta.getIngredientes().size() == 0) {
                    Toast.makeText(getApplicationContext(), "Ingredientes vacios", Toast.LENGTH_SHORT).show();
                    update.setEnabled(true);
                }
                else if(receta.getPasos().size() == 0) {
                    Toast.makeText(getApplicationContext(), "Pasos vacios", Toast.LENGTH_SHORT).show();
                    update.setEnabled(true);
                }
                else if(receta.getTags() == null) {
                    Toast.makeText(getApplicationContext(), "Tag vacio", Toast.LENGTH_SHORT).show();
                    update.setEnabled(true);
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();


                    receta.setName(name.getText().toString());
                    receta.setDescription(description.getText().toString());
                    receta.setChefName(DataBase.getDataBase().currentUser.getName());


                    if(imageUri != null){

                        final StorageReference riversRef = storageRef.child("Recetas Images/" + imageUri.getLastPathSegment() + System.currentTimeMillis());

                        receta.setRecipeImage(riversRef.getName());
                        final UploadTask uploadTask = riversRef.putFile(imageUri);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(CreateRecipe.this, "Fallo al actulizar la receta", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressBar.setVisibility(View.GONE);
                                Log.i("Upload Image", riversRef.getName());
                                Toast.makeText(getApplicationContext(), "Receta actualizar exitosamente!", Toast.LENGTH_SHORT).show();
                                DataBase.getDataBase().addRecipe(receta);
                                finish();
                            }
                        });
                    }
                    else{
                        DataBase.getDataBase().addRecipe(receta);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Receta actualizada exitosamente!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
        });


        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = null;
                recipeImage.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_report_image));
                receta.setImage(null);
                receta.setRecipeImage(null);
                removeImage.setVisibility(View.GONE);
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

            recipeImage.setImageBitmap(Util.fixSizeRectangle(bit));
            receta.setImage(bit);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addIngredient.setEnabled(true);
        receta = DataBase.getDataBase().receta;
        updateView();
    }

    private void updateView() {
        if(receta.getName() != null)
            name.setText(receta.getName());
        if(receta.getDescription() != null)
            description.setText(receta.getDescription());

        updateIngredient();
        updateStep();
        updateTags();

        dificultPicker.setProgress(receta.getDificult());
        timePicker.setProgress(receta.getTime());

        removeImage.setVisibility(View.VISIBLE);
        if(receta.getImage() != null){
            recipeImage.setImageBitmap(Util.fixSizeRectangle(receta.getImage()));
        }
        else if(imageUri == null)
            removeImage.setVisibility(View.GONE);

    }

    private void updateTags() {
        LinearLayout linearLayout = findViewById(R.id.tags_container);
        linearLayout.removeAllViews();
        String tag = receta.getTags();

        if(tag == null){
            addTag.setVisibility(View.VISIBLE);
            return;
        }


        addTag.setVisibility(View.GONE);
        View view = getLayoutInflater().inflate(R.layout.single_element_tag_crete_recipe, null);
        TextView tagsName = view.findViewById(R.id.name_tag);
        tagsName.setText(tag);

        ImageButton cancel = view.findViewById(R.id.remove_tag);

        linearLayout.addView(view);
        cancel.setOnClickListener(new RemoveTagClick(tag, linearLayout, view));


    }

    private void updateIngredient() {
        LinearLayout linearLayout = findViewById(R.id.container_ingredient);
        linearLayout.removeAllViews();
        ArrayList<Ingrediente> ingredientes = receta.getIngredientes();

        if(ingredientes == null)
            receta.setIngredientes(ingredientes = new ArrayList<>());

        Collections.sort(ingredientes);

        for(int i = 0; i < ingredientes.size(); i++){
            Ingrediente ingrediente = ingredientes.get(i);
            View view = getLayoutInflater().inflate(R.layout.single_ingredient, null);

            TextView name_ingredient = view.findViewById(R.id.name_ingredient);
            name_ingredient.setText(ingrediente.getName());

            ImageButton cancel = view.findViewById(R.id.remove_ingredient_button);

            linearLayout.addView(view);
            cancel.setOnClickListener(new RemoveIngredientClick(ingrediente, linearLayout, view));
        }
    }

    private void updateStep(){
        LinearLayout linearLayout = findViewById(R.id.step_container);
        linearLayout.removeAllViews();

        ArrayList<Paso> pasos = receta.getPasos();

        if(pasos == null)
            receta.setPasos(pasos = new ArrayList<>());

        Log.i("COUNTSTEP", pasos.size() + "");
        for(int i = 0; i < pasos.size(); i++){
            View view = getLayoutInflater().inflate(R.layout.single_step_list, null);

            TextView number = view.findViewById(R.id.number_step);
            number.setText((i + 1) + "");

            TextView description = view.findViewById(R.id.description_step);

            description.setText(pasos.get(i).getDescription());
            linearLayout.addView(view);


            ImageButton removeButton = view.findViewById(R.id.remove_step);
            removeButton.setOnClickListener(new RemoveStepClick(linearLayout, view));

            ImageButton editButton = view.findViewById(R.id.edit_step);
            editButton.setOnClickListener(new EditStepClick(linearLayout, view, pasos.get(i)));
        }
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
            receta.getIngredientes().remove(ingrediente);
            DataBase.getDataBase().rest.add(ingrediente);
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
            receta.removePaso(index);
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

            DataBase.getDataBase().receta = receta;
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

    private class RemoveTagClick implements View.OnClickListener {
        private final String tag;
        private final LinearLayout linearLayour;
        private final View root;

        public RemoveTagClick(String tag, LinearLayout linearLayout, View view) {
            this.tag = tag;
            this.linearLayour = linearLayout;
            this.root = view;
        }

        @Override
        public void onClick(View view) {
            receta.setTags(null);
            DataBase.getDataBase().currTags.add(tag);
            linearLayour.removeView(root);
            addTag.setVisibility(View.VISIBLE);
        }
    }
}
