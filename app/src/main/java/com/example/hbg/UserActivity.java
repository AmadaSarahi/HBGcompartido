package com.example.hbg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {
    Button btnLogOut, btnRegresar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnRegresar = findViewById(R.id.btnRegresar);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(UserActivity.this, LoginActivity.class));
        });
        btnRegresar.setOnClickListener(view -> {
            startActivity(new Intent(UserActivity.this, MainActivity.class));
        });
    }

}