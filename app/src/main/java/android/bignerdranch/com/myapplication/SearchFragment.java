package android.bignerdranch.com.myapplication;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchFragment extends Fragment {

    private EditText searchBar;
    private ImageButton searchButton;
    private RecyclerView recyclerView;
    private AdapterSearchFragment adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView2;
    private AdapterSearchFragmentUser adapter2;
    private ProgressBar progressBar2;
    private TextView badText;
    private TextView badTex2;



    ArrayList<Receta> recetas;
    ArrayList<Usuario> usuarios;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchBar = view.findViewById(R.id.search_bar);
        searchButton = view.findViewById(R.id.search_button);
        recyclerView = view.findViewById(R.id.search_recycle);
        progressBar = view.findViewById(R.id.search_progressbar);
        recyclerView2 = view.findViewById(R.id.search_recycle_user);
        progressBar2 = view.findViewById(R.id.search_progressbar2);
        badText = view.findViewById(R.id.bad_search_recipes);
        badTex2 = view.findViewById(R.id.bad_search_recipes2);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recetas = new ArrayList<>();
        adapter = new AdapterSearchFragment(recetas);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(linearLayoutManager2);

        usuarios = new ArrayList<>();
        adapter2 = new AdapterSearchFragmentUser(usuarios);
        recyclerView2.setAdapter(adapter2);


        final GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                boolean buttonState = e.isButtonPressed(0);
                Log.i("CLICK", buttonState + "");
                return true;
            }
        });



        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View view= rv.findChildViewUnder(e.getX(),e.getY());


                if(view != null && mGestureDetector.onTouchEvent(e)){
                    final int index = rv.getChildAdapterPosition(view);
                    Receta receta = recetas.get(index);
                    if(DataBase.getDataBase().getCurrentRecipe() == null) {
                        DataBase.getDataBase().setCurrentRecipe(receta);
                        Intent intent = new Intent(getContext(), RecipeView.class);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });


        recyclerView2.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View view= rv.findChildViewUnder(e.getX(),e.getY());

                if(view != null && mGestureDetector.onTouchEvent(e)){
                    final int index = rv.getChildAdapterPosition(view);
                    Usuario usuario = usuarios.get(index);
                    if(DataBase.getDataBase().getCurrentRecipe() == null) {
                        Intent intent = new Intent(SearchFragment.this.getContext(), UserView.class);
                        intent.putExtra(UserView.USER_TAG, usuario.id);
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });




        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchBar.getText().toString().trim().length() == 0){
                    Toast.makeText(SearchFragment.this.getContext(), "Busqueda vacia", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseSearch();
            }
        });

        return view;
    }

    private void firebaseSearch() {
        recetas.clear();
        usuarios.clear();
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        badText.setVisibility(View.GONE);
        badTex2.setVisibility(View.GONE);



        String search = searchBar.getText().toString();


        ArrayList<Receta> r = DataBase.getDataBase().getListReceta();

        for(Receta receta: r){
            if(receta.getName().toLowerCase().startsWith(search.toLowerCase()))
                recetas.add(receta);
        }

        Collections.sort(recetas, new Comparator<Receta>() {
            @Override
            public int compare(Receta receta, Receta t1) {
                return receta.getName().compareTo(t1.getName());
            }
        });

        adapter.notifyDataSetChanged();



        ArrayList<Usuario> u = DataBase.getDataBase().getlistUsers();

        for(Usuario usuario: u){
            if(usuario.getName().toLowerCase().startsWith(search.toLowerCase()) && !usuario.id.equals(DataBase.getDataBase().currentUser.id))
                usuarios.add(usuario);
        }

        Collections.sort(usuarios, new Comparator<Usuario>() {
            @Override
            public int compare(Usuario receta, Usuario t1) {
                return receta.getName().compareTo(t1.getName());
            }
        });



        adapter2.notifyDataSetChanged();

        if(recetas.size() == 0)
            badText.setVisibility(View.VISIBLE);
        if(usuarios.size() == 0)
            badTex2.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
    }

}
