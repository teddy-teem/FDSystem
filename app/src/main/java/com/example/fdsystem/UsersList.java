package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersList extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference ref, myRef;
    FirebaseDatabase database;
    ArrayList<uData> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        init();
    }
    public  void init(){
        recyclerView = findViewById(R.id.user_recyclerview);
        // sr = (SearchView) findViewById(R.id.srch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");
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
                        list.clear();
                        for (DataSnapshot ds :dataSnapshot.getChildren()){
                            list.add(ds.getValue(uData.class));

                        }
                        UsersAdapterClass usersAdapterClass = new UsersAdapterClass(list);
                        recyclerView.setAdapter(usersAdapterClass);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UsersList.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.datalog_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.actionsearch);
        SearchView sr = (SearchView) menuItem.getActionView();
        sr.setQueryHint("Search");
        sr.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<uData> mylist = new ArrayList<>();
                for (uData ld: list){
                    if (ld.getName().toLowerCase().contains(s.toLowerCase())){
                        mylist.add(ld);
                    }
                }
                UsersAdapterClass lAdapter = new UsersAdapterClass(mylist);
                recyclerView.setAdapter(lAdapter);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.datalog_menu_help){
            Intent intent = new Intent(UsersList.this,Help.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.datalog_menu_logout){
            Intent intent = new Intent(UsersList.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminArea.class);
        startActivity(intent);
        finish();
    }
}