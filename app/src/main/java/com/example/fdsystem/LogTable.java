package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogTable extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference ref, myRef;
    FirebaseDatabase database;
    ArrayList<LogData> list=new ArrayList<>();;
    int isSort=0;
    LogData logData;
    String dbunitH, amI;
    //SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_table);
        init();
        amI = getIntent().getExtras().getString("amIadmin","0");
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        if (amI.equals("1")){
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    dbunitH = dataSnapshot.child("admin").child("unitH").getValue(String.class).toString();
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dbunitH = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("unitH").getValue(String.class).toString();
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public  void init(){
        recyclerView = findViewById(R.id.myrecyclerview);
        // sr = (SearchView) findViewById(R.id.srch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Readings");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.datalog_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.actionsearch);
        SearchView sr = (SearchView) menuItem.getActionView();
        sr.setQueryHint("Search");
        final LogAdapterClass adapter;
        sr.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<LogData> mylist = new ArrayList<>();
                for (LogData ld: list){
                    if (ld.getDeviceID().toLowerCase().contains(s.toLowerCase())){
                        mylist.add(ld);
                    }
                }
                LogAdapterClass lAdapter = new LogAdapterClass(mylist);
                recyclerView.setAdapter(lAdapter);
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.datalog_menu_help){
            Intent intent = new Intent(LogTable.this, Help.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.datalog_menu_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(LogTable.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        LogData D = new LogData();
                        list.clear();
                        for (DataSnapshot ds :dataSnapshot.getChildren()){
                            D=ds.getValue(LogData.class);
                            if (dbunitH.equals("Inch")){
                                Double x = D.Level;
                                x=x/2.54;
                                x = Math.round(x*100.0)/100.0;
                                D.Level=x;
                                D.Unit_h ="I";
                                list.add(D);
                            }
                            else if (dbunitH.equals("Foot")){
                                Double x = D.Level;
                                x=x/30.48;
                                x = Math.round(x*100.0)/100.0;
                                D.Level=x;
                                D.Unit_h="F";
                                list.add(D);
                            }
                            else if (dbunitH.equals("M")){
                                Double x = D.Level;
                                x=x/100.00;
                                x = Math.round(x*100.0)/100.0;
                                D.Level=x;
                                D.Unit_h="M";
                                list.add(D);
                            }
                            else{
                               D.Unit_h= "CM";
                                list.add(D);
                            }
                        }
                        LogAdapterClass logAdapterClass = new LogAdapterClass(list);
                        recyclerView.setAdapter(logAdapterClass);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LogTable.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainTask.class);
        intent.putExtra("amIadmin", amI);
        startActivity(intent);
        finish();
    }
}