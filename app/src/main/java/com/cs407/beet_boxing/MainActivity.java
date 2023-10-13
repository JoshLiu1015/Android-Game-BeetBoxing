package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.content.Context;
import android.view.View;
import android.animation.ObjectAnimator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private List<View> fallingObjects;
    private TextView score;
    private ImageView box;
    private int oldScore;
    private boolean isCollision;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // An `ObjectAnimator` object that animates `fallingObject`,
        // moving it vertically (`translationY`) from its original position
        // to 1500f over 3 seconds
        isCollision = false;

        box = findViewById(R.id.box);
        score = findViewById(R.id.score);

        fallingObjects = new ArrayList<>();
//        fallingObject = findViewById(R.id.falling_object1);
//        ObjectAnimator animation = ObjectAnimator.ofFloat(fallingObject, "translationY", 0f, 2100f);
//        animation.setDuration(3000);
//
//        animation.setRepeatCount(ObjectAnimator.INFINITE);
//        animation.start();

        setupFallingObject(R.id.falling_object1, 3000, 5000);  // 3 seconds duration, 0 delay
        setupFallingObject(R.id.falling_object2, 3500, 10000);  // 3.5 seconds duration, 0.5 second delay
        setupFallingObject(R.id.falling_object3, 4000, 15000);  // 4 seconds duration, 1 second delay

    }

//    private ObjectAnimator createFallingAnimation(View view, long duration) {
//        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY", 0f, 2100f);
//        // 3 seconds for the object to fall
//        animation.setDuration(duration);
//        // repeating infinitely.
////        animation.setRepeatCount(ObjectAnimator.INFINITE);
//        return animation;
//    }

    private ObjectAnimator createFallingAnimation(View view, long duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationY", 0f, 2100f);
        animation.setDuration(duration);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Reset the object's position once it goes out of screen
                view.setTranslationY(0f);
                // Also make sure the object is made invisible until the next time it starts falling
                view.setVisibility(View.GONE);
            }
        });
        return animation;
    }


    private void setupFallingObject(int viewId, long duration, long startDelay) {
        View fallingObject = findViewById(viewId);
        fallingObjects.add(fallingObject);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animation = createFallingAnimation(fallingObject, duration);
                animation.start();
            }
        }, startDelay);


//        ObjectAnimator animation = createFallingAnimation(fallingObject, duration);
//        animation.setStartDelay(startDelay);  // Setting different start delays
//        animation.start();
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        // Called when sensor data changes.
        // It moves a `View` named `box` horizontally based on the `x` accelerometer reading.
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];

            // Use the x value to determine the direction of the tilt
            // Move the box left or right based on this value

            // Move the box left or right based on this value
//            ImageView box = findViewById(R.id.box);
            float newX = box.getX() - x * 2; // Multiplied by 5 for sensitivity, adjust as needed
            box.setX(newX);


//            ImageView fallingObject = findViewById(R.id.falling_object);
//            TextView score = findViewById(R.id.score);

            for (View collisionObject : fallingObjects) {
                if (collisionObject.getVisibility() == View.VISIBLE &&
                        collisionObject.getY() + collisionObject.getHeight() >= box.getTop() &&
                        collisionObject.getY() <= box.getBottom() && // Ensure object is within screen bounds
                        box.getX() + box.getWidth() > collisionObject.getX() &&
                        box.getX() < collisionObject.getX() + collisionObject.getWidth()) {

                    // Collision detected
                    // Handle the collision (e.g., increase score, play sound, etc.)
                    if (!isCollision) {
                        //                    Toast.makeText(MainActivity.this, "collected", Toast.LENGTH_LONG).show();

                        oldScore = Integer.parseInt(score.getText().toString());
                        oldScore++;
                        score.setText(String.valueOf(oldScore));

                        // Set the flag to indicate a collision
                        isCollision = true;

                        // Make the fallingObject invisible
                        collisionObject.setVisibility(View.GONE);
                    }
                }
                else
                    // Reset the flag when objects are no longer colliding
                    isCollision = false;
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }











}