package com.arain.v2;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScheduleService extends BroadcastReceiver {

    private static final String CHANNEL_ID = "com.arain.v2.channelId";
    private static final int NOTIFICATION_ID = 1;

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context != null && intent != null) {

            int soilMoisture = Integer.parseInt(intent.getStringExtra("soilMoisture"));
            int threshold = 100; // Change this value to set the threshold

            if (soilMoisture > threshold || "0".equals(intent.getStringExtra("waterLevel"))) {
                // Cancel the existing timer and show a notification
                cancelTimer();
                createSoilMoistureNotification(context);
                return;
            }
            // Create and show a notification when the timer starts
            createNotification(context);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

            ReportInfo reportInfo = new ReportInfo(
                    sdf.format(calendar.getTime()),
                    intent.getStringExtra("time"),
                    intent.getStringExtra("duration"),
                    intent.getStringExtra("humidity"),
                    intent.getStringExtra("soilMoisture"),
                    intent.getStringExtra("temperature"),
                    intent.getStringExtra("waterLevel"),
                    intent.getStringExtra("waterConsumption")
            );

            FirebaseDatabase.getInstance().getReference("Reports")
                    .push()
                    .setValue(reportInfo);

            FirebaseDatabase.getInstance().getReference().child("pumpStatus").setValue(1);

            startTimer(Long.parseLong(intent.getStringExtra("duration")));

            Toast.makeText(context, "Irrigation started", Toast.LENGTH_LONG).show();

            createNotification(context);
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

    private void createNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the notification channel (for Android 8.0 or higher) this is required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Create the intent for when the notification is clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.time)
                .setContentTitle("Irrigation Started")
                .setContentText("The irrigation has started.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Show the notification
        notificationManager.notify(0, builder.build());
    }
    private void createSoilMoistureNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the notification channel (for Android 8.0 or higher) this is required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Accudrip",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Create the intent for when the notification is clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.warning)
                .setContentTitle("Irrigation Cancelled")
                .setContentText("Check Water Level and Soil Moisture")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
//    private void createWaterLevelNotification(Context context) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Accudrip",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        // Create the intent for when the notification is clicked
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        // Create the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.warning)
//                .setContentTitle("Irrigation Cancelled")
//                .setContentText("The Water Level is LOW")
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        // Show the notification
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }

}
