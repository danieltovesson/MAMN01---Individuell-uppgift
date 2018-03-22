package com.example.danieltovesson.hellosensor.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.danieltovesson.hellosensor.R;
import com.example.danieltovesson.hellosensor.helpers.Functions;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    // Variables
    private TextView xValueTextView, yValueTextView, zValueTextView, directionTextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean hasAccelerometerSensor = false;
    private float[] lastAccelerometer = new float[3];
    private float[] coordinatesHistory = new float[2];
    private String[] direction = {null, null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        // Initialize variables
        xValueTextView = findViewById(R.id.xValueTextView);
        yValueTextView = findViewById(R.id.yValueTextView);
        zValueTextView = findViewById(R.id.zValueTextView);
        directionTextView = findViewById(R.id.directionTextView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensors();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSensors();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Adds a low pass filter to the sensor data
        lastAccelerometer = Functions.lowPassFilter(event.values.clone(), lastAccelerometer);

        // Get x, y and z coordinates
        float xValue = lastAccelerometer[0];
        float yValue = lastAccelerometer[1];
        float zValue = lastAccelerometer[2];

        // Change background color when device is flat
        if (xValue == 0 && yValue == 0) {
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }

        // Set coordinate text views
        xValueTextView.setText(getString(R.string.coordinate, "X", Float.toString(xValue)));
        yValueTextView.setText(getString(R.string.coordinate, "Y", Float.toString(yValue)));
        zValueTextView.setText(getString(R.string.coordinate, "Z", Float.toString(zValue)));

        // Calculate change in x and y coordinates
        float xChange = coordinatesHistory[0] - xValue;
        float yChange = coordinatesHistory[1] - yValue;

        // Save history for coordinates
        coordinatesHistory[0] = xValue;
        coordinatesHistory[1] = yValue;

        // Check if it moved left or right
        if (xChange > 2) {
            direction[0] = "left";
        } else if (xChange < -2) {
            direction[0] = "right";
        } else {
            direction[0] = null;
        }

        // Check if it moved up or down
        if (yChange > 2) {
            direction[1] = "down";
        } else if (yChange < -2) {
            direction[1] = "up";
        } else {
            direction[1] = null;
        }

        // Print direction in direction text view
        if (direction[0] != null || direction[1] != null) {
            if (direction[0] != null && direction[1] != null) {
                directionTextView.setText(getString(R.string.twoDirections, direction[0], direction[1]));
            } else if (direction[0] != null) {
                directionTextView.setText(getString(R.string.oneDirection, direction[0]));
            } else if (direction[1] != null) {
                directionTextView.setText(getString(R.string.oneDirection, direction[1]));
            }
        } else {
            directionTextView.setText(getString(R.string.noDirection));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    /**
     * Starts the sensors
     */
    private void startSensors() {

        // Try to create accelerometer sensor
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Check if accelerometer sensor was created successfully
        if (accelerometerSensor == null) {
            noSensorsAlert();
        } else {
            hasAccelerometerSensor = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * Stops the sensors
     */
    private void stopSensors() {

        // Unregister sensors
        if (hasAccelerometerSensor) {
            sensorManager.unregisterListener(this, accelerometerSensor);
        }
    }

    /**
     * Shows no sensors alert
     */
    private void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device does not support the desired sensors")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }
}
