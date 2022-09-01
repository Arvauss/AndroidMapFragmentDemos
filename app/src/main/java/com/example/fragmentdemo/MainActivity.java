package com.example.fragmentdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        fab.setSize(FloatingActionButton.SIZE_AUTO);

        FragmentManager fragman = getSupportFragmentManager();
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragcontainer_id, FirstFragment.class, null, "firstfrag").commitNow();
        }

        SetOnClicks(fragman);



    }

    private void SetOnClicks(FragmentManager fragman){


      //Fragment curFrag = (Fragment) fragman.findFragmentById(R.id.fragcontainer_id);
/*        Toast.makeText(getApplicationContext(),curFrag.getTag(),Toast.LENGTH_SHORT).show();*/


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
                        fab.setImageResource(R.drawable.ic_baseline_switch_right_24);

                        break;
                    case "secondfrag":
                        Log.d("1234567", fragman.findFragmentById(R.id.fragcontainer_id).getTag());
                        fragman.beginTransaction().replace(R.id.fragcontainer_id, FirstFragment.class, null, "firstfrag")
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                        fab.setImageResource(R.drawable.ic_baseline_switch_left_24);

                        break;
                }
            }
        });

    }
}