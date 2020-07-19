package com.example.fdsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Preference extends AppCompatActivity {
    Button select_temp,setT;
    Button select_h,setH;
    TextView c_temp, c_dist;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Select");
    MainTask mainTask = new MainTask();
    public  String x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        select_temp = findViewById(R.id.unit);
        select_h = findViewById(R.id.unit2);
        c_temp = findViewById(R.id.textView1);
        c_dist = findViewById(R.id.textView2);

        myRef = FirebaseDatabase.getInstance().getReference().child("Select");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                x = dataSnapshot.child("unitTemp").getValue(String.class);
                y = dataSnapshot.child("unitHeight").getValue(String.class);
                c_temp.setText(x);
                c_dist.setText(y);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        select_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Preference.this, select_temp);
                popupMenu.getMenuInflater().inflate(R.menu.menu_2,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(Preference.this, menuItem.getTitle()+" unit setted", Toast.LENGTH_SHORT).show();
                        select_temp.setText(menuItem.getTitle());
                        x = menuItem.getTitle().toString();
                        myRef.child("unitTemp").setValue(x);

                        return  true;
                    }
                });
                popupMenu.show();
            }
        });
        select_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Preference.this, select_h);
                popupMenu.getMenuInflater().inflate(R.menu.menu_3,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(Preference.this, menuItem.getTitle()+" unit setted", Toast.LENGTH_SHORT).show();
                        select_h.setText(menuItem.getTitle());
                        y = menuItem.getTitle().toString();
                        myRef.child("unitHeight").setValue(y);
                        return  true;
                    }
                });
                popupMenu.show();
            }
        });

    }
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }
}
