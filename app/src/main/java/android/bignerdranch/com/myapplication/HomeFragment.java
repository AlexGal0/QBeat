package android.bignerdranch.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FloatingActionButton addIngredient;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recipeView;


    private ArrayList<Receta> recetas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setRetainInstance(true);

        recipeView = view.findViewById(R.id.recicle_view_home);


        recipeView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recipeView.setLayoutManager(linearLayoutManager);
        recetas = DataBase.getDataBase().getListReceta();

        final AdapterRecycleViewHome adapter = new AdapterRecycleViewHome(recetas);


        recipeView.setAdapter(adapter);

        DataBase.db.collection(References.RECETAS_REFERENCE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ERROR", "Listen ERROR", e);
                    return;
                }
                Log.i("CHANGES", "NOTIFY CHANGE IN HOMEFRAGMENT");
                adapter.recetas = DataBase.getDataBase().getListReceta();
                adapter.notifyDataSetChanged();
            }
        });





        Switch switch1 = view.findViewById(R.id.switch1);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ViewPageFragment viewPager = (ViewPageFragment) container;
                viewPager.setEnable(b);
            }

        });

        floatingActionButton = view.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floatingActionButton.setEnabled(false);
                if(!DataBase.getDataBase().isNull()){
                    FragmentActivity activity = getActivity();
                    if(activity == null) {
                        Log.i("FAIL", "Fragment Activity in NULL");
                    }
                    Intent i = new Intent(activity, CreateRecipe.class);

                    activity.startActivity(i);
                }
            }
        });

        addIngredient = view.findViewById(R.id.floating_ingredient);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredient.setEnabled(false);
                if(!DataBase.getDataBase().isNull()){
                    FragmentActivity activity = getActivity();
                    if(activity == null) {
                        Log.i("FAIL", "Fragment Activity in NULL");
                    }
                    Intent i = new Intent(activity, Add_Ingredient.class);

                    activity.startActivity(i);
                }
                else
                    addIngredient.setEnabled(true);

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        floatingActionButton.setEnabled(true);
        addIngredient.setEnabled(true);

    }
}
