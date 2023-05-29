package com.example.lostandfoundapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lostandfoundapp.data.DatabaseHelper;
import com.example.lostandfoundapp.model.Advertisement;

    public class Details extends AppCompatActivity {
        DatabaseHelper db;
        TextView namedetails,descriptiondetails, locationdetails, datedetails, phonedetails;
        Button deleteButton;
        Advertisement advertisement;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_details);
            db = new DatabaseHelper(this);
            namedetails= findViewById(R.id.namedetails);
            descriptiondetails = findViewById(R.id.descriptiondetails);
            locationdetails = findViewById(R.id.locationdetails);
            datedetails = findViewById(R.id.datedetails);
            phonedetails = findViewById(R.id.phonedetails);
            deleteButton = findViewById(R.id.deleteButton);
            String name = getIntent().getStringExtra("name");

            //pulling from database
            advertisement = db.getAdvertisementByName(name);

            //displays user details individually
            namedetails.setText(advertisement.getName());
            descriptiondetails.setText(advertisement.getDescription());
            locationdetails.setText(advertisement.getLocation());
            datedetails.setText(advertisement.getDate());
            phonedetails.setText(advertisement.getPhone());
            // CRUD operation to Delete user with button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Details.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    db.deleteAdvertisementByName(name);
                    finish();
                }
            });
        }
    }