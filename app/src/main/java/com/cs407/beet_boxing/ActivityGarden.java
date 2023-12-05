package com.cs407.beet_boxing;

import static com.cs407.beet_boxing.util.EnumProduceType.APPLE;
import static com.cs407.beet_boxing.util.EnumProduceType.BANANA;
import static com.cs407.beet_boxing.util.EnumProduceType.BEET;
import static com.cs407.beet_boxing.util.EnumProduceType.CARROT;
import static com.cs407.beet_boxing.util.EnumProduceType.GINGER;
import static com.cs407.beet_boxing.util.EnumProduceType.MELON;
import static com.cs407.beet_boxing.util.EnumProduceType.ONION;
import static com.cs407.beet_boxing.util.EnumProduceType.ORANGE;
import static com.cs407.beet_boxing.util.EnumProduceType.POTATO;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cs407.beet_boxing.persistence.PersistentInfo;

import java.util.HashMap;

public class ActivityGarden extends AppCompatActivity {
    private ImageButton buttonProduce1;
    private ImageButton buttonProduce2;
    private ImageButton buttonProduce3;
    private ImageButton buttonProduce4;
    private ImageButton buttonProduce5;
    private ImageButton buttonProduce6;
    private ImageButton buttonProduce7;
    private ImageButton buttonProduce8;
    private ImageButton buttonProduce9;

    private ImageButton produceIconCarrot;
    private ImageButton produceIconBanana;
    private ImageButton produceIconApple;
    private ImageButton produceIconPotato;
    private ImageButton produceIconOnion;
    private ImageButton produceIconOrange;
    private ImageButton produceIconMelon;
    private ImageButton produceIconGinger;
    private ImageButton produceIconBeet;

    private TextView produceNumCarrot;
    private TextView produceNumBanana;
    private TextView produceNumApple;
    private TextView produceNumPotato;
    private TextView produceNumOnion;
    private TextView produceNumOrange;
    private TextView produceNumMelon;
    private TextView produceNumGinger;
    private TextView produceNumBeet;
    private HashMap<Integer, MediaPlayer> mediaPlayers;
    private View editMenuLayout;
    private Button closeEditMenuButton;
    private boolean isEditOpen = false;
    private HashMap<Integer, Boolean> cooldownMap = new HashMap<>();
    private long globalStartTime = -1;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden);



        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(e -> startActivity(new Intent(this, ActivityTiltGame.class)));

        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(e -> startActivity(new Intent(this, SettingsActivity.class)));

        editMenuLayout = findViewById(R.id.edit_menu_layout);


        // Set click listener to show the edit menu
        Button editButton = findViewById(R.id.edit);



        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditOpen) {
                    editMenuLayout.setVisibility(View.VISIBLE); // Show the edit menu
                    isEditOpen = true;
                } else {
                    editMenuLayout.setVisibility(View.GONE); // Hide the edit menu
                    isEditOpen = false;
                }
            }
        });

