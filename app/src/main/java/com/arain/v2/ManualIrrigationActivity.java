package com.arain.v2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManualIrrigationActivity extends AppCompatActivity implements View.OnClickListener {

    //variables
    private DatabaseReference databaseReference;
    private Switch switchPump;
    private Button home;
    private ImageButton back;
    private EditText editTextDuration;
    private Button buttonStart;
    private CountDownTimer countDownTimer;
    private TextView waterLevel;
    String status;
    private long durationSeconds; // Duration in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_irrigation);

        switchPump = findViewById(R.id.switchPump);
        editTextDuration = findViewById(R.id.editTextDuration);
        buttonStart = findViewById(R.id.buttonStart);
        waterLevel = (TextView) findViewById(R.id.waterLevel);


        back = (ImageButton) findViewById(R.id.back);
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);

        buttonStart.setOnClickListener(v -> {
            String durationString = editTextDuration.getText().toString();
            if (!TextUtils.isEmpty(durationString)) {
                // Get the duration in seconds from user input
                int duration = Integer.parseInt(durationString);
                durationSeconds = duration; // Assign duration to durationSeconds
                // Convert to milliseconds
                durationSeconds *= 1000;
                // Turn on the switch
                switchPump.setChecked(true);
                // Start the timer
                startTimer();
            } else {
                // Show an error message if duration is not provided
                Toast.makeText(ManualIrrigationActivity.this, "Enter a duration", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(v -> {
            startActivity(new Intent(ManualIrrigationActivity.this, MainActivity.class));
            finish();
        });

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status = snapshot.child("waterLevel").getValue().toString();
                //waterLevel.setText(status);
                switch (status) {
                    case "0":
                        waterLevel.setText("LOW");
                        switchPump.setChecked(false);
                        databaseReference.child("pumpStatus").setValue(0);
                        switchPump.setText("OFF");
                        break;
                    default:
                        waterLevel.setText("HIGH");
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Get reference to the Switch widget
        switchPump = findViewById(R.id.switchPump);


        // Set a listener for the switch state change
        switchPump.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (status.equals("0")) { // Check if water level is LOW
                switchPump.setChecked(false); // Set switch to unchecked
                Toast.makeText(ManualIrrigationActivity.this, "Water level is LOW. Cannot turn on the pump.", Toast.LENGTH_SHORT).show();
            } else { // Water level is not LOW
                if (isChecked) {
                    switchPump.setText("ON"); // Set label to "On" when checked
                } else {
                    switchPump.setText("OFF"); // Set label to "Off" when unchecked
                    cancelTimer();
                }
                // Update the Firebase Realtime Database with the new pump status
                databaseReference.child("pumpStatus").setValue(isChecked ? 1 : 0);
            }
        });


    }

    private void startTimer() {
        cancelTimer(); // Cancel any existing timer before starting a new one
        countDownTimer = new CountDownTimer(durationSeconds, durationSeconds) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Do nothing during the timer tick
            }

            @Override
            public void onFinish() {
                // Turn off the switch when the timer finishes
                switchPump.setChecked(false);
            }
        };
        countDownTimer.start();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}