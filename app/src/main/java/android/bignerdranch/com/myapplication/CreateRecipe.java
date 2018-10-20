package android.bignerdranch.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.ResultReceiver;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.primitives.Chars;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CreateRecipe extends FragmentActivity {
    public static final String TAG_INGREDIENTES = "android.qbeat.create_recipe.ingredientes";
    public static final String TAG_NUMBER_STEP = "android.qbeat.create_recipe.number_step";
    public static final String TAG_DESCRIPTION_STEP = "android.qbeat.create_recipe.description_step";




    public static ArrayList<Paso> pasos;
    public static ArrayList<Ingrediente> ingredientes;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipe);

        SetIngredientes.reset();
        pasos = new ArrayList<>();
        ingredientes = new ArrayList<>();


        final EditText name = findViewById(R.id.name_edit_text);
        final TextInputEditText description = findViewById(R.id.description_text);

        final Button addIngredient = findViewById(R.id.add_ingredient_button);
        Log.i("INGREDIENTE", SetIngredientes.size()+ "");
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateRecipe.this, SelectIngredientActivity.class);

                startActivityForResult(i, Activity.RESULT_OK);
            }
        });

        final Button addStep = findViewById(R.id.add_step);

        addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = findViewById(R.id.step_container);

                Paso step = new Paso("Ingrese descripcion");
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

        final Button create = findViewById(R.id.create_recipe_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create.setEnabled(false);
                if(name.getText().length() == 0)
                    Toast.makeText(getApplicationContext(), "Nombre vacio", Toast.LENGTH_SHORT).show();
                else if(description.getText().length() == 0)
                    Toast.makeText(getApplicationContext(), "Descricion vacia", Toast.LENGTH_SHORT).show();
                else if(ingredientes.size() == 0)
                    Toast.makeText(getApplicationContext(), "Ingredientes vacios", Toast.LENGTH_SHORT).show();
                else if(pasos.size() == 0)
                    Toast.makeText(getApplicationContext(), "Pasos vacios", Toast.LENGTH_SHORT).show();
                else{
                    Receta receta = new Receta();
                    receta.setName(name.getText().toString());
                    receta.setDescription(description.getText().toString());
                    receta.setIngredientes(ingredientes);
                    receta.setPasos(pasos);



                    ingredientes = null;
                    pasos = null;

                    DataBase.getDataBase().addRecipe(receta);
                    Toast.makeText(getApplicationContext(), "Receta creada exitosamente!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

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


}