//        ImageButton[] buttonProduceArray = new ImageButton[]{buttonProduce1, buttonProduce2, buttonProduce3,
//                buttonProduce4, buttonProduce5, buttonProduce6,
//                buttonProduce7, buttonProduce8, buttonProduce9};

        Integer[] iconArray = new Integer[]{R.id.icon_carrot, R.id.icon_banana, R.id.icon_apple,
                R.id.icon_potato, R.id.icon_onion, R.id.icon_orange,
                R.id.icon_melon, R.id.icon_ginger, R.id.icon_beet};


        // Initialize buttons
        buttonProduce1 = findViewById(R.id.button_placeholder_1);
        buttonProduce2 = findViewById(R.id.button_placeholder_2);
        buttonProduce3 = findViewById(R.id.button_placeholder_3);
        buttonProduce4 = findViewById(R.id.button_placeholder_4);
        buttonProduce5 = findViewById(R.id.button_placeholder_5);
        buttonProduce6 = findViewById(R.id.button_placeholder_6);
        buttonProduce7 = findViewById(R.id.button_placeholder_7);
        buttonProduce8 = findViewById(R.id.button_placeholder_8);
        buttonProduce9 = findViewById(R.id.button_placeholder_9);

        // Initialize placeholders as empty
        buttonProduce1.setTag(true);
        buttonProduce2.setTag(true);
        buttonProduce3.setTag(true);
        buttonProduce4.setTag(true);
        buttonProduce5.setTag(true);
        buttonProduce6.setTag(true);
        buttonProduce7.setTag(true);
        buttonProduce8.setTag(true);
        buttonProduce9.setTag(true);


        produceIconCarrot = editMenuLayout.findViewById(R.id.icon_carrot);
        produceIconBanana = editMenuLayout.findViewById(R.id.icon_banana);
        produceIconApple = editMenuLayout.findViewById(R.id.icon_apple);
        produceIconPotato = editMenuLayout.findViewById(R.id.icon_potato);
        produceIconOnion = editMenuLayout.findViewById(R.id.icon_onion);
        produceIconOrange = editMenuLayout.findViewById(R.id.icon_orange);
        produceIconMelon = editMenuLayout.findViewById(R.id.icon_melon);
        produceIconGinger = editMenuLayout.findViewById(R.id.icon_ginger);
        produceIconBeet = editMenuLayout.findViewById(R.id.icon_beet);

        produceIconCarrot.setTag(R.id.icon_carrot);
        produceIconBanana.setTag(R.id.icon_banana);
        produceIconApple.setTag(R.id.icon_apple);
        produceIconPotato.setTag(R.id.icon_potato);
        produceIconOnion.setTag(R.id.icon_onion);
        produceIconOrange.setTag(R.id.icon_orange);
        produceIconMelon.setTag(R.id.icon_melon);
        produceIconGinger.setTag(R.id.icon_ginger);
        produceIconBeet.setTag(R.id.icon_beet);

        produceNumCarrot = editMenuLayout.findViewById(R.id.number_carrot);
        produceNumBanana = editMenuLayout.findViewById(R.id.number_banana);
        produceNumApple = editMenuLayout.findViewById(R.id.number_apple);
        produceNumPotato = editMenuLayout.findViewById(R.id.number_potato);
        produceNumOnion = editMenuLayout.findViewById(R.id.number_onion);
        produceNumOrange = editMenuLayout.findViewById(R.id.number_orange);
        produceNumMelon = editMenuLayout.findViewById(R.id.number_melon);
        produceNumGinger = editMenuLayout.findViewById(R.id.number_ginger);
        produceNumBeet = editMenuLayout.findViewById(R.id.number_beet);





        // set numbers next to produce icon indicating how many users have collected
        setProduceAmount();


        // Create the OnTouchListener
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Boolean isOnCooldown = cooldownMap.getOrDefault(view.getId(), false);
                    if (isOnCooldown) {
                        // This view is on cooldown, do not allow dragging
                        return false;
                    }


                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDragAndDrop(clipData, shadowBuilder, view, 0);
//                    view.setVisibility(View.INVISIBLE); // Optional: make the view invisible during the drag operation
                    return true;
                }
                return false;
            }
        };



        // Set the touch listener to all your produce ImageButtons
        produceIconCarrot.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(CARROT, 0) >= 5 ? touchListener : null);
        produceIconBanana.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(BANANA, 0) >= 5 ? touchListener : null);
        produceIconApple.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(APPLE, 0) >= 5 ? touchListener : null);
        produceIconPotato.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(POTATO, 0) >= 5 ? touchListener : null);
        produceIconOnion.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(ONION, 0) >= 5 ? touchListener : null);
        produceIconOrange.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(ORANGE, 0) >= 5 ? touchListener : null);
        produceIconMelon.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(MELON, 0) >= 5 ? touchListener : null);
        produceIconGinger.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(GINGER, 0) >= 5 ? touchListener : null);
        produceIconBeet.setOnTouchListener(PersistentInfo.gameData.inventory.getOrDefault(BEET, 0) >= 5 ? touchListener : null);

        for (int id : new int[]{R.id.icon_carrot, R.id.icon_banana, R.id.icon_apple, R.id.icon_potato,
                R.id.icon_onion, R.id.icon_orange, R.id.icon_melon, R.id.icon_ginger, R.id.icon_beet}) {
            cooldownMap.put(id, false);
        }

        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View receivingLayoutView, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // ...
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // ...
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // ...
                        break;
                    case DragEvent.ACTION_DROP:
                        View draggedView = (View) dragEvent.getLocalState();
                        ImageButton droppedOn = (ImageButton) receivingLayoutView;
                        Drawable draggedIconDrawable = ((ImageButton) draggedView).getDrawable();

                        Boolean isEmpty = (Boolean) droppedOn.getTag();
                        if (isEmpty != null && isEmpty) {
                            startCooldown((ImageButton) draggedView, droppedOn, 60000); // 1 minute cooldown

                            // Find the corresponding TextView for the countdown and start it
                            TextView countdownView = findCountdownTextViewForButton(draggedView.getId());
                            if (countdownView != null) {
                                startCountdownTimer(countdownView, 60000);
                            }

                            // Set the drawable on the garden placeholder ImageButton
                            droppedOn.setImageDrawable(draggedIconDrawable);
                            droppedOn.setTag(false); // Set the tag to false, indicating it's no longer empty

                            // Set click listeners
                            droppedOn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Integer produceIconId = (Integer) draggedView.getTag();

                                    // Now, since the placeholder was empty and we just dropped an item, we can play the sound associated with the item
                                    // Assuming the ID of the draggedView correlates to a specific sound
                                    toggleSound(getSoundResourceIdForIcon(draggedView.getId()), droppedOn.getId(), produceIconId);
                                }
                            });

                            // when produce is dropped on a placeholder, its number reduces
                            reduceProduceNum(draggedView.getId());
                            // reset the amount of produce collected
                            setProduceAmount();
                        }

                        // Optional: make the original produce icon invisible or remove it
