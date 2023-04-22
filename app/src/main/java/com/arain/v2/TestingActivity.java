package com.arain.v2;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TestingActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference pumpStatusRef;
    private DatabaseReference schedulesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holy);

        // Get a reference to the Firebase Realtime Database.
        database = FirebaseDatabase.getInstance();

        // Get a reference to the "pumpStatus" variable in Firebase.
        pumpStatusRef = database.getReference("pumpStatus");

        // Get a reference to the "schedules" reference in Firebase.
        schedulesRef = database.getReference("schedules");

        // Set up a listener to listen for changes to the "schedules" reference in Firebase.
        schedulesRef.addValueEventListener(schedulesListener);

        // Set up a button to add a new schedule.
        findViewById(R.id.addScheduleButton).setOnClickListener(v -> addSchedule());
    }

    private void addSchedule() {
        // Get the user's desired date, time, and duration.
        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);
        EditText durationEditText = findViewById(R.id.durationEditText);

        // Create a Calendar object with the user's desired date and time.
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());

        // Convert the Calendar object to a timestamp in milliseconds.
        long timestamp = calendar.getTimeInMillis();

        // Get the user's desired duration in seconds.
        int duration = Integer.parseInt(durationEditText.getText().toString());

        // Create a ScheduleInfo object with the user's input.
        Schedule schedule = new Schedule();
        schedule.timestamp = timestamp;
        schedule.duration = duration;

        // Get a reference to a new child node in the "schedules" reference with the date as the key ID.
        String key = String.valueOf(calendar.getTimeInMillis());
        DatabaseReference newScheduleRef = schedulesRef.child(key);

        // Save the schedule to Firebase.
        newScheduleRef.setValue(schedule, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(TestingActivity.this, "Failed to add schedule: " + error.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TestingActivity.this, "ScheduleInfo added successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private ValueEventListener schedulesListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
            // Iterate through all schedules in the "schedules" reference.
            for (com.google.firebase.database.DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                // Get the ScheduleInfo object from the snapshot.
                Schedule schedule = scheduleSnapshot.getValue(Schedule.class);

                // Get the current time in milliseconds.
                long now = System.currentTimeMillis();

                // If the schedule's timestamp is in the future and its duration is greater than 0...
                if (schedule.timestamp > now && schedule.duration > 0) {
                    // Calculate the difference between the schedule's timestamp and the current time.
                    long diff = schedule.timestamp - now;

                    // ScheduleInfo a TimerTask to turn the water pump on after the delay.
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // Set the "pumpStatus" variable to 1 in Firebase to turn the pump on.
                            pumpStatusRef.setValue(1);

                            // ScheduleInfo another TimerTask to turn the water pump off after the duration.
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    // Set the "pumpStatus" variable to 0 in Firebase to turn the pump off.
                                    pumpStatusRef.setValue(0);

                                    // Set the duration of the schedule to 0 in Firebase to indicate that it has completed.
                                    schedule.duration = 0;
                                    scheduleSnapshot.getRef().setValue(schedule);
                                }
                            }, schedule.duration * 1000);
                        }
                    }, diff);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(TestingActivity.this, "Failed to read schedules: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private static class Schedule {
        public long timestamp;
        public int duration;
    }
}
