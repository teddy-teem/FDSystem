package com.example.fdsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminArea extends AppCompatActivity {
    private EditText adminID,adminPass,OldPass;
  private   Button changeAdminInfo,back;
   private DatabaseReference ref,myref;
   private String dbpass,new_AdminID, new_Pass;
   private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_area);
        adminID = findViewById(R.id.edit_AdminId);
        adminPass = findViewById(R.id.edit_adminpass);
        OldPass =findViewById(R.id.edit_oldpass);
        changeAdminInfo=findViewById(R.id.btnchangeadmininfo);
        back = findViewById(R.id.back);


        ref= FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dbpass = dataSnapshot.child("pass").getValue(String.class).toString();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
        myref= FirebaseDatabase.getInstance().getReference("Users");

        changeAdminInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_AdminID = adminID.getText().toString();
                new_Pass = adminPass.getText().toString();
                if(new_AdminID.equals("") || new_Pass.equals("")){
                    Toast.makeText(getApplicationContext(), "fill all the field", Toast.LENGTH_SHORT).show();
                }
                else if (dbpass.equals(OldPass.getText().toString())) {
                    myref.child("admin").setValue(new_AdminID);
                    myref.child("pass").setValue(new_Pass);
                    Toast.makeText(getApplicationContext(), "Successfully Changed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminArea.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
