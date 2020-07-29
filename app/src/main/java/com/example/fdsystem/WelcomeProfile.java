package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeProfile extends AppCompatActivity {
   private TextView tvMob,tvEmail, tvName, tv;
   private EditText pass;
   private Button mainTask, delete, comDelete;
   private FirebaseAuth auth;
   private FirebaseUser user;
   DatabaseReference reference;
   String rmEm, stEm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_profile);
        tv = findViewById(R.id.tv_mess);
        pass = findViewById(R.id.pass_edit);
        mainTask = findViewById(R.id.data_page_btn);
        delete = findViewById(R.id.delete_btn);
        tvEmail=findViewById(R.id.user_email_tv);
        tvName=findViewById(R.id.user_name_tv);
        tvMob = findViewById(R.id.user_mob_tv);
        comDelete = findViewById(R.id.delete_btn_temp);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        tvEmail.setText(user.getEmail());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                tvName.setText(dataSnapshot.child(user.getUid()).child("name").getValue(String.class).toString());
                tvMob.setText(dataSnapshot.child(user.getUid()).child("mobile").getValue(String.class).toString());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


        if (user.isEmailVerified()){
            tv.setText("You are Varified User, Welcome to the Apps");
            reference.child(user.getUid()).child("status").setValue("1");
            mainTask.setEnabled(true);
        }
        else{
            tv.setText("Not Varified User, Click On This Text To Have Verify Email");
            mainTask.setEnabled(false);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(WelcomeProfile.this, "email Varification Send", Toast.LENGTH_SHORT).show();
                                        tv.setText("Verification Link has been send, check your email");
                                    }
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(WelcomeProfile.this, "Failed to verify", Toast.LENGTH_SHORT).show();
                        }
                    });
                    ;
                }
            });
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pass.getText().toString().isEmpty()) {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass.getText().toString().trim());
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                rmEm=user.getEmail();
                                                Delete(user.getUid());
                                                auth.signOut();
                                                Intent intent = new Intent(WelcomeProfile.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                Toast.makeText(WelcomeProfile.this, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                }
            }
        });
        comDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        });
        mainTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeProfile.this, MainTask.class);
                intent.putExtra("amIadmin", "0");
                startActivity(intent);
                finish();
            }
        });
    }
    public  void Delete(String s){
        reference.child(s).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(WelcomeProfile.this, "Done", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.wc_profile_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.admin_help){
            Intent intent = new Intent(WelcomeProfile.this,Help.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.wc_menu_logout){
            Intent intent = new Intent(WelcomeProfile.this,MainActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.wc_menu_about){
            Intent intent = new Intent(WelcomeProfile.this,About.class);
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