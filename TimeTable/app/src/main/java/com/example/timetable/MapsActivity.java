//Written by: Lorraine, Yu Feng
package com.example.timetable;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.timetable.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("info_markerClicked","Marker clicked");
        return false;
    }

    @Override
    public void onMapClick(LatLng point) {
        // Create a new marker at the clicked location
        mMap.addMarker(new MarkerOptions().position(point).title("Selected Location"));

        // Return the location to the calling activity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("latitude", point.latitude);
        returnIntent.putExtra("longitude", point.longitude);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng wilkieEdge = new LatLng(1.302850, 103.847549);
        mMap.addMarker(new MarkerOptions().position(wilkieEdge).title("Marker @ Wilkie Edge Campus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wilkieEdge, 12.0f));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this); // Set the map to listen for onMapClick events
    }

}
