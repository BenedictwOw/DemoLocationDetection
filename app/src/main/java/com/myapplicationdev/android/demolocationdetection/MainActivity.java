package com.myapplicationdev.android.demolocationdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient client;
    Button btnLastLocation,btnUpdateLocation,btnRemoveLocation;
    LocationCallback mLocationCallback;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = LocationServices.getFusedLocationProviderClient(this);
        btnLastLocation = findViewById(R.id.btnGetLastLocation);
        btnUpdateLocation = findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocation = findViewById(R.id.btnRemoveLocationUpdate);

        //configurations to be done
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);
        //define action
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult != null){
                    Location locData = locationResult.getLastLocation();
                    String msg = "New Loc Detected\n" +
                            "Lat: " +locData.getLatitude() + ", "+
                            "Lng: " +locData.getLongitude();
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnLastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission() == true){
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            //Get last known location.
                            if(location != null){
                                String msg = "Lat : " + location.getLatitude() + " Lng: " + location.getLongitude();
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }else{
                                String msg = "No last known location found";
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        btnUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission() == true){
                    if(checkPermission() == true){
                        client.requestLocationUpdates(mLocationRequest,mLocationCallback, null);
                    }
                }
            }
        });

        btnRemoveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.removeLocationUpdates(mLocationCallback);
            }
        });



    }
    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            String msg = "Permission not granted";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}