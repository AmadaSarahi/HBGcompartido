package com.example.hbg;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

public class ConfiguracionActivity extends AppCompatActivity {

    Button btnCambContra, btnRegresar;
    SeekBar SBVolumen;
    Switch SNotificaciones;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        SBVolumen = findViewById(R.id.SBVolumen);
        SNotificaciones = findViewById(R.id.SNotificaciones);
        btnCambContra = findViewById(R.id.btnCambContra);
        btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(view -> {
            startActivity(new Intent(ConfiguracionActivity.this, MainActivity.class));
        });
    }


}
