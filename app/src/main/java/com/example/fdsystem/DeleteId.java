package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteId extends AppCompatActivity {
    ProgressDialog progressDialog;
    String id,pass;
    DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_id);

        progressDialog = new ProgressDialog(DeleteId.this);
        progressDialog.setMessage("Deleting......");
        progressDialog.show();
        id = getIntent().getExtras().getString("deleteUID","");
        pass = getIntent().getExtras().getString("AccPass","");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Delete();
                                    Toast.makeText(DeleteId.this, "Delete Done/Exiting", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(DeleteId.this, "Unsuccesful Deleting / Exiting", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                    homeIntent.addCategory( Intent.CATEGORY_HOME );
                                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(homeIntent);
                                    finish();
                                }
                            }
                        });
                    }
                });

    }
    public void Delete(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(id).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        finish();
                        Toast.makeText(DeleteId.this, "Deleted Succesfully", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}