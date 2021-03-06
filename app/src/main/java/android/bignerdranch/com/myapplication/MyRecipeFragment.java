package android.bignerdranch.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class MyRecipeFragment extends Fragment {
    public static final String TAG_RECIPE = "android.bignerdranch.com.myapplication.my_recipe.recipe";
    private ArrayList<Receta> recetas;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_my_recipe, container, false);
        Log.i("RECIPE_FRAGMENT", "OnCreateView()");
        return view;
    }

    private void updateRecipes(View view, LayoutInflater inflater){
        recetas = DataBase.getDataBase().userTree.get(DataBase.getDataBase().currentUser.id);

        LinearLayout linearLayout = view.findViewById(R.id.my_recipe_container);

        linearLayout.removeAllViews();
        if(recetas == null)
            return;
        Log.i("INGREDIENTE", recetas.size() + "");

        Collections.sort(recetas, new Comparator<Receta>() {
            @Override
            public int compare(Receta receta, Receta t1) {
                return receta.getName().compareTo(t1.getName());
            }
        });

        for(int i = 0; i < recetas.size(); i++){
            Receta r = recetas.get(i);
            View recipe_view = inflater.inflate(R.layout.single_recipe_list, null);

            Button textView = recipe_view.findViewById(R.id.my_recipe_name_list);

            textView.setText(r.getName());

            textView.setOnClickListener(new onRecipeListClick(r));

            linearLayout.addView(recipe_view);
        }

        if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals("V7bVcf7wQrOayyKzYX4ofeLPGeC3")){
            TextView textView = view.findViewById(R.id.invitado_text);
            textView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        DataBase.getDataBase().setCurrentRecipe(null);
        Log.i("RECIPE_FRAGMENT", "OnResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        notifyDataChange();
    }

    public void notifyDataChange() {
        ((LinearLayout)getView().findViewById(R.id.my_recipe_container)).removeAllViews();
        updateRecipes(getView(), getLayoutInflater());
        Log.i("RECIPE_FRAGMENT", "NotifyDataChange()");

    }

    public class onRecipeListClick implements View.OnClickListener{
        private Receta receta;
        public onRecipeListClick(Receta receta){
            this.receta = receta;
        }
        @Override
        public void onClick(View view) {
            if(DataBase.getDataBase().getCurrentRecipe() == null) {
                DataBase.getDataBase().setCurrentRecipe(receta);
                Intent intent = new Intent(view.getContext(), RecipeView.class);
                startActivity(intent);
            }
        }
    }


}
