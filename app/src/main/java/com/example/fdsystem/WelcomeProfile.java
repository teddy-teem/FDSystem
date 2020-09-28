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
import android.widget.ImageView;
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
    private TextView tvMob,tvEmail, tvName, tv, changePassTv, DeleteIDTv;
    private EditText CrntPass, newChangePass, oldChangePass;
    private Button mainTask, delete, changePassBtn;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    String rmEm, oldPass="", newPass="",dbpass, crntPass="", dbCrntPass;
    ImageView imageView;
    long backpretime;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_profile);
        tv = findViewById(R.id.tv_mess);
        CrntPass = findViewById(R.id.crnt_pass_edit);
        mainTask = findViewById(R.id.data_page_btn);
        delete = findViewById(R.id.delete_btn);
        tvEmail=findViewById(R.id.user_email_tv);
        tvName=findViewById(R.id.user_name_tv);
        tvMob = findViewById(R.id.user_mob_tv);
        DeleteIDTv = findViewById(R.id.delete_btn_temp);
        changePassBtn = findViewById(R.id.pass_change_btn);
        changePassTv = findViewById(R.id.change_pass_txt);
        newChangePass = findViewById(R.id.new_pass_change_edit);
        oldChangePass=findViewById(R.id.old_pass_change_edit);

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
                dbpass=dataSnapshot.child(user.getUid()).child("password").getValue(String.class).toString();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


        if (user.isEmailVerified()){
            tv.setText("You are Varified User, Welcome to the Apps");
            reference.child(user.getUid()).child("status").setValue("✓");
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

        changePassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newChangePass.setVisibility(View.VISIBLE);
                oldChangePass.setVisibility(View.VISIBLE);
                changePassBtn.setVisibility(View.VISIBLE);
            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oldPass = oldChangePass.getText().toString();
                newPass = newChangePass.getText().toString().trim();
                if (oldPass.equals(dbpass)){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), dbpass);

// Prompt the user to re-provide their sign-in credentials
                    if (!newPass.isEmpty()) {
                        progressDialog = new ProgressDialog(WelcomeProfile.this);
                        progressDialog.setMessage("Changing......");
                        progressDialog.show();
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        reference.child(user.getUid()).child("password").setValue(newPass);
                                                        Intent intent = new Intent(WelcomeProfile.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Error Occuared", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Authenticatin Failed Failed", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Fill all the fields", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rmEm= user.getUid();
                crntPass = CrntPass.getText().toString();
                if (!crntPass.isEmpty()) {
                    if (crntPass.equals(dbpass)) {
                        Intent intent = new Intent(WelcomeProfile.this, DeleteId.class);
                        intent.putExtra("deleteUID", rmEm);
                        intent.putExtra("AccPass", crntPass);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Wrogn Pass", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Empty Field", Toast.LENGTH_SHORT).show();
                }
            }
        });
        DeleteIDTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrntPass.setVisibility(View.VISIBLE);
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
        if (backpretime+2000 > System.currentTimeMillis()){
            super.onBackPressed();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
            return;
        }
        else{
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backpretime = System.currentTimeMillis();
    }
}