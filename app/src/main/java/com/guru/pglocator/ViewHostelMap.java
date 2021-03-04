package com.guru.pglocator;

import android.icu.util.BuddhistCalendar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewHostelMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hostel_map);
        Bundle bundle=getIntent().getExtras();

        location=bundle.getString("location");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    String location1[]=location.split(",");
    Log.e("lati",location1[0]);
    Log.e("longi",location1[1]);
        String sub=location1[0].substring(10);

        int index=location1[1].lastIndexOf(location1[1]);
        String sub1=location1[1].substring(0,14);
        Log.e("longi1",sub1);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(sub),Double.parseDouble(sub1));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Hostel"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
