package com.example.hbg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnLogOut, btnUser, btnNiveles, btnRetos, btnSettings;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnUser = findViewById(R.id.btnUser);
        mAuth = FirebaseAuth.getInstance();
        btnSettings = findViewById(R.id.btnSettings);
        btnNiveles = findViewById(R.id.btnNiveles);
        btnRetos = findViewById(R.id.btnRetos);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });

        btnUser.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, UserActivity.class));
        });

        btnSettings.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ConfiguracionActivity.class));
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
    private void cerrarSesion(){
        SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("password","");
        editor.commit();

        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}