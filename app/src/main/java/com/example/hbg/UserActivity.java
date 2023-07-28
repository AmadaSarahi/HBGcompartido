package com.example.hbg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

public class UserActivity extends AppCompatActivity {
    Button btnLogOut, btnRegresar;
    TextView txtVUserName;
    ImageView imgViewUser;

    StorageReference storageReference;
    String storage_path = "user/*";
    String id;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        txtVUserName = findViewById(R.id.txtVUserName);
        imgViewUser = findViewById(R.id.imgViewUser);

        btnLogOut = findViewById(R.id.btnLogOut);
        btnRegresar = findViewById(R.id.btnRegresar);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        id = mAuth.getCurrentUser().getUid();

        //Recupera la información de Firebase
        DocumentReference documentReference = mFirebaseFirestore.collection("users").document(id);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot == null || mAuth.getCurrentUser() == null) return;
                txtVUserName.setText(documentSnapshot.getString("usuario"));
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password","");
                editor.commit();

                mAuth.signOut();
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnRegresar.setOnClickListener(view -> {
            startActivity(new Intent(UserActivity.this, MainActivity.class));
        });
    }

}