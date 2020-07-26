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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogTable extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference ref;
    FirebaseDatabase database;
    ArrayList<LogData> list;
    int isSort=0;
    LogData logData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_table);

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
        if (item.getItemId() == R.id.sort){
            isSort=1;
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
                        list = new ArrayList<>();
                        for (DataSnapshot ds :dataSnapshot.getChildren()){
                            list.add(ds.getValue(LogData.class));
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
        startActivity(intent);
        finish();
    }
}