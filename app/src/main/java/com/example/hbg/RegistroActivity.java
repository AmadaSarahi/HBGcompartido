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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroActivity extends AppCompatActivity {
    TextInputEditText txtRegistroUser, txtRegistroCorreo, txtRegistroPass;
    Button btnRegistroEntrar, btnRegistroAtras;
    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_activity);
        txtRegistroUser = findViewById(R.id.txtRegistroUser);
        txtRegistroCorreo = findViewById(R.id.txtRegistroCorreo);
        txtRegistroPass = findViewById(R.id.txtRegistroPass);
        btnRegistroEntrar = findViewById(R.id.btnRegistroEntrar);
        btnRegistroAtras = findViewById(R.id.btnRegistroAtras);

        mAuth = FirebaseAuth.getInstance();

        btnRegistroEntrar.setOnClickListener(view -> {
            createUser();
        });
        btnRegistroAtras.setOnClickListener(view -> {
            startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
        });
    }

    private void createUser(){
        String usuario = txtRegistroUser.getText().toString();
        String correo = txtRegistroCorreo.getText().toString();
        String pass = txtRegistroPass.getText().toString();

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
                        Snackbar.make(RegistroActivity.this.getCurrentFocus(), "Registro exitoso", Snackbar.LENGTH_LONG).show();

                        //Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                    }else{
                        Snackbar.make(RegistroActivity.this.getCurrentFocus(), "Error al registrar usuario", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(RegistroActivity.this, "Error al registrar usuario: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

}
