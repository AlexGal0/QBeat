package android.bignerdranch.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserView extends FragmentActivity {
    public static String USER_TAG = "android.bignerdranch.com.myapplication.USER_TAG";

    private ProgressBar progressBar;
    private Usuario usuario;
    private ImageView imageView;
    private ProgressBar imageProgressBar;
    private TextView chefName;
    private TextView chefLevel;
    private LinearLayout container;
    private ArrayList<Receta> recetas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);
        String chef_id = getIntent().getStringExtra(USER_TAG);

        progressBar = findViewById(R.id.user_progress_bar);

        usuario = DataBase.getDataBase().getUser(chef_id);
        if(usuario == null){
            progressBar.setVisibility(View.VISIBLE);
            DataBase.getDataBase().db.collection(References.USERS_REFERENCE).document(chef_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    usuario = documentSnapshot.toObject(Usuario.class);
                    DataBase.getDataBase().addUserToMemory(usuario);
                    inflateUser();
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserView.this, "Fallo al cargar el usuario", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else
            inflateUser();

    }

    private void inflateUser() {
        imageView = findViewById(R.id.chef_photo_user_view);
        imageProgressBar = findViewById(R.id.chef_image_progress_bar);
        chefName = findViewById(R.id.chef_name_user_view);
        chefLevel = findViewById(R.id.chef_level_user_view);


        chefName.setText(usuario.getName() + "");
        chefLevel.setText(usuario.getLevel() + "");

        if(usuario.getImageReference() != null){
            if(usuario.getImage() == null){

                imageProgressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);

                StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("ProfileImages/"+ usuario.getImageReference());

                final long ONE_MEGABYTE = 1024 * 1024 * 2;
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        usuario.setImage(bytes);
                        Log.i("MT", "CREANDO");
                        imageView.setImageBitmap(Util.fixSize(bytes));
                        imageProgressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        imageView.setVisibility(View.GONE);
                        imageProgressBar.setVisibility(View.GONE);
                    }
                });
            }
            else{
                imageView.setImageBitmap(Util.fixSize(usuario.getImage()));
                imageProgressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

        }
        else{
            usuario.setImage(DataBase.getDataBase().f);
            imageView.setImageBitmap(Util.fixSize(usuario.getImage()));
            imageProgressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserView.this, ImageViewCompleteFragment.class);
                i.putExtra(ImageViewCompleteFragment.KEY_IMAGE, 3);
                i.putExtra(ImageViewCompleteFragment.KEY_USER, usuario.id);
                startActivity(i);
            }
        });


        container = findViewById(R.id.chef_recipe_container);
        recetas = DataBase.getDataBase().userTree.get(usuario.id);

        if(recetas != null){
            Log.i("INGREDIENTE", recetas.size() + "");

            Collections.sort(recetas, new Comparator<Receta>() {
                @Override
                public int compare(Receta receta, Receta t1) {
                    return receta.getName().compareTo(t1.getName());
                }
            });

            for(int i = 0; i < recetas.size(); i++){
                Receta r = recetas.get(i);
                View recipe_view = getLayoutInflater().inflate(R.layout.single_recipe_list, null);

                Button textView = recipe_view.findViewById(R.id.my_recipe_name_list);

                textView.setText(r.getName());

                textView.setOnClickListener(new onRecipeListClick(r));

                container.addView(recipe_view);
            }
        }



    }


    public class onRecipeListClick implements View.OnClickListener{
        private Receta receta;
        public onRecipeListClick(Receta receta){
            this.receta = receta;
        }
        @Override
        public void onClick(View view) {
            if(DataBase.getDataBase().getCurrentRecipe() == null) {
                finish();
                DataBase.getDataBase().setCurrentRecipe(receta);
                Intent intent = new Intent(view.getContext(), RecipeView.class);
                intent.putExtra(RecipeView.TAG_STATUS, "USERVIEW");
                startActivity(intent);
            }
        }
    }

}
