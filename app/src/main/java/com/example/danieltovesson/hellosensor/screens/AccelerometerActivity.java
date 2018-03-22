package com.example.danieltovesson.hellosensor.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.danieltovesson.hellosensor.R;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    // Variables
    private TextView xValueTextView, yValueTextView, zValueTextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean hasAccelerometerSensor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        // Initialize variables
        xValueTextView = findViewById(R.id.xValueTextView);
        yValueTextView = findViewById(R.id.yValueTextView);
        zValueTextView = findViewById(R.id.zValueTextView);
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

        // Set coordinate text views
        xValueTextView.setText(getString(R.string.coordinate, "X", Float.toString(event.values[0])));
        yValueTextView.setText(getString(R.string.coordinate, "Y", Float.toString(event.values[1])));
        zValueTextView.setText(getString(R.string.coordinate, "Z", Float.toString(event.values[2])));
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
