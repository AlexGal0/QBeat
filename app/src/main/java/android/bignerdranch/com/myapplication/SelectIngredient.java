package android.bignerdranch.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectIngredient extends ListFragment {
    public final static String SELECT_INGREDIENT = "android.bignerdranch.com.myapplication.select_ingredient";

    private ArrayList<Ingrediente> ingredientes;
    @Override
    public  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ingredientes = SetIngredientes.getListIngredientes();

        for(Ingrediente i: ingredientes){
            Log.i("INGREDIENTE", i.getName() + " " + i.getId());
        }

        Log.i("INGREDIENTE", ingredientes.size() + "");
        IngredientAdapter adapter = new IngredientAdapter(ingredientes);
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SetIngredientes.remove(ingredientes.get(position));
        getActivity().finish();
    }

    private class IngredientAdapter extends ArrayAdapter<Ingrediente>{
        public ArrayList<Ingrediente> ingredientes;
        public IngredientAdapter(ArrayList<Ingrediente> i){
            super(getActivity(), R.layout.single_ingredent_list, i );
            this.ingredientes = i;
        }



        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.single_ingredent_list, null);
            }

            Ingrediente ingrediente = ingredientes.get(position);

            Log.i("INGREDIENTE", "Adapter-sizeIngredients:  "+ ingredientes.size());

            TextView textView = convertView.findViewById(R.id.name_ingredient_list);

            textView.setText(ingrediente.getName());

            return convertView;
        }
    }
}
