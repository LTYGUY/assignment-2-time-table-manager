package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapView mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        LatLng selectedLocation = new LatLng(12.345, 67.890);
        onLocationSelected(selectedLocation);
    }
    private void onLocationSelected(LatLng selectedLocation) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("latitude", selectedLocation.latitude);
        resultIntent.putExtra("longitude", selectedLocation.longitude);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Set a default location (e.g., your city's coordinates)
        LatLng defaultLocation = new LatLng(37.7749, -122.4194);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));

        // Set a marker at the default location
        map.addMarker(new MarkerOptions().position(defaultLocation).title("Default Location"));

        // Set a click listener on the map to allow location selection
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                selectedLocation = latLng;

                // Clear the map and add a marker at the selected location
                map.clear();
                map.addMarker(new MarkerOptions().position(selectedLocation).title("Selected Location"));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // You must call the following method to ensure that the MapView resumes correctly
        MapView mapView = findViewById(R.id.mapView);
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // You must call the following method to ensure that the MapView pauses correctly
        MapView mapView = findViewById(R.id.mapView);
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // You must call the following method to ensure that the MapView is properly destroyed
        MapView mapView = findViewById(R.id.mapView);
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // You must call the following method to handle low memory situations
        MapView mapView = findViewById(R.id.mapView);
        mapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        // Check if a location was selected
        if (selectedLocation != null) {
            // Return the selected location to the calling activity (AddNewScheduleFragment)
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedLocation", selectedLocation);
            setResult(Activity.RESULT_OK, resultIntent);
        } else {
            // If no location was selected, return a canceled result
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }
}