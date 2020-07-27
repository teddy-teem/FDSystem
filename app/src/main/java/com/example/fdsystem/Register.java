package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText userName, userPassword, userEmail, adminPass,userConfirmPassword;
    Button back, reg;
    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;
    String adpass;
    int cnt=0;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = (EditText) findViewById(R.id.editname);
        userPassword= (EditText) findViewById(R.id.editpass);
        userConfirmPassword= (EditText) findViewById(R.id.editconfirmpass);
        userEmail = (EditText) findViewById(R.id.editemail);
        adminPass = (EditText) findViewById(R.id.editadminpass);
        back = (Button) findViewById(R.id.back);
        reg = (Button) findViewById(R.id.register);

        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                adpass = dataSnapshot.child("pass").getValue(String.class).toString();
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
                if(validate() && ADMIN.equals(adpass)){
                    ///Upload Data to database..
                    final String user_email = userEmail.getText().toString().trim();
                    final String password  = userConfirmPassword.getText().toString().trim();
                    final String c_pass = userPassword.getText().toString().trim();
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    // final DatabaseReference Ref = database.getReference().child("Users");

                    firebaseAuth.createUserWithEmailAndPassword(user_email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() && cnt<5) {
                                Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                                /*Ref.child(user_email).setValue(user_email);
                                Ref.child(user_email).child("id").setValue(user_email);
                                Ref.child(user_email).child("password").setValue(c_pass);
                                Ref.child(user_email).child("name").setValue(userName.getText().toString());*/
                                cnt++;
                                progressDialog.dismiss();
                                startActivity(new Intent(Register.this,MainActivity.class));
                                finish();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Failed Attempt",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Admin Pass wrong",Toast.LENGTH_SHORT).show();
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
}
