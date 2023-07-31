package com.example.hbg;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RetosJuegoActivity extends AppCompatActivity{

    Button  btnRegresar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retosjuego);


        btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(view -> {
            startActivity(new Intent(RetosJuegoActivity.this, RetosActivity.class));
        });
    }

}