//                        draggedView.setVisibility(View.INVISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // ...
                        break;
                    default:
                        break;
                }
                return true;
            }
        };

        buttonProduce1.setOnDragListener(dragListener);
        buttonProduce2.setOnDragListener(dragListener);
        buttonProduce3.setOnDragListener(dragListener);
        buttonProduce4.setOnDragListener(dragListener);
        buttonProduce5.setOnDragListener(dragListener);
        buttonProduce6.setOnDragListener(dragListener);
        buttonProduce7.setOnDragListener(dragListener);
        buttonProduce8.setOnDragListener(dragListener);
        buttonProduce9.setOnDragListener(dragListener);


        // Initialize mediaPlayers HashMap
        mediaPlayers = new HashMap<>();


    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop all MediaPlayer objects
        for (MediaPlayer player : mediaPlayers.values()) {
            if (player.isPlaying()) {
                player.pause();
            }
        }
    }



    private void continueCooldown(final ImageButton draggedView, final long remainingTime) {
        // Update the UI immediately to reflect the cooldown state
        // You might want to disable the view, change its appearance, etc.
        draggedView.setEnabled(false); // Example: disable the ImageButton during cooldown

        // Schedule the end of the cooldown using a Handler
        new Handler().postDelayed(() -> {
            cooldownMap.put(draggedView.getId(), false);
            runOnUiThread(() -> {
                // When the cooldown ends, update the UI accordingly
                // Re-enable the view, reset its appearance, etc.
                draggedView.setEnabled(true); // Re-enable the ImageButton

                // Since the cooldown has finished, you can now set the onClickListener back to its original state if needed
                // However, you might need additional logic to determine if it should really be re-enabled, based on your game's rules
            });
        }, remainingTime);

        // Start a countdown display if you have a TextView for this purpose
        TextView countdownView = findCountdownTextViewForButton(draggedView.getId());
        if (countdownView != null) {
            startCountdownTimer(countdownView, remainingTime);
        }
    }




    private TextView findCountdownTextViewForButton(int iconId) {

        if (iconId == R.id.icon_carrot) {
            return findViewById(R.id.countdown_carrot);
        } else if (iconId == R.id.icon_banana) {
            return findViewById(R.id.countdown_banana);
        } else if (iconId == R.id.icon_apple) {
            return findViewById(R.id.countdown_apple);
        } else if (iconId == R.id.icon_potato) {
            return findViewById(R.id.countdown_potato);
        } else if (iconId == R.id.icon_onion) {
            return findViewById(R.id.countdown_onion);
        } else if (iconId == R.id.icon_orange) {
            return findViewById(R.id.countdown_orange);
        } else if (iconId == R.id.icon_melon) {
            return findViewById(R.id.countdown_melon);
        } else if (iconId == R.id.icon_ginger) {
            return findViewById(R.id.countdown_ginger);
        } else if (iconId == R.id.icon_beet) {
            return findViewById(R.id.countdown_beet);
        }

        return null;
    }

    private void startCountdownTimer(TextView countdownView, long timeInFuture) {
        new CountDownTimer(timeInFuture, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                countdownView.setText(""); // Clear the countdown when finished
            }
        }.start();
    }


    private void startCooldown(final ImageButton draggedView, final ImageButton droppedOn, long cooldownTime) {
        // Mark the view as on cooldown
        cooldownMap.put(draggedView.getId(), true);

        // Start a delay to remove the view after the cooldown
        new Handler().postDelayed(() -> {
            cooldownMap.put(draggedView.getId(), false); // Cooldown finished
            runOnUiThread(() -> {
                droppedOn.setImageDrawable(null); // Remove the drawable from the droppedOn ImageButton
                droppedOn.setTag(true); // Reset the tag to indicate it's empty
                droppedOn.setOnClickListener(null);


                // Release the MediaPlayer associated with the droppedOn ImageButton if it exists
                MediaPlayer player = mediaPlayers.remove(droppedOn.getId());
                if (player != null) {
                    if (player.isPlaying()) {
                        player.stop();
                    }
                    player.release();
                }


            });
        }, cooldownTime);
    }




    private void reduceProduceNum(int produceId) {
        if (produceId == R.id.icon_carrot) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(CARROT, 0);
            PersistentInfo.gameData.inventory.put(CARROT, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(CARROT, 0) < 5) {
                produceIconCarrot.setOnTouchListener(null);
            }
        } else if (produceId == R.id.icon_banana) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(BANANA, 0);
            PersistentInfo.gameData.inventory.put(BANANA, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(BANANA, 0) < 5) {
                produceIconBanana.setOnTouchListener(null);
            }
        } else if (produceId == R.id.icon_apple) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(APPLE, 0);
            PersistentInfo.gameData.inventory.put(APPLE, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(APPLE, 0) < 5) {
                produceIconApple.setOnTouchListener(null);
            }
        } else if (produceId == R.id.icon_potato) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(POTATO, 0);
            PersistentInfo.gameData.inventory.put(POTATO, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(POTATO, 0) < 5) {
                produceIconPotato.setOnTouchListener(null);
            }
        } else if (produceId == R.id.icon_onion) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(ONION, 0);
            PersistentInfo.gameData.inventory.put(ONION, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(ONION, 0) < 5) {
                produceIconOnion.setOnTouchListener(null);
            }
        } else if (produceId == R.id.icon_orange) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(ORANGE, 0);
            PersistentInfo.gameData.inventory.put(ORANGE, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(ORANGE, 0) < 5) {
                produceIconOrange.setOnTouchListener(null);
            }
        } else if (produceId == R.id.icon_melon) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(MELON, 0);
            PersistentInfo.gameData.inventory.put(MELON, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(MELON, 0) < 5) {
                produceIconMelon.setOnTouchListener(null);
            }
        } else if (produceId == R.id.icon_ginger) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(GINGER, 0);
            PersistentInfo.gameData.inventory.put(GINGER, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(GINGER, 0) < 5) {
                produceIconGinger.setOnTouchListener(null);
            }
        } else if (produceId == R.id.icon_beet) {
            int previousAmount = PersistentInfo.gameData.inventory.getOrDefault(BEET, 0);
            PersistentInfo.gameData.inventory.put(BEET, previousAmount - 5);
            if (PersistentInfo.gameData.inventory.getOrDefault(BEET, 0) < 5) {
                produceIconBeet.setOnTouchListener(null);
            }
        }
    }

    private void setProduceAmount() {
        produceNumCarrot.setText(PersistentInfo.gameData.inventory.getOrDefault(CARROT, 0).toString());
        produceNumBanana.setText(PersistentInfo.gameData.inventory.getOrDefault(BANANA, 0).toString());
        produceNumApple.setText(PersistentInfo.gameData.inventory.getOrDefault(APPLE, 0).toString());
        produceNumPotato.setText(PersistentInfo.gameData.inventory.getOrDefault(POTATO, 0).toString());
        produceNumOnion.setText(PersistentInfo.gameData.inventory.getOrDefault(ONION, 0).toString());
        produceNumOrange.setText(PersistentInfo.gameData.inventory.getOrDefault(ORANGE, 0).toString());
        produceNumMelon.setText(PersistentInfo.gameData.inventory.getOrDefault(MELON, 0).toString());
        produceNumGinger.setText(PersistentInfo.gameData.inventory.getOrDefault(GINGER, 0).toString());
        produceNumBeet.setText(PersistentInfo.gameData.inventory.getOrDefault(BEET, 0).toString());
    }

