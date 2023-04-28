package com.arain.v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ProfileActivity extends AppCompatActivity {

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

        Button updateBtn = (Button) findViewById(R.id.updateBtn);

        //Getting the User's information
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userProfile = snapshot.getValue(UserInfo.class);

                if (userProfile != null) {
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
                Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });

        //Getting the set schedule
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
                Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });

        //Getting the total water consumption
        reportsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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

        //Update user information
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameTextView.getText().toString();
                String email = emailTextView.getText().toString();
                String phoneNumber = phoneNumberTextView.getText().toString();

                userReference.child("fullName").setValue(fullName);
                userReference.child("email").setValue(email);
                userReference.child("phoneNumber").setValue(phoneNumber);

                Toast.makeText(ProfileActivity.this,"User Information Updated!",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void finishActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
