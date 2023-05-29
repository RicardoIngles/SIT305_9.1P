package com.example.lostandfoundapp;

import static android.content.ContentValues.TAG;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lostandfoundapp.data.DatabaseHelper;
import com.example.lostandfoundapp.model.Advertisement;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {
    DatabaseHelper db;
    RadioGroup checkboxGroup;
    EditText NameInput, PhoneInput, DescriptionInput, DateInput;
    double latitude, longitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    AutocompleteSupportFragment autocompletefragment;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Button CurrentLocationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_activity);
        db = new DatabaseHelper(this);
        checkboxGroup = findViewById(R.id.checkBoxGroup);
        NameInput = findViewById(R.id.NameInput);
        PhoneInput = findViewById(R.id.PhoneInput);
        DescriptionInput = findViewById(R.id.DescriptionInput);
        DateInput = findViewById(R.id.DateInput);
       // LocationInput = findViewById(R.id.LocationInput);
        //CurrentLocationButton =findViewById(R.id.CurrentLocationButton);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        autocompletefragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.apiLocation);

        autocompletefragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));
        autocompletefragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG, "ERROR: " + status);
            }
        });

    }
        public void onClickCurrentLocation(View view) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            } else {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    latitude= location.getLatitude();
                                    longitude = location.getLongitude();
                                    Geocoder geocoder = new Geocoder(CreateActivity.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                        if (!addresses.isEmpty()) {
                                            String address = addresses.get(0).getAddressLine(0);
                                          autocompletefragment.setText(address);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        }

    public void onClickSave(View view) {
        int selectedId = checkboxGroup.getCheckedRadioButtonId();
        String name = NameInput.getText().toString();
        String phone = PhoneInput.getText().toString();
        String description = DescriptionInput.getText().toString();
        String date = DateInput.getText().toString();


        //Checks if all fields have been filled in
        if (selectedId == -1 || name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(CreateActivity.this, "ALL FIELDS MUST BE FILLED", Toast.LENGTH_SHORT).show();
            return;
        }
        // gets type of advertisement and inserts it into the database
        String selectedOption = "";
        RadioButton selectedRadioButton = findViewById(selectedId);
        selectedOption = selectedRadioButton.getText().toString();
        long result = db.insertAdvertisement(new Advertisement(selectedOption, name, phone, description, date, latitude,longitude));
        if (result > 0) {
            Toast.makeText(CreateActivity.this, "REGISTERED USER", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CreateActivity.this, "THERE WAS AN ISSUE REGISTERING THIS USER", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

}