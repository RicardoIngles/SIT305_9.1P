package com.example.lostandfoundapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lostandfoundapp.data.DatabaseHelper;
import com.example.lostandfoundapp.util.Util;
import java.util.ArrayList;

public class DisplayAdvertisment extends AppCompatActivity {
    DatabaseHelper db;
    ListView listView;
    ArrayList<String> dataList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialising
        setContentView(R.layout.fragment_display_advertisment);
        db = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        Cursor cursor = db.getAllAdvertisementsCursor();

        //This if statement will check if there are any advertisements in the database and if there is it will add it to the Array
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String selected_option = cursor.getString(cursor.getColumnIndex(Util.SELECTED_OPTION));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Util.NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(Util.PHONE));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Util.DATE));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(Util.LATITUDE));
                String data = "SELECTED: " + selected_option + "\nNAME: " + name + "\nPHONE: " + phone + "\nDESCRIPTION: " + description + "\nDATE: " + date + "\nLOCATION " + location;
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = (String) adapterView.getItemAtPosition(position);
                String name = selectedItem.substring(selectedItem.indexOf("NAME: ") + 6, selectedItem.indexOf("\nPHONE: "));
                Intent intent = new Intent(DisplayAdvertisment.this, Details.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}