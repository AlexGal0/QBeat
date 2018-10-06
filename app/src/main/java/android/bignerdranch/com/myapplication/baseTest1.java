package android.bignerdranch.com.myapplication;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class baseTest1 {

    public static FirebaseFirestore db= FirebaseFirestore.getInstance();


    public baseTest1() {
    }

    public static void Buscar (Usuario user){
        CollectionReference userRef = db.collection(References.USERS_REFERENCE);
        Query query = userRef.whereEqualTo("name", user.getName());
    }
    public void addUser(Usuario user) {

        Map<String, Object> userTo = new HashMap<>();
        userTo.put("name", user.getName());
        userTo.put("level", user.getLevel());
        userTo.put("currentExperience", user.getCurrentExperience());
        userTo.put("misRecetas", user.getMisRecetas());
        userTo.put("misRecetasGuardadas",user.getMisRecetasGuardadas());

// Add a new document with a generated ID
        db.collection("users").document(user.getName())
                .set(userTo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void addRecipe(Receta receta) {

        Map<String, Object> recipeTo = new HashMap<>();
        recipeTo.put("name", receta.getName());
        recipeTo.put("pasos", receta.getPasos());
        recipeTo.put("descripcion", receta.getDescription());

// Add a new document with a generated ID
        db.collection(References.RECETAS_REFERENCE).document(receta.getName())
                .set(recipeTo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void addIngrediente(Ingrediente ingrediente) {

        Map<String, Object> ingredientTo = new HashMap<>();
        ingredientTo.put("name", ingrediente.getName());


// Add a new document with a generated ID
        db.collection(References.INGREDIENTE_REFERENCE).document(ingrediente.getName())
                .set(ingredientTo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
