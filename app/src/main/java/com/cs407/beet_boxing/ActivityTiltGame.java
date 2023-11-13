package com.cs407.beet_boxing;

        import androidx.appcompat.app.AppCompatActivity;

        import android.animation.Animator;
        import android.animation.AnimatorListenerAdapter;
        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.os.Bundle;
        import android.hardware.Sensor;
        import android.hardware.SensorManager;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorEvent;
        import android.content.Context;
        import android.util.DisplayMetrics;
        import android.view.View;
        import android.animation.ObjectAnimator;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.os.Handler;

        import java.util.ArrayList;
        import java.util.List;

public class ActivityTiltGame extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private List<View> fallingObjects;
    private TextView score;
    private ImageView box;
    private int oldScore;
    private boolean isCollision;
    private int screenWidth;
    private Button gardenButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilt_game);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // An `ObjectAnimator` object that animates `fallingObject`,
        // moving it vertically (`translationY`) from its original position
        // to 1500f over 3 seconds
        isCollision = false;

        box = findViewById(R.id.box);
        score = findViewById(R.id.score);

        fallingObjects = new ArrayList<>();

        createFallingAnimation(findViewById(R.id.fallingCarrot));  // 3 seconds duration, 0 delay
        createFallingAnimation(findViewById(R.id.fallingBeet));  // 3.5 seconds duration, 0.5 second delay
        createFallingAnimation(findViewById(R.id.fallingRock));  // 4 seconds duration, 1 second delay
        createFallingAnimation(findViewById(R.id.fallingApple));
        createFallingAnimation(findViewById(R.id.fallingMelon));
        createFallingAnimation(findViewById(R.id.fallingOrange));

        gardenButton = findViewById(R.id.button1);
        gardenButton.setOnClickListener(this::startGarden);

    }

    public void startGarden(View view) {
        Intent intent = new Intent(this, ActivityGarden.class);
        startActivity(intent);
    }


    private void createFallingAnimation(View fallingObject) {
        fallingObjects.add(fallingObject);

        ObjectAnimator animation = ObjectAnimator.ofFloat(fallingObject, "translationY", 0f, 2100f);

        // Change the X position, speed, or any other property for variety
        float initialX = (float) (Math.random() * screenWidth);
        fallingObject.setX(initialX);

        long initialDuration = (long) (2000 + Math.random() * 2000);
        animation.setDuration(initialDuration);

        // Use a Handler to start the animation after a random initial delay
        long initialDelay = (long) (3000 + Math.random() * 1000);
        new Handler().postDelayed(animation::start, initialDelay);

        // Initially set the view to be invisible
        fallingObject.setVisibility(View.INVISIBLE);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Make the view visible when animation starts
                fallingObject.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Reset the object's position once it goes out of screen
                fallingObject.setTranslationY(0f);

                fallingObject.setVisibility(View.INVISIBLE);
                // Change the X position, speed, or any other property for variety
                float randomX = (float) (Math.random() * screenWidth);
                fallingObject.setX(randomX);
                // Restart the animation
                long randomDuration = (long) (2000 + Math.random() * 2000);
                animation.setDuration(randomDuration);

                // Get a random delay before the next fall starts
                long randomDelay = (long) (Math.random() * 1000);
                // Use a Handler to start the animation after the random delay
                new Handler().postDelayed(animation::start, randomDelay);

//                animation.start();
            }
        });


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

            // Ensure box does not move off the screen
            if (newX < 0) {
                newX = 0;
            } else if (newX > screenWidth - box.getWidth()) {
                newX = screenWidth - box.getWidth();
            }

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
                        collisionObject.setVisibility(View.INVISIBLE);
//                        createFallingAnimation(collisionObject);
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