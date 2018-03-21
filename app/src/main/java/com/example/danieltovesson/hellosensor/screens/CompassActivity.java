package com.example.danieltovesson.hellosensor.screens;

import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danieltovesson.hellosensor.R;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    // Variables
    private TextView compassTitleTextView;
    private float currentDegree;
    private ImageView compassImageView;
    private SensorManager sensorManager;

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

        // Register sensor manager
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister sensor manager
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Get the number of degrees rotated around the z-axis
        float degree = Math.round(event.values[0]);

        // Set compass title text view to show the number of degrees
        compassTitleTextView.setText("Heading: " + Float.toString(degree) + " degrees");

        // Create rotation animation
        RotateAnimation rotateAnimation = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // Set duration of animation
        rotateAnimation.setDuration(210);

        // Set fill after
        rotateAnimation.setFillAfter(true);

        // Start animation
        compassImageView.startAnimation(rotateAnimation);
        currentDegree = -degree;

        if (degree >= 345 || degree <= 15) {

            // Play sound
            final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound);
            mediaPlayer.start();

            // Vibrate for 500 milliseconds
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }
}
