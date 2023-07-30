package com.example.hbg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
    TextInputEditText txtRegistroUser, txtRegistroCorreo, txtRegistroPass;
    Button btnRegistroEntrar, btnRegistroAtras;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);

        txtRegistroUser = findViewById(R.id.txtRegistroUser);
        txtRegistroCorreo = findViewById(R.id.txtRegistroCorreo);
        txtRegistroPass = findViewById(R.id.txtRegistroPass);
        btnRegistroEntrar = findViewById(R.id.btnRegistroEntrar);
        btnRegistroAtras = findViewById(R.id.btnRegistroAtras);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        btnRegistroEntrar.setOnClickListener(view -> {
            String usuario = txtRegistroUser.getText().toString();
            String correo = txtRegistroCorreo.getText().toString();
            String pass = txtRegistroPass.getText().toString();
            createUser(usuario, correo, pass);
        });
        btnRegistroAtras.setOnClickListener(view -> {
            startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
        });
    }

    private void createUser(String usuario, String correo, String pass){
        String imgUser = "https://firebasestorage.googleapis.com/v0/b/happyblindglish-e196c.appspot.com/o/images%2FDEFAULT-DONT-DELETE.png?alt=media&token=7d90688c-561b-4490-9d62-e3050ff942b9";

        if(TextUtils.isEmpty(usuario)){
            txtRegistroUser.setError("Se necesita un nombre de usuario");
            txtRegistroUser.requestFocus();
        } else if (TextUtils.isEmpty(correo)) {
            txtRegistroCorreo.setError("Es necesario que escriba un correo");
            txtRegistroCorreo.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            txtRegistroPass.setError("Es necesario que escriba su contraseña");
            txtRegistroPass.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String id = mAuth.getCurrentUser().getUid();
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", id);
                        map.put("usuario", usuario);
                        map.put("email", correo);
                        map.put("password", pass);
                        map.put("imgUsuario", imgUser);

                        mFirestore.collection("users").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Snackbar.make(RegistroActivity.this.getCurrentFocus(), "Registro exitoso", Snackbar.LENGTH_LONG).show();
                                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(RegistroActivity.this.getCurrentFocus(), "Error al guardar usuario", Snackbar.LENGTH_LONG).show();
                            }
                        });

                    }else{
                        Snackbar.make(RegistroActivity.this.getCurrentFocus(), "Error al registrar usuario", Snackbar.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

}
