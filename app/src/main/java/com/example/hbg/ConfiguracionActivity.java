package com.example.hbg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class ConfiguracionActivity extends AppCompatActivity {
    private String email = "";
    private ProgressDialog mDialog;
    TextInputEditText txtEmail;
    Button btnCambContra, btnRegresar;
    SeekBar SBVolumen;
    Switch SNotificaciones;
    String id;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirebaseFirestore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        mDialog = new ProgressDialog(this);

        txtEmail = findViewById(R.id.txtEmail);
        SBVolumen = findViewById(R.id.SBVolumen);
        SNotificaciones = findViewById(R.id.SNotificaciones);
        btnCambContra = findViewById(R.id.btnCambContra);
        btnRegresar = findViewById(R.id.btnRegresar);

        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        //Recupera la información de Firebase
        DocumentReference documentReference = mFirebaseFirestore.collection("users").document(id);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot == null || mAuth.getCurrentUser() == null) return;
                txtEmail.setText(documentSnapshot.getString("email"));

            }
        });
        btnRegresar.setOnClickListener(view -> {
            startActivity(new Intent(ConfiguracionActivity.this, MainActivity.class));
        });
        btnCambContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracionActivity.this);
                builder.setTitle("Cambiar contraseña");
                builder.setMessage("¿Seguro que quieres cambiar la contraseña? " +
                                "Resivirá un correo para continuar con el proceso")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                email = txtEmail.getText().toString();
                                mDialog.setMessage("Por favor espere...");
                                mDialog.setCanceledOnTouchOutside(false);
                                mDialog.show();
                                reestablecerContraseña();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ConfiguracionActivity.this, "Cancelando...", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }
    private void reestablecerContraseña() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ConfiguracionActivity.this, "Se ha enviado un correo para reestablecer la contraseña", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ConfiguracionActivity.this, "No se pudo reestablecer la contraseña", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
            }
        });
    }
}
