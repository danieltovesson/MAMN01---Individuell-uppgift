package com.example.danieltovesson.hellosensor.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danieltovesson.hellosensor.R;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    // Variables
    private TextView compassTitleTextView;
    private float currentDegree;
    private ImageView compassImageView;
    private SensorManager sensorManager;
    private Sensor rotationVectorSensor, accelerometerSensor, magneticFieldSensor;
    private boolean hasRotationVectorSensor, hasAccelerometerSensor, hasMagneticFieldSensor = false;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetField = new float[3];
    private boolean lastAccelerometerSet, lastMagnetFieldSet = false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // Initialize variables
        compassTitleTextView = findViewById(R.id.compassTitleTextView);
        currentDegree = 0f;
        compassImageView = findViewById(R.id.compassImageView);
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

        // Check sensor type
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ROTATION_VECTOR:
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                currentDegree = updateCurrentDegree();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
                lastAccelerometerSet = true;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, lastMagnetField, 0, event.values.length);
                lastMagnetFieldSet = true;
                break;
        }

        // Check if last accelerometer and last magnetic field is set
        if (lastAccelerometerSet && lastMagnetFieldSet) {

            // Change current degree
            SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetField);
            SensorManager.getOrientation(rotationMatrix, orientation);
            currentDegree = updateCurrentDegree();
            lastAccelerometerSet = false;
            lastMagnetFieldSet = false;
        }

        // Round current degrees
        currentDegree = Math.round(currentDegree);

        // Rotate image
        compassImageView.setRotation(-currentDegree);

        // Calculate direction
        String where = "NW";
        if (currentDegree >= 350 || currentDegree <= 10) {
            where = "N";
        } else if (currentDegree < 350 && currentDegree > 280) {
            where = "NW";
        } else if (currentDegree <= 280 && currentDegree > 260) {
            where = "W";
        } else if (currentDegree <= 260 && currentDegree > 190) {
            where = "SW";
        } else if (currentDegree <= 190 && currentDegree > 170) {
            where = "S";
        } else if (currentDegree <= 170 && currentDegree > 100) {
            where = "SE";
        } else if (currentDegree <= 100 && currentDegree > 80) {
            where = "E";
        } else if (currentDegree <= 80 && currentDegree > 10) {
            where = "NE";
        }

        if (currentDegree >= 345 || currentDegree <= 15) {

            // Play sound
            if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
                mediaPlayer = MediaPlayer.create(this, R.raw.sound);
                mediaPlayer.start();
            }

            // Vibrate for 500 milliseconds
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        }

        // Set compass title text
        compassTitleTextView.setText(getString(R.string.compass_heading, Float.toString(currentDegree), where));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    /**
     * Starts the sensors
     */
    private void startSensors() {

        // Try to create rotation vector sensor
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Check if rotation vector sensor was created successfully
        if (rotationVectorSensor == null) {

            // Try to create accelerometer sensor and magnetic field sensor
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            // Check if accelerometer sensor and magnetic field sensor was created successfully
            if (accelerometerSensor == null || magneticFieldSensor == null) {
                noSensorsAlert();
            } else {
                hasAccelerometerSensor = sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
                hasMagneticFieldSensor = sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_UI);
            }
        } else {
            hasRotationVectorSensor = sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * Stops the sensors
     */
    private void stopSensors() {

        // Unregister sensors
        if (hasRotationVectorSensor) {
            sensorManager.unregisterListener(this, rotationVectorSensor);
        } else if (hasAccelerometerSensor && hasMagneticFieldSensor) {
            sensorManager.unregisterListener(this, accelerometerSensor);
            sensorManager.unregisterListener(this, magneticFieldSensor);
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

    /**
     * Updates the current degree
     *
     * @return the current degree
     */
    private int updateCurrentDegree() {
        return (int) (Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[0]) + 360) % 360;
    }
}
