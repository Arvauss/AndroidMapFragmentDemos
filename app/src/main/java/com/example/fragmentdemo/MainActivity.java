package com.example.fragmentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    LocationManager locationManager;
    Location curLoc;


    public static double lat = 0, lng = 0;
    public static boolean isGpsEnabled = false;

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){

            fab = findViewById(R.id.fab);
            fab.setSize(FloatingActionButton.SIZE_AUTO);

            InitFrags(savedInstanceState);

        } else {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE) ;
        isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGpsEnabled){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 30, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {

                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

            });
        }

    }




    private void InitFrags(Bundle savedInstanceState){
        FragmentManager fragman = getSupportFragmentManager();
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragcontainer_id, MapsFragment.class, null, "secondfrag").commitNow();
        }
        SetOnClicks(fragman);
    }

    private void SetOnClicks(FragmentManager fragman){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (fragman.findFragmentById(R.id.fragcontainer_id).getTag()){
                    case "firstfrag":
                        Log.d("1234567", fragman.findFragmentById(R.id.fragcontainer_id).getTag());
                        fragman.beginTransaction().replace(R.id.fragcontainer_id, SecondFragment.class, null, "secondfrag")
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                        fab.setImageResource(R.drawable.ic_baseline_switch_left_24);
                        break;
                    case "secondfrag":
                        Log.d("1234567", fragman.findFragmentById(R.id.fragcontainer_id).getTag());
                        fragman.beginTransaction().replace(R.id.fragcontainer_id, FirstFragment.class, null, "firstfrag")
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();

                        fab.setImageResource(R.drawable.ic_baseline_switch_right_24);
                        break;
                    case "mapfrag":
                        Log.d("1234567", fragman.findFragmentById(R.id.fragcontainer_id).getTag());
                        fragman.beginTransaction().replace(R.id.fragcontainer_id, FirstFragment.class, null, "firstfrag")
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();

                        fab.setImageResource(R.drawable.ic_baseline_switch_right_24);
                        break;

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0 :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }
}