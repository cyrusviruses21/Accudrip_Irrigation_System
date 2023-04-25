package com.arain.v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ProfileActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        DatabaseReference scheduleInfoReference = FirebaseDatabase.getInstance().getReference("ScheduleInfo");
        DatabaseReference reportsReference = FirebaseDatabase.getInstance().getReference("Reports");

        final TextView nameDisplayTextView = (TextView) findViewById(R.id.nameDisplay);
        final TextView emailDisplayTextView = (TextView) findViewById(R.id.emailDisplay);
        final TextInputEditText fullNameTextView = (TextInputEditText) findViewById(R.id.fullName);
        final TextInputEditText emailTextView = (TextInputEditText) findViewById(R.id.email);
        final TextInputEditText phoneNumberTextView = (TextInputEditText) findViewById(R.id.phoneNumber);
        final TextView timeTextView = (TextView) findViewById(R.id.time_label);
        final TextView totalTextView = (TextView) findViewById(R.id.total_label);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userProfile = snapshot.getValue(UserInfo.class);

                if(userProfile != null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String phoneNumber = userProfile.phoneNumber;

                    emailDisplayTextView.setText(email);
                    nameDisplayTextView.setText(fullName);
                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                    phoneNumberTextView.setText(phoneNumber);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity2.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });

        scheduleInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ScheduleInfo scheduleInfo = snapshot.getValue(ScheduleInfo.class);

                if (scheduleInfo != null) {
                    String time = scheduleInfo.time;

                    timeTextView.setText(time);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity2.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });

        reportsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Object waterConsumption = map.get("waterConsumption");
                    int pValue = Integer.parseInt(String.valueOf(waterConsumption));
                    sum += pValue;

                    totalTextView.setText(String.valueOf(sum));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void finishActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
