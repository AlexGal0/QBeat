package android.bignerdranch.com.myapplication;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Printer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
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

    public void addRecipe2(Receta receta){
        db.collection(References.RECETAS_REFERENCE).document(receta.getId()).set(receta);
    }

    public void getRecipe (String id){
        DocumentReference docRer= db.collection(References.RECETAS_REFERENCE).document(id);
        docRer.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               Receta rec = documentSnapshot.toObject(Receta.class);
                Log.d("chefid", rec.getChefId());
                Log.d("descripcion", rec.getDescription());
                Log.d("id", rec.getId());
                Log.d("name", rec.getName());
                ArrayList<Paso> pasos = rec.getPasos();
                Log.d("Paso 1", pasos.get(0).getTitle()+"");
                Log.d("Paso 2", pasos.get(1).getTitle());
            }
        });
    }

}
