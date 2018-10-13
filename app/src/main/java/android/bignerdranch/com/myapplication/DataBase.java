package android.bignerdranch.com.myapplication;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Printer;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class DataBase {

    private Receta receta;
    private ArrayList<Ingrediente> listIngredients;
    private static DataBase dataBase;


    public static FirebaseFirestore db= FirebaseFirestore.getInstance();


    public static DataBase getDataBase(){
        if(dataBase == null){
            dataBase = new DataBase();
        }
        return dataBase;
    }
    private DataBase() {
        getIngredientesDB();
    }

    public static void Buscar (Usuario user){
        CollectionReference userRef = db.collection(References.USERS_REFERENCE);
        Query query = userRef.whereEqualTo("name", user.getName());
    }

    public void addRecipe(Receta receta){
        db.collection(References.RECETAS_REFERENCE).document(receta.getId()).set(receta);
    }

    public Receta getRecipe (String id){
        DocumentReference docRer= db.collection(References.RECETAS_REFERENCE).document(id);
        docRer.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               receta = documentSnapshot.toObject(Receta.class);
                Log.d("chefid", receta.getChefId());
                Log.d("descripcion", receta.getDescription());
                Log.d("id", receta.getId());
                Log.d("name", receta.getName());
                ArrayList<Paso> pasos = receta.getPasos();
            }
        });


        return new Receta(receta);
    }


    public void addIngrediente(Ingrediente ingrediente){
        db.collection(References.INGREDIENTE_REFERENCE).document(ingrediente.getId()).set(ingrediente);
    }

    private void getIngredientesDB(){
        CollectionReference collectionReference = db.collection(References.INGREDIENTE_REFERENCE);
        Task<QuerySnapshot> query = collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isComplete()){

                    listIngredients = new ArrayList<>((task.getResult().size()));
                    Ingrediente ingrediente;
                    for(DocumentSnapshot doc : task.getResult()){
                        ingrediente = doc.toObject(Ingrediente.class);
                        //Log.i("INGREDIENTE", doc.getString("name") + "    " + i.getId());
                        listIngredients.add(ingrediente);
                    }
                    Log.i("INGREDIENTE","COMPLETE :)");

                }
                else{
                    Log.i("INGREDIENTE","Error getting documents: ");

                }
            }
        });
    }


    public ArrayList<Ingrediente> getListIngredients() {
        if(this.listIngredients == null)
            return new ArrayList<>();
        return new ArrayList<>(this.listIngredients);
    }


    public boolean isNull(){
        return listIngredients == null;
    }
}
