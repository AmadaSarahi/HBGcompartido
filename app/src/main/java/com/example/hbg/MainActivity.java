package com.example.hbg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut, btnUser, btnNiveles, btnRetos;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnUser = findViewById(R.id.btnUser);
        mAuth = FirebaseAuth.getInstance();
        btnNiveles = findViewById(R.id.btnNiveles);
        btnRetos = findViewById(R.id.btnRetos);

        btnLogOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
        btnUser.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        });
        btnNiveles.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, NivelActivity.class));
        });
        btnRetos.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RetosActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

}