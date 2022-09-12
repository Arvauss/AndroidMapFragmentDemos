package com.example.fragmentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    LocationManager locationManager;
    Location curLoc;

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


    }

    private void InitFrags(Bundle savedInstanceState){
        FragmentManager fragman = getSupportFragmentManager();
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragcontainer_id, SecondFragment.class, null, "secondfrag").commitNow();
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