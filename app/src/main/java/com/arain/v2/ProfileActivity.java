package com.arain.v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity
{

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private Button logout;
    private ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = (ImageButton) findViewById(R.id.back);
        logout = (Button) findViewById(R.id.logout);

        back.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, MainActivity.class)));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(this, LoginActivity.class);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView nameDisplayTextView = (TextView) findViewById(R.id.nameDisplay);
        final TextView fullNameTextView = (TextView) findViewById(R.id.fullName);
        final TextView emailTextView = (TextView) findViewById(R.id.email);
        final TextView phoneNumberTextView = (TextView) findViewById(R.id.phoneNumber);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userProfile = snapshot.getValue(UserInfo.class);

                if(userProfile != null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String phoneNumber = userProfile.phoneNumber;

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
    }

    private void finishActivity(View.OnClickListener onClickListener, Class<LoginActivity> mainActivityClass) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
    }
}