package android.bignerdranch.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth .AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        final EditText username = findViewById(R.id.username_login);
        final ProgressBar progressBar = findViewById(R.id.progressBar_waiting);
        progressBar.setVisibility(View.INVISIBLE);


        final EditText password = findViewById(R.id.password_login);
        Button signInButton = findViewById(R.id.login_button);
        TextView registrar = findViewById(R.id.registrar_login);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = username.getText().toString().trim();
                final String pass = password.getText().toString();

                Log.i("REGISTER", email);

                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    finish();
                                    return;
                                }
                                Toast.makeText(Login.this, "Credenciales incorrectas",
                                        Toast.LENGTH_SHORT).show();
                            }});
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registrar.class);
                Login.this.startActivity(intent);
            }
        });
    }
}
