package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminArea extends AppCompatActivity {
     private EditText adminID, adminPass1, adminPass2, OldPass, getAllField;
    private Button changeAdminInfo_btn, back_btn, back2_btn, userList_btn, getAllBtn;
    private DatabaseReference ref;
    private String dbpass=new String(), new_AdminID=new String(), new_Pass= new String();
     private TextView tvGoto, tvT;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_area);
        adminID = findViewById(R.id.edit_AdminId);
        adminPass1 = findViewById(R.id.edit_adminpass);
        OldPass = findViewById(R.id.edit_oldpass);
        changeAdminInfo_btn = findViewById(R.id.btnchangeadmininfo);
        back_btn = findViewById(R.id.back);
        back2_btn = findViewById(R.id.back2);
        adminPass2 = findViewById(R.id.admin_pass);
        userList_btn = findViewById(R.id.user_list_btn);
        tvGoto = findViewById(R.id.go_to_btn);
        tvT = findViewById(R.id.tv_text);
        getAllField = findViewById(R.id.get_all_field);
        getAllBtn = findViewById(R.id.get_all_btn);

        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dbpass = dataSnapshot.child("admin").child("password").getValue(String.class).toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
            }
        });


        tvGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvT.setVisibility(View.VISIBLE);
                adminPass2.setVisibility(View.VISIBLE);
                back2_btn.setVisibility(View.VISIBLE);
                userList_btn.setVisibility(View.VISIBLE);
            }
        });

        getAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getAllField.getText().toString();
                if (dbpass.equals(str)){
                    getAllField.setText("");
                    findViewById(R.id.lay1).setVisibility(View.VISIBLE);
                    findViewById(R.id.lay2).setVisibility(View.VISIBLE);
                    findViewById(R.id.lay3).setVisibility(View.VISIBLE);
                    findViewById(R.id.lay4).setVisibility(View.VISIBLE);
                    findViewById(R.id.lay5).setVisibility(View.VISIBLE);
                    findViewById(R.id.lay6).setVisibility(View.VISIBLE);
                   // Toast.makeText(getApplicationContext(), dbpass, Toast.LENGTH_SHORT).show();
              }
                else{
                    Toast.makeText(getApplicationContext(), "No Internet / Sorry wrong Password", Toast.LENGTH_SHORT).show();
                }
            }
        });



        changeAdminInfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_AdminID = adminID.getText().toString();
                new_Pass = adminPass1.getText().toString();
                if (new_AdminID.equals("") || new_Pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "fill all the field", Toast.LENGTH_SHORT).show();
                } else if (dbpass.equals(OldPass.getText().toString())) {
                    ref.child("admin").child("email").setValue(new_AdminID);
                    ref.child("admin").child("password").setValue(new_Pass);
                    Intent intent = new Intent(AdminArea.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Successfully Changed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet / Sorry Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminArea.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        back2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        userList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = adminPass2.getText().toString().trim();
                if (dbpass.equals(temp)) {
                    Intent intent = new Intent(AdminArea.this, UsersList.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AdminArea.this, "No Internet/worng pass", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.admin_help) {
            Intent intent = new Intent(AdminArea.this, Help.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.admin_menu_logout) {
            Intent intent = new Intent(AdminArea.this, MainActivity.class);
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