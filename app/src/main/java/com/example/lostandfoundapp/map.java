package com.example.lostandfoundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.lostandfoundapp.data.DatabaseHelper;
import com.example.lostandfoundapp.model.Advertisement;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class map extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private DatabaseHelper databaseHelper;
    private Marker selectedMarker;
    private List<Advertisement> itemList;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        databaseHelper = new DatabaseHelper(this);
        itemList = (List<Advertisement>) databaseHelper.getAllAdvertisements();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap gMap) {
        googleMap = gMap;

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                if (selectedMarker != null) {
                    selectedMarker.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng);
                selectedMarker = googleMap.addMarker(markerOptions);
            }
        });
        googleMap.setOnMarkerClickListener(this);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng defaultLocation = new LatLng(-37.840935, 144.9631);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f));
        addMarkersForItems();
    }
    private void addMarkersForItems() {
        for (Advertisement item : itemList) {
            LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(item.getName()).snippet("Phone Number: " + item.getPhone() + ", Type: " + item.getSelectedOption()));
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        Advertisement clickedItem = null;
        for (Advertisement item : itemList) {
            LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
            if (latLng.equals(marker.getPosition())) {
                clickedItem = item;
                break;
            }
        }
        if (clickedItem != null) {
            Toast.makeText(this, "Item Name: " + clickedItem.getName(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        if (selectedMarker != null) {
            LatLng position = selectedMarker.getPosition();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("latitude", position.latitude);
            resultIntent.putExtra("longitude", position.longitude);
            setResult(RESULT_OK, resultIntent);
        } else {
            setResult(RESULT_CANCELED);
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}