//Sage edited these to use the new mp3 file assets
    private int getSoundResourceIdForIcon(int iconId) {
        if (iconId == R.id.icon_carrot) {
            return R.raw.violin_trimmed;
        } else if (iconId == R.id.icon_banana) {
            return R.raw.guitar_trimmed;
        } else if (iconId == R.id.icon_apple) {
            return R.raw.piano_trimmed;
        } else if (iconId == R.id.icon_potato) {
            return R.raw.drumset2_trimmed;
        } else if (iconId == R.id.icon_onion) {
            return R.raw.meow_trimmed;
        } else if (iconId == R.id.icon_orange) {
            return R.raw.stabs_trimmed;
        } else if (iconId == R.id.icon_melon) {
            return R.raw.xylo_trimmed;
        } else if (iconId == R.id.icon_ginger) {
            return R.raw.synth_trimmed;
        } else if (iconId == R.id.icon_beet) {
            return R.raw.drumset1_trimmed;
        } else {
            return -1; // Invalid ID or default sound
        }
    }

    private void toggleSound(int soundResourceId, int buttonId, int produceIconId) {


        // Check if the button already has a MediaPlayer associated with it
        if (mediaPlayers.containsKey(buttonId)) {
            MediaPlayer player = mediaPlayers.get(buttonId);
            if (player.isPlaying()) {
                player.pause(); // Pause playback
            } else {
                // Resume playback, but synchronize with global start time
                if (globalStartTime != -1) {
                    long soundPosition;
                    if (produceIconId == R.id.icon_orange) {
                        soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() + 400);
                    }
                    else {
                        soundPosition = (System.currentTimeMillis() - globalStartTime) % player.getDuration();
                    }
                    System.out.println("Seeking to: " + soundPosition);

                    player.setOnSeekCompleteListener(mp -> {
                        mp.start();
                        mp.setOnSeekCompleteListener(null); // Reset listener
                    });

                    player.seekTo((int) soundPosition);
                    System.out.println("Current position after seek: " + player.getCurrentPosition());
                }
                player.start(); // Resume playback
            }
        } else {
            // No MediaPlayer for this button yet, create and start it
            MediaPlayer player = MediaPlayer.create(this, soundResourceId);
            player.setLooping(true);

            // If this is the first sound being played, set the global start time
            if (globalStartTime == -1) {
                globalStartTime = System.currentTimeMillis();
                System.out.println("globalStartTime: " + globalStartTime);
            } else {
                // Synchronize the start of this new sound with the others
                long soundPosition;
                if (produceIconId == R.id.icon_melon) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() + 4);
                }
                else if (produceIconId == R.id.icon_orange) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() + -60);
                }
                else if (produceIconId == R.id.icon_beet) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() - 13);
                }
                else if (produceIconId == R.id.icon_banana) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() + 125);
                }
                else if (produceIconId == R.id.icon_apple) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() + 48);
                }
                else if (produceIconId == R.id.icon_potato) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() + 100);
                }
                else if (produceIconId == R.id.icon_carrot) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() - 8);
                }
                else if (produceIconId == R.id.icon_ginger) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() - 15);
                }
                else if (produceIconId == R.id.icon_onion) {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration());
                }
                else {
                    soundPosition = (System.currentTimeMillis() - globalStartTime) % player.getDuration();
                }
//                long soundPosition = (System.currentTimeMillis() - globalStartTime) % (player.getDuration() + 560);
                System.out.println("Seeking to: " + soundPosition);
                // player should be in start/pause state before calling seekTo
//                player.start();
//                player.pause();
                player.setOnSeekCompleteListener(mp -> {
                    mp.start();
                    mp.setOnSeekCompleteListener(null); // Reset listener
                });
                if (soundResourceId == R.id.icon_orange) {
                    soundPosition += 500;
                }
                player.seekTo((int) soundPosition);
                System.out.println("Current position after seek: " + player.getCurrentPosition());
            }


            player.start();
            mediaPlayers.put(buttonId, player); // Store it in the map
        }
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
        // Release all MediaPlayer resources
        for (MediaPlayer player : mediaPlayers.values()) {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.release();
            }
        }
        mediaPlayers.clear();
    }
}