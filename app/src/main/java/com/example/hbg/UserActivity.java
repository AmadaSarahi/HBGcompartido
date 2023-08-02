package com.example.hbg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.*;
import android.content.*;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserActivity extends AppCompatActivity {
    private ProgressDialog mDialog;
    Button btnLogOut, btnRegresar, btnSelImg, btnSave;
    TextView txtVUserName;
    ImageView imgViewUser;

    String id;
    Uri imgUri;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirebaseFirestore;
    FirebaseStorage mFirebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mDialog = new ProgressDialog(this);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnRegresar = findViewById(R.id.btnRegresar);
        btnSelImg = findViewById(R.id.btnSelImg);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setEnabled(false);
        txtVUserName = findViewById(R.id.txtVEmail);
        imgViewUser = findViewById(R.id.imgViewUser);

        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        btnSelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                btnSave.setEnabled(true);
            }
        });
        if(btnSave.isEnabled() == false && btnSave.isHovered() == true && btnSave.isFocused() == true){
            Toast.makeText(UserActivity.this, "Primero selecciona una imagen", Toast.LENGTH_SHORT).show();
        }else{
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizarImg();
                    btnSave.setEnabled(false);
                }
            });
        }

        //Recupera la información de Firebase
        DocumentReference documentReference = mFirebaseFirestore.collection("users").document(id);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot == null || mAuth.getCurrentUser() == null) return;
                String imgUrl = documentSnapshot.getString("imgUsuario");

                Picasso.get().load(imgUrl).into(imgViewUser);

                txtVUserName.setText(documentSnapshot.getString("usuario"));
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("Cerrar sesión");
                builder.setMessage("¿Seguro que quieres cerrar sesión?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(UserActivity.this, "¡Hasta luego!", Toast.LENGTH_LONG).show();
                                        cerrarSesion();
                                    }
                                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(UserActivity.this, "Cancelando...", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
        btnRegresar.setOnClickListener(view -> {
            startActivity(new Intent(UserActivity.this, MainActivity.class));
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            imgUri = data.getData();
            obtenerImagen();
        }
    }
    private void obtenerImagen() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imgViewUser.setImageBitmap(bitmap);
    }

    private void actualizarImg(){
        //Muestra el progreso de guardado
        mDialog.setMessage("Actualizando...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        //almacena la imagen en firebase storage
        mFirebaseStorage.getReference("images/"+ UUID.randomUUID().toString()).putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            subirImg(task.getResult().toString());
                        }
                    });
                    Toast.makeText(UserActivity.this, "Imagen actualizada", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(UserActivity.this, "Algo salió mal", Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
            }
        });
    }
    private void subirImg(String url){
        Map<String, Object> map = new HashMap<>();
        map.put("imgUsuario", url);
        mFirebaseFirestore.collection("users").document(id).set(map, SetOptions.merge());
    }
    private void cerrarSesion(){
        SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("password","");
        editor.commit();

        mAuth.signOut();
        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}