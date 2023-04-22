package com.arain.v2;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScheduleService extends BroadcastReceiver {

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context != null && intent != null) {
            Toast.makeText(context, "Irrigation Started", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            ReportInfo reportInfo = new ReportInfo(
                    sdf.format(calendar.getTime()),
                    intent.getStringExtra("time"),
                    intent.getStringExtra("duration"),
                    intent.getStringExtra("humidity"),
                    intent.getStringExtra("soilMoisture"),
                    intent.getStringExtra("temperature"),
                    intent.getStringExtra("waterLevel")
            );

            FirebaseDatabase.getInstance().getReference("Reports")
                    .push()
                    .setValue(reportInfo);

            FirebaseDatabase.getInstance().getReference().child("pumpStatus").setValue(1);

            startTimer(Long.parseLong(intent.getStringExtra("duration")));
        }
    }

    private CountDownTimer countDownTimer;

    private void startTimer(long durationSeconds) {
        cancelTimer(); // Cancel any existing timer before starting a new one
        countDownTimer = new CountDownTimer(durationSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Do nothing during the timer tick
            }
            @Override
            public void onFinish() {
                // Turn off the switch when the timer finishes
                cancelTimer();
                FirebaseDatabase.getInstance().getReference().child("pumpStatus").setValue(0);
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
}
