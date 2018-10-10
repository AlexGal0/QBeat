package android.bignerdranch.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectIngredient extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_view);

        ListView list = new ListView(SelectIngredient.this);

    }


    private class IngredientAdapter extends ArrayAdapter<Ingrediente>{
        public ArrayList<Ingrediente> ingredientes;
        public IngredientAdapter(ArrayList<Ingrediente> ingredientes){
            super(SelectIngredient.this, R.layout.single_ingredient, ingredientes);
            this.ingredientes = ingredientes;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.single_ingredient, null);
            }

            Ingrediente ingrediente = ingredientes.get(position);

            TextView textView = convertView.findViewById(R.id.name_ingredient);

            textView.setText(ingrediente.getName());

            return convertView;
        }
    }
}
