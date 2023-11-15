package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

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

    private ImageButton produceIcon1;
    private ImageButton produceIcon2;
    private ImageButton produceIcon3;
    private ImageButton produceIcon4;
    private ImageButton produceIcon5;
    private ImageButton produceIcon6;
    private ImageButton produceIcon7;
    private ImageButton produceIcon8;
    private ImageButton produceIcon9;
    private HashMap<Integer, MediaPlayer> mediaPlayers;
    private View editMenuLayout;
    private Button closeEditMenuButton;
    private boolean isEditOpen = false;
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
                }
                else {
                    editMenuLayout.setVisibility(View.GONE); // Hide the edit menu
                    isEditOpen = false;
                }
            }
        });

        // Initialize buttons
        buttonProduce1 = findViewById(R.id.button_produce1);
        buttonProduce2 = findViewById(R.id.button_produce2);
        buttonProduce3 = findViewById(R.id.button_produce3);
        buttonProduce4 = findViewById(R.id.button_produce4);
        buttonProduce5 = findViewById(R.id.button_produce5);
        buttonProduce6 = findViewById(R.id.button_produce6);
        buttonProduce7 = findViewById(R.id.button_produce7);
        buttonProduce8 = findViewById(R.id.button_produce8);
        buttonProduce9 = findViewById(R.id.button_produce9);

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


        produceIcon1 = editMenuLayout.findViewById(R.id.produce_icon_1);
        produceIcon2 = editMenuLayout.findViewById(R.id.produce_icon_2);
        produceIcon3 = editMenuLayout.findViewById(R.id.produce_icon_3);
        produceIcon4 = editMenuLayout.findViewById(R.id.produce_icon_4);
        produceIcon5 = editMenuLayout.findViewById(R.id.produce_icon_5);
        produceIcon6 = editMenuLayout.findViewById(R.id.produce_icon_6);
        produceIcon7 = editMenuLayout.findViewById(R.id.produce_icon_7);
        produceIcon8 = editMenuLayout.findViewById(R.id.produce_icon_8);
        produceIcon9 = editMenuLayout.findViewById(R.id.produce_icon_9);

        // Create the OnTouchListener
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(clipData, shadowBuilder, view, 0);
//                    view.setVisibility(View.INVISIBLE); // Optional: make the view invisible during the drag operation
                    return true;
                }
                return false;
            }
        };

        // Set the touch listener to all your produce ImageButtons
        produceIcon1.setOnTouchListener(touchListener);
        produceIcon2.setOnTouchListener(touchListener);
        produceIcon3.setOnTouchListener(touchListener);
        produceIcon4.setOnTouchListener(touchListener);
        produceIcon5.setOnTouchListener(touchListener);
        produceIcon6.setOnTouchListener(touchListener);
        produceIcon7.setOnTouchListener(touchListener);
        produceIcon8.setOnTouchListener(touchListener);
        produceIcon9.setOnTouchListener(touchListener);

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
                        if (isEmpty == null || isEmpty) {
                            // Set the drawable on the garden placeholder ImageButton
                            droppedOn.setImageDrawable(draggedIconDrawable);
                            droppedOn.setTag(false); // Set the tag to false, indicating it's no longer empty

                            // Set click listeners
                            droppedOn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Now, since the placeholder was empty and we just dropped an item, we can play the sound associated with the item
                                    // Assuming the ID of the draggedView correlates to a specific sound
                                    toggleSound(getSoundResourceIdForIcon(draggedView.getId()), droppedOn.getId());
                                }
                            });


                        }
                        // Optional: make the original produce icon invisible or remove it
                        draggedView.setVisibility(View.INVISIBLE);
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

//        // Set click listeners
//        buttonProduce1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleSound(R.raw.drum_loop, buttonProduce1.getId());
//            }
//        });
//
//        buttonProduce2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleSound(R.raw.electric_guitar, buttonProduce2.getId());
//            }
//        });
//
//        buttonProduce3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleSound(R.raw.drum_loop, buttonProduce3.getId());
//            }
//        });

    }


    private int getSoundResourceIdForIcon(int iconId) {
        if (iconId == R.id.produce_icon_1) {
            return R.raw.drum_loop;
        } else if (iconId == R.id.produce_icon_2) {
            return R.raw.electric_guitar;
        } else if (iconId == R.id.produce_icon_3) {
            return R.raw.banjo;
        } else if (iconId == R.id.produce_icon_4) {
            return R.raw.beatbox;
        } else if (iconId == R.id.produce_icon_5) {
            return R.raw.bells;
        } else if (iconId == R.id.produce_icon_6) {
            return R.raw.blend_beats;
        } else if (iconId == R.id.produce_icon_7) {
            return R.raw.piano;
        } else if (iconId == R.id.produce_icon_8) {
            return R.raw.vocal;
        } else if (iconId == R.id.produce_icon_9) {
            return R.raw.woodwind;
        }

        else {
            return -1; // Invalid ID or default sound
        }
    }

    private void toggleSound(int soundResourceId, int buttonId) {
        // Check if the button already has a MediaPlayer associated with it
        if (mediaPlayers.containsKey(buttonId)) {
            MediaPlayer player = mediaPlayers.get(buttonId);
            if (player.isPlaying()) {
                player.pause(); // Pause playback
            } else {
                player.start(); // Resume playback
            }
        } else {
            // No MediaPlayer for this button yet, create and start it
            MediaPlayer player = MediaPlayer.create(this, soundResourceId);
            player.setLooping(true);
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