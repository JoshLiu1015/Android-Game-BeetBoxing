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
import android.media.MediaPlayer;
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

import com.cs407.beet_boxing.persistence.GameData;
import com.cs407.beet_boxing.persistence.PersistentInfo;

import java.util.HashMap;

/** @noinspection DataFlowIssue*/
public class ActivityGarden extends AppCompatActivity {

    private HashMap<Integer, MediaPlayer> mediaPlayers;
    private View editMenuLayout;
    private Button closeEditMenuButton;
    private boolean isEditOpen = false;
    private HashMap<Integer, Boolean> cooldownMap = new HashMap<>();
    private long globalStartTime = -1;

    private static final int[] BUTTON_PRODUCE_IDS = {
            R.id.button_placeholder_1, R.id.button_placeholder_2, R.id.button_placeholder_3,
            R.id.button_placeholder_4, R.id.button_placeholder_5, R.id.button_placeholder_6,
            R.id.button_placeholder_7, R.id.button_placeholder_8, R.id.button_placeholder_9
    };

    private static final int[] ICON_IDS = {
            R.id.icon_carrot, R.id.icon_banana, R.id.icon_apple,
            R.id.icon_potato, R.id.icon_onion, R.id.icon_orange,
            R.id.icon_melon, R.id.icon_ginger, R.id.icon_beet
    };

    final com.cs407.beet_boxing.util.EnumProduceType[] PRODUCE_ENUMS = {
            CARROT, BANANA, APPLE,
            POTATO, ONION, ORANGE,
            MELON, GINGER, BEET
    };

    private static final int[] PRODUCE_NUM_IDS = {
            R.id.number_carrot, R.id.number_banana, R.id.number_apple,
            R.id.number_potato, R.id.number_onion, R.id.number_orange,
            R.id.number_melon, R.id.number_ginger, R.id.number_beet
    };

    private static final int[] SOUND_RESOURCE_IDS = {
            R.raw.violin_trimmed, R.raw.guitar_trimmed, R.raw.piano_trimmed,
            R.raw.drumset2_trimmed, R.raw.meow_trimmed, R.raw.stabs_trimmed,
            R.raw.xylo_trimmed, R.raw.synth_trimmed, R.raw.drumset1_trimmed
    };


    private static final int[] COUNTDOWN_IDS = {
            R.id.countdown_carrot, R.id.countdown_banana, R.id.countdown_apple,
            R.id.countdown_potato, R.id.countdown_onion, R.id.countdown_orange,
            R.id.countdown_melon, R.id.countdown_ginger, R.id.countdown_beet
    };

    private ImageButton[] buttonProduces = new ImageButton[BUTTON_PRODUCE_IDS.length];
    private ImageButton[] produceIcons = new ImageButton[ICON_IDS.length];
    private TextView[] produceNums = new TextView[ICON_IDS.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden);

        setupUI();
        setupListeners();

        // set numbers next to produce icon indicating how many users have collected
        setProduceAmount();

        for (int id : new int[]{R.id.icon_carrot, R.id.icon_banana, R.id.icon_apple, R.id.icon_potato,
                R.id.icon_onion, R.id.icon_orange, R.id.icon_melon, R.id.icon_ginger, R.id.icon_beet}) {
            cooldownMap.put(id, false);
        }
    }

    private void setupUI() {
        editMenuLayout = findViewById(R.id.edit_menu_layout);

        // Initialize buttons and placeholders
        for (int i = 0; i < BUTTON_PRODUCE_IDS.length; i++) {
            buttonProduces[i] = findViewById(BUTTON_PRODUCE_IDS[i]);
            buttonProduces[i].setTag(true); // Tag as empty
        }

        // Initialize produce icons and numbers
        for (int i = 0; i < ICON_IDS.length; i++) {
            produceIcons[i] = editMenuLayout.findViewById(ICON_IDS[i]);
            produceIcons[i].setTag(ICON_IDS[i]); // Tag with its own ID

            produceNums[i] = editMenuLayout.findViewById(PRODUCE_NUM_IDS[i]);
        }

        // Initialize mediaPlayers HashMap
        mediaPlayers = new HashMap<>();
    }

    private void setupListeners() {
        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(e -> startActivity(new Intent(this, ActivityTiltGame.class)));
        ImageView settings = findViewById(R.id.settings);
        settings.setOnClickListener(e -> startActivity(new Intent(this, SettingsActivity.class)));

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

        for (int i = 0; i < produceIcons.length; i++) {
            GameData gameData = PersistentInfo.getGameData();
            assert gameData != null;
            produceIcons[i].setOnTouchListener(gameData.getInventory().
                    getOrDefault(PRODUCE_ENUMS[i], 0) >= 5 ? touchListener : null);
        }

        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View receivingLayoutView, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
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
                        break;
                    default:
                        break;
                }
                return true;
            }
        };


        for (ImageButton buttonProduce : buttonProduces) {
            buttonProduce.setOnDragListener(dragListener);
        }


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

    private TextView findCountdownTextViewForButton(int iconId) {

        int index = 0;
        for (int i = 0; i < produceIcons.length; i++) {
            if (produceIcons[i].getId() == iconId) {
                index = i;
            }
        }

        return findViewById(COUNTDOWN_IDS[index]);
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
        int index = 0;
        for (int i = 0; i < produceIcons.length; i++) {
            if (produceIcons[i].getId() == produceId) {
                index = i;
            }
        }
        GameData gameData = PersistentInfo.getGameData();
        assert gameData != null;

        int previousAmount = gameData.getInventory().getOrDefault(PRODUCE_ENUMS[index], 0);
        gameData.getInventory().put(PRODUCE_ENUMS[index], previousAmount - 5);
        if (gameData.getInventory().getOrDefault(PRODUCE_ENUMS[index], 0) < 5) {
            produceIcons[index].setOnTouchListener(null);
        }

    }

    private void setProduceAmount() {
        for (int i = 0; i < produceNums.length; i++) {
            GameData gameData = PersistentInfo.getGameData();
            assert gameData != null;
            produceNums[i].setText(gameData.getInventory().getOrDefault(PRODUCE_ENUMS[i], 0).toString());
        }
    }

    //Sage edited these to use the new mp3 file assets
    private int getSoundResourceIdForIcon(int iconId) {
        int index = 0;
        for (int i = 0; i < produceIcons.length; i++) {
            if (produceIcons[i].getId() == iconId) {
                index = i;
            }
        }

        return SOUND_RESOURCE_IDS[index];
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