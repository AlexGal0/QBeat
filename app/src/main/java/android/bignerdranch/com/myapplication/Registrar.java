package android.bignerdranch.com.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registrar extends FragmentActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registrar_layout);

        final EditText email = findViewById(R.id.email_register);
        final EditText name = findViewById(R.id.name_register);
        final EditText password = findViewById(R.id.password_register);
        final EditText cpassword = findViewById(R.id.cpassword_register);

        Button create = findViewById(R.id.create_register);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!password.getText().toString().equals(cpassword.getText().toString())){
                    Toast.makeText(Registrar.this, "Las contraseñas no coinciden",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.getText().length() < 6){
                    Toast.makeText(Registrar.this, "La contraseña es muy corta",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                final Usuario usuario = new Usuario(email.getText().toString(), name.getText().toString());

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(usuario.getEmail(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Registrar.this, "Fallo al crear usuario",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        DataBase.getDataBase().addUser(usuario);
                        Toast.makeText(Registrar.this, "Usuario creado correctamente",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registrar.this, "Email en uso / Correo invalido",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
