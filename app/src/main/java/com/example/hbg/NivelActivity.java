package com.example.hbg;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class NivelActivity extends AppCompatActivity{

    Button btnPrincipiante, btnIntermedio, btnExperto, btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel);

        btnPrincipiante = findViewById(R.id.btnPrincipiante);
        btnIntermedio = findViewById(R.id.btnIntermedio);
        btnExperto = findViewById(R.id.btnExperto);
        btnRegresar = findViewById(R.id.btnRegresar);



        btnRegresar.setOnClickListener(view -> {
            startActivity(new Intent(NivelActivity.this, MainActivity.class));
        });
    }


}
