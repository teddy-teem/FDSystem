package com.example.fdsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DataTable extends AppCompatActivity {
    private static final String TAG = "DataTable";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_table);
        Log.d(TAG, "Started Successfully");
        ListView listView = findViewById(R.id.data_table_list);
        ListView DatalistView = findViewById(R.id.data_table_list);

        Data a1 = new Data("Dhanmondi", "Dhaka","107cm", "▲");
        Data a2 = new Data("Dhanmondi", "Dhaka","106cm", "▲");
        Data a3 = new Data("Dhanmondi", "Dhaka","105cm", "▲");
        Data a4 = new Data("Dhanmondi", "Dhaka","104cm", "▲");
        Data a5 = new Data("Dhanmondi", "Dhaka","102cm", "▲");
        Data a6 = new Data("Dhanmondi", "Dhaka","101cm", "▲");
        Data a7 = new Data("Dhanmondi", "Dhaka","105cm", "▲");
        Data a8 = new Data("Dhanmondi", "Dhaka","100cm", "▲");

        ArrayList<Data> arrayList = new ArrayList<>();
        arrayList.add(a1);
        arrayList.add(a2);
        arrayList.add(a3);
        arrayList.add(a4);
        arrayList.add(a5);
        arrayList.add(a6);
        arrayList.add(a7);
        arrayList.add(a8);

        DataTableAdapter dataTableAdapter = new DataTableAdapter(this,R.layout.data_list_item,arrayList);
        listView.setAdapter(dataTableAdapter);

    }
}