package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    EditText userName, userPassword, userEmail, adminPass,userConfirmPassword, userMob;
    Button back, reg;
    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;
    String adpass;
    TextView tv;
    ProgressDialog progressDialog;
    FirebaseDatabase rootNode;
    DatabaseReference ref;
    String uMobNum, uName, uEmail, uPass;
    DatabaseReference fRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = (EditText) findViewById(R.id.editname);
        userPassword= (EditText) findViewById(R.id.editpass);
        userConfirmPassword= (EditText) findViewById(R.id.editconfirmpass);
        userEmail = (EditText) findViewById(R.id.editemail);
        adminPass = (EditText) findViewById(R.id.editadminpass);
        userMob = findViewById(R.id.editmob);
        back = (Button) findViewById(R.id.back);
        reg = (Button) findViewById(R.id.register);
       // tv = findViewById(R.id.debug);

        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        fRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                adpass = dataSnapshot.child("admin").child("password").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        firebaseAuth  =  FirebaseAuth.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(Register.this);
                progressDialog.setMessage("Wait while");
                progressDialog.show();
                String ADMIN = adminPass.getText().toString();
                if(validate() && ADMIN.equals(adpass)) {
                    ///Upload Data to database..
                    uEmail = userEmail.getText().toString().trim();
                    uPass = userConfirmPassword.getText().toString().trim();
                    uMobNum = userMob.getText().toString().trim();
                    uName = userName.getText().toString().trim();
                    if(uPass.length()<6){
                        Toast.makeText(getApplicationContext(), "Failed Attempt/password have to be 6+ character", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        firebaseAuth.createUserWithEmailAndPassword(uEmail, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    UsersData usersData = new UsersData(uEmail,uName, uPass, uMobNum, "0");

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(usersData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Succesfully Created", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    progressDialog.dismiss();
                                    startActivity(new Intent(Register.this, MainActivity.class));
                                    finish();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failed Attempt/u have to use unique Email id", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    }
                else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Admin Pass wrong", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
    private Boolean validate(){
        Boolean res = false;

        String i = userEmail.getText().toString();
        String p = userPassword.getText().toString();
        String x = userPassword.getText().toString().trim();
        String y = userConfirmPassword.getText().toString().trim();

        if(i.isEmpty() || p.isEmpty()){
            Toast.makeText(this,"Please Enter all the field",Toast.LENGTH_SHORT).show();
        }
        else if (!x.equals(y)){
            Toast.makeText(this,"Passord and Confirm Passord didn't match",Toast.LENGTH_SHORT).show();
        }
        else{
            res = true;

        }
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.admin_help){
            Intent intent = new Intent(Register.this,Help.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.admin_menu_logout){
            Intent intent = new Intent(Register.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
