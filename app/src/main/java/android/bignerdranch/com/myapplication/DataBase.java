package android.bignerdranch.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.ArraySet;
import android.util.Log;
import android.util.Printer;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class DataBase {

    private static DataBase dataBase;

    private Receta receta;

    private Set<Ingrediente> listIngredients;
    private Set<Receta> listRecipe;
    private Set<Usuario> listUsers;

    public Map<String, ArrayList<Receta>> userTree;
    public Map<String, Usuario> users;

    public Usuario currentUser;

    public MyRecipeFragment myRecipeFragment;

    public byte[] f;

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Receta currentRecipe;

    public int loadLogin;
    /*
        0  =  Ingredients
        1  =  Recipes
        2  =  Users
        3  =  CurrentUser

     */


    public static DataBase getDataBase(){
        if(dataBase == null){
            dataBase = new DataBase();
        }
        return dataBase;
    }
    private DataBase() {
        loadLogin = 0;
        listRecipe = new TreeSet<>();
        listIngredients = new TreeSet<>();
        listUsers = new TreeSet<>();
        userTree = new HashMap<>();
        users = new HashMap<>();
        Log.i("INGREDIENTE","COMPLETE :)");
    }

    public static void Buscar (Usuario user){
        CollectionReference userRef = db.collection(References.USERS_REFERENCE);
        Query query = userRef.whereEqualTo("name", user.getName());
    }

    public void addRecipe(Receta receta){
        db.collection(References.RECETAS_REFERENCE).document(receta.getId()).set(receta);
    }
    public void addUser(Usuario usuario) {
        db.collection(References.USERS_REFERENCE).document(usuario.id).set(usuario);
    }

    public Receta getRecipe (String id){
        DocumentReference docRer= db.collection(References.RECETAS_REFERENCE).document(id);
        docRer.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               receta = documentSnapshot.toObject(Receta.class);
                ArrayList<Paso> pasos = receta.getPasos();
            }
        });
        return new Receta(receta);
    }

    public void setCampo(String campo, String newValue){
        db.collection(References.USERS_REFERENCE).document(currentUser.id).update(campo, newValue);
    }


    public void addIngrediente(Ingrediente ingrediente){
        db.collection(References.INGREDIENTE_REFERENCE).document(ingrediente.getId()).set(ingrediente);
    }

    private void getIngredientesDB(final MainActivity mainActivity){
        CollectionReference collectionReference = db.collection(References.INGREDIENTE_REFERENCE);

        if((loadLogin&1) == 0) {
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("ERROR", "Listen ERROR", e);
                        return;
                    }


                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        Ingrediente ingrediente = documentChange.getDocument().toObject(Ingrediente.class);

                        switch (documentChange.getType()) {
                            case ADDED:
                                listIngredients.add(ingrediente);
                                break;
                            case MODIFIED:
                                break;
                            case REMOVED:
                                listIngredients.remove(ingrediente);
                                break;
                        }
                    }


                    if ((loadLogin & 1) == 0) {
                        loadLogin |= 1;
                        Log.i("LOAD", "COMPLETE TASK 0");
                        mainActivity.updateFrame();
                    }
                }
            });
        }
    }

    private void getRecetaDB(final MainActivity mainActivity){
        CollectionReference collectionReference = db.collection(References.RECETAS_REFERENCE);
        collectionReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.w("ERROR", "Listen ERROR", e);
                            return;
                        }
                        for(DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                            Receta receta = documentChange.getDocument().toObject(Receta.class);

                            ArrayList<Receta> userR = userTree.get(receta.chefId);
                            if(userR == null)
                                userTree.put(receta.chefId, userR = new ArrayList<Receta>());


                            switch (documentChange.getType()){
                                case ADDED:
                                    listRecipe.add(receta);
                                    userR.add(receta);
                                    break;
                                case MODIFIED:
                                    Log.i("CHANGES", "CHANGE A VALUE");
                                    listRecipe.remove(receta);
                                    listRecipe.add(receta);
                                    break;
                                case REMOVED:
                                    listRecipe.remove(receta);
                                    userR.remove(receta);
                                    break;
                            }
                        }

                        if(DataBase.getDataBase().myRecipeFragment != null && DataBase.getDataBase().myRecipeFragment.getView() != null)
                            DataBase.getDataBase().myRecipeFragment.notifyDataChange();


                        Log.i("CHANGES", "NOTIFY CHANGE IN DATABASE");

                        if ((loadLogin & (1 << 1)) == 0) {
                            loadLogin |= 1 << 1;
                            mainActivity.updateFrame();
                            Log.i("LOAD", "COMPLETE TASK 1");
                        }
                    }
                });
    }


    public ArrayList<Ingrediente> getListIngredients() {
        if(this.listIngredients == null)
            return new ArrayList<>();
        return new ArrayList<>(this.listIngredients);
    }

    public ArrayList<Receta> getListReceta(){
        if(this.listRecipe == null)
            return new ArrayList<>();
        return new ArrayList<>(this.listRecipe);
    }

    public boolean isNull(){
        return listIngredients == null;
    }

    public void setUser(final MainActivity mainActivity) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = currentUser.getEmail();
        Log.i("LOGIN", email);

        Task<QuerySnapshot> documentSnapshotTask = db.collection(References.USERS_REFERENCE).whereEqualTo("email", email).get();
        documentSnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                DataBase.this.currentUser = queryDocumentSnapshots.getDocuments().get(0).toObject(Usuario.class);
                loadLogin |= (1<<3);
                Log.i("LOAD", "COMPLETE TASK 3");
                mainActivity.updateFrame();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mainActivity.getApplicationContext(), "FALLO CON LA DESCARGA DEL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getUsersDB(final MainActivity mainActivity) {
        CollectionReference collectionReference = db.collection(References.USERS_REFERENCE);
        collectionReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.w("ERROR", "Listen ERROR", e);
                            return;
                        }
                        for(DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
                            Usuario usuario = documentChange.getDocument().toObject(Usuario.class);

                            if(userTree.get(usuario.id) == null)
                                userTree.put(usuario.id, new ArrayList<Receta>());


                            switch (documentChange.getType()){
                                case ADDED:
                                    listUsers.add(usuario);
                                    break;
                                case MODIFIED:
                                    Log.i("CHANGES", "CHANGE A VALUE IN USER");
                                    listUsers.remove(usuario);
                                    listUsers.add(usuario);
                                    break;
                                case REMOVED:
                                    break;
                            }
                        }

                        if ((loadLogin & (1 << 2)) == 0) {
                            loadLogin |= 1 << 2;
                            Log.i("LOAD", "COMPLETE TASK 2");
                            mainActivity.updateFrame();
                        }
                    }
                });
    }

    public void cleanUser() {
        currentUser = null;
        listRecipe.clear();
        userTree.clear();
        users.clear();
        listUsers.clear();
        loadLogin = 0;
    }

    public void removeRecipe(Receta receta) {
        db.collection(References.RECETAS_REFERENCE).document(receta.getId()).delete();
    }

    public void updateLogin(MainActivity mainActivity) {
        this.getIngredientesDB(mainActivity); // 0
        this.getRecetaDB(mainActivity);       // 1
        this.getUsersDB(mainActivity);        // 2
        this.setUser(mainActivity);           // 3
    }



    public void updateAllRecipes() {
        for(Receta e: listRecipe){
            db.collection(References.RECETAS_REFERENCE).document(e.getId()).update("create", new Date(System.currentTimeMillis()));
        }
    }

    public Receta getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(Receta currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    public Usuario getUser(String chefId) {
        return users.get(chefId);
    }

    public void addUserToMemory(Usuario usuario) {
        users.put(usuario.id, usuario);
    }

    public ArrayList<Usuario> getlistUsers() {
        return new ArrayList<>(listUsers);
    }
}
