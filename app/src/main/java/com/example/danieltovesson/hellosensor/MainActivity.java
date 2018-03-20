package com.example.danieltovesson.hellosensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.danieltovesson.hellosensor.screens.AccelerometerActivity;
import com.example.danieltovesson.hellosensor.screens.CompassActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Compass button
     */
    public void openCompassActivity(View v) {
        startActivity(new Intent(MainActivity.this, CompassActivity.class));
    }

    /**
     * Called when the user taps the Accelerometer button
     */
    public void openAccelerometerActivity(View v) {
        startActivity(new Intent(MainActivity.this, AccelerometerActivity.class));
    }
}
