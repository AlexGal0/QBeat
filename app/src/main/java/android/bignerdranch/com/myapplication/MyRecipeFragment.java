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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class MyRecipeFragment extends Fragment {
    public static final String TAG_RECIPE = "android.bignerdranch.com.myapplication.my_recipe.recipe";
    private ArrayList<Receta> recetas;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipe, container, false);
        Log.i("RECIPE_FRAGMENT", "OnCreateView()");
        return view;
    }

    private void updateRecipes(View view, LayoutInflater inflater){
        recetas = DataBase.getDataBase().getListReceta(User.id);
        LinearLayout linearLayout = view.findViewById(R.id.my_recipe_container);

        linearLayout.removeAllViews();
        Log.i("INGREDIENTE", recetas.size() + "");
        for(int i = 0; i < recetas.size(); i++){

            Receta r = recetas.get(i);
            View recipe_view = inflater.inflate(R.layout.single_recipe_list, null);

            Button textView = recipe_view.findViewById(R.id.my_recipe_name_list);

            textView.setText(r.getName());

            textView.setOnClickListener(new onRecipeListClick(i));

            linearLayout.addView(recipe_view);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((LinearLayout)getView().findViewById(R.id.my_recipe_container)).removeAllViews();
        updateRecipes(getView(), getLayoutInflater());
        Log.i("RECIPE_FRAGMENT", "OnResume()");
    }

    private class onRecipeListClick implements View.OnClickListener{
        private int index;
        public onRecipeListClick(int index){
            this.index = index;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RecipeView.class);
            intent.putExtra(TAG_RECIPE, index);

            startActivity(intent);
        }
    }
}
