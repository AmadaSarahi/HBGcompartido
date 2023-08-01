package com.example.hbg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity{
    private boolean backPressedOnce = false;
    TextInputEditText txtLoginCorreo, txtLoginPass;
    Button btnEntrar, btnRegistro;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        txtLoginCorreo = findViewById(R.id.txtLoginCorreo);
        txtLoginPass = findViewById(R.id.txtLoginPass);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnRegistro = findViewById(R.id.btnRegistro);

        mAuth = FirebaseAuth.getInstance();

        btnEntrar.setOnClickListener(view -> {
            loginUser();
        });
        btnRegistro.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
        });
    }

    private void loginUser(){
        String correo = txtLoginCorreo.getText().toString();
        String pass = txtLoginPass.getText().toString();

       if (TextUtils.isEmpty(correo)) {
            txtLoginCorreo.setError("Es necesario que escriba un correo");
            txtLoginCorreo.requestFocus();
       } else if (TextUtils.isEmpty(pass)) {
            txtLoginPass.setError("Es necesario que escriba su contraseña");
            txtLoginPass.requestFocus();
       }else{
           mAuth.signInWithEmailAndPassword(correo, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @SuppressLint("ResourceAsColor")
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                           Toast.makeText(LoginActivity.this,"Bienvenido", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                       }else{
                            Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                   }
               }
           });
       }
    }
    //salir después de presionar dos veces
    @Override
    public void onBackPressed(){
            if(backPressedOnce){
            super.onBackPressed();
            return;
        }
        Snackbar.make(LoginActivity.this.getCurrentFocus(), "Presiona otra vez para salir", Snackbar.LENGTH_SHORT).show();
        backPressedOnce = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 2000);
    }


}
