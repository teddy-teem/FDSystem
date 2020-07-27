package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button login,reg,admin;
    EditText username, userpass;
    DatabaseReference myRef;
    String adpass,adname;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    TextView tv;
    long backpretime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.user_txt);
        userpass = (EditText) findViewById(R.id.pass_txt);
        reg = (Button) findViewById(R.id.reg_btn);
        admin=findViewById(R.id.admin_btn);



        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                adpass = dataSnapshot.child("pass").getValue(String.class).toString();
                adname = dataSnapshot.child("admin").getValue(String.class).toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        Toast.makeText(getApplicationContext(), "Welcome to AIMS ", Toast.LENGTH_SHORT).show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Wait while");
                progressDialog.show();

                String x,y;
                x=username.getText().toString();
                y = userpass.getText().toString();

                try {
                    if (x.equals(adname) && y.equals(adpass)) {
                        progressDialog.dismiss();

                        Intent intent = new Intent(MainActivity.this, MainTask.class);
                        startActivity(intent);
                        finish();
                    }
                    validate(x,y);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Put ID and Password please! ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
                finish();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AdminArea.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void validate(String s, String t){
        firebaseAuth.signInWithEmailAndPassword(s, t)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            progressDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this,MainTask.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Something Error.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
    @Override
    public void onBackPressed() {
        if (backpretime+2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backpretime = System.currentTimeMillis();
    }
}
