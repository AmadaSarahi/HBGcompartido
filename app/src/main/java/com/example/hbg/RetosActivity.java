package com.example.hbg;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class RetosActivity extends AppCompatActivity{

    Button btnBasico, btnIntermedio, btnAvanzado, btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retos);

        btnBasico = findViewById(R.id.btnBasico);
        btnIntermedio = findViewById(R.id.btnIntermedio);
        btnAvanzado = findViewById(R.id.btnAvanzado);
        btnRegresar = findViewById(R.id.btnRegresar);



        btnRegresar.setOnClickListener(view -> {
            startActivity(new Intent(RetosActivity.this, MainActivity.class));
        });
    }

}
