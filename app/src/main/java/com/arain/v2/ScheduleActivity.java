package com.arain.v2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {
    //declare variables

    private Button switchPumpOn, switchPumpOff;
    private Button setTime;
    private EditText editTextDuration;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private boolean isPumpOn;
    private String selectedTime;
    private Long timeInMilliSeconds = 0L;
    private Intent serviceIntent;
    private PendingIntent alarmServiceIntent;
    private AlarmManager alarmManager;
    private Calendar calendar;
    private String waterConsumption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // Initialize the views
        switchPumpOn = findViewById(R.id.switchPumpOn);
        switchPumpOff = findViewById(R.id.switchPumpOff);
        setTime = findViewById(R.id.setTime);
        editTextDuration = findViewById(R.id.editTextDuration);

        // Initialize the Firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ScheduleInfo");
        serviceIntent = new Intent(ScheduleActivity.this, ScheduleService.class);
        calendar = Calendar.getInstance();

        // Set onClickListener for the start button
        switchPumpOn.setOnClickListener(v -> {
            isPumpOn = true;
            switchPumpOn.setEnabled(false);
            switchPumpOff.setEnabled(true);
            editTextDuration.setEnabled(false);
            setTime.setEnabled(false);
            Toast.makeText(ScheduleActivity.this, "Schedule Set", Toast.LENGTH_LONG).show();
            // Update label or perform any other necessary actions
            saveUserSelections();
        });

        // Set onClickListener for the cancel button
        switchPumpOff.setOnClickListener(v -> {
            isPumpOn = false;
            // Update label or perform any other necessary actions
            FirebaseDatabase.getInstance().getReference("ScheduleInfo").child("pumpOn").setValue(false);
            switchPumpOn.setEnabled(true);
            switchPumpOff.setEnabled(false);
            editTextDuration.setEnabled(true);
            Toast.makeText(ScheduleActivity.this, "Schedule Canceled", Toast.LENGTH_LONG).show();
            setTime.setEnabled(true);
            if (alarmManager != null) {
                alarmManager.cancel(alarmServiceIntent);
            }
        });


        // Load the user's selections from the database
        loadUserSelections();

        // Set an OnClickListener for the time button
        setTime.setOnClickListener(v -> showTimePickerDialog());
    }

    // Show a TimePickerDialog to allow the user to select a time
    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfDay) -> {
            // Format the selected time as a string and set it in the UI
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minuteOfDay);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
            selectedTime = sdf.format(calendar.getTime());
            setTime.setText(selectedTime);
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    // Load the user's selections from the database
    private void loadUserSelections() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Update the UI with the loaded selections
                    selectedTime = dataSnapshot.child("time").getValue(String.class);
                    setTime.setText(selectedTime);
                    editTextDuration.setText(dataSnapshot.child("duration").getValue(String.class));

                    // Set the state of the start button based on the pump status
                    boolean pumpOn = Boolean.TRUE.equals(dataSnapshot.child("pumpOn").getValue(Boolean.class));
                    switchPumpOn.setEnabled(!pumpOn);
                    switchPumpOff.setEnabled(pumpOn);

                    timeInMilliSeconds = dataSnapshot.child("timeInMilliSeconds").getValue(Long.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    // Save the user's selections to the database
    private void saveUserSelections() {

        ScheduleInfo scheduleInfo = new ScheduleInfo(
                selectedTime,
                editTextDuration.getText().toString(),
                isPumpOn,
                timeInMilliSeconds,
                waterConsumption
        );

        databaseReference.setValue(scheduleInfo).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            String durationString = editTextDuration.getText().toString();
                            int duration = Integer.parseInt(durationString);
                            int waterConsumption = (int) (duration * 33.33);


                            serviceIntent.putExtra("duration", editTextDuration.getText().toString());
                            serviceIntent.putExtra("humidity", snapshot.child("humidity").getValue().toString());
                            serviceIntent.putExtra("soilMoisture", snapshot.child("soilMoisture").getValue().toString());
                            serviceIntent.putExtra("temperature", snapshot.child("temperature").getValue().toString());
                            serviceIntent.putExtra("waterLevel", snapshot.child("waterLevel").getValue().toString());
                            serviceIntent.putExtra("waterConsumption", String.valueOf(waterConsumption));
                            serviceIntent.putExtra("time", selectedTime);
                            alarmServiceIntent = PendingIntent.getBroadcast(ScheduleActivity.this, 0, serviceIntent, PendingIntent.FLAG_IMMUTABLE);
                            timeInMilliSeconds = calendar.getTimeInMillis();
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliSeconds, alarmServiceIntent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Do Nothing
                    }
                });
            }
        });
    }
}