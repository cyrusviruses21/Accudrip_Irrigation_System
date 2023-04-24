package com.arain.v2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity2 extends AppCompatActivity {

    TextInputLayout fullName, email, phoneNo, password;
    TextView fullNameLabel, emailLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
    }

//        fullName = findViewById(R.id.full_name_profile);
//        fullName = findViewById(R.id.email_profile);
//        fullName = findViewById(R.id.phoneNumber_profile);
//        fullName = findViewById(R.id.password_profile);
//        fullNameLabel = findViewById(R.id.fullname_field);
//        emailLabel = findViewById(R.id.email_field);
//
//        //ShowAllData
//        showAllUserData();
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
//        String userID = user.getUid();
//
//    }
//
//    private void showAllUserData() {
//
//        Intent intent = getIntent();
//        String user_fullName = intent.getStringExtra("fullName");
//        String user_email = intent.getStringExtra("email");
//        String user_phoneNumber = intent.getStringExtra("phoneNumber");
//
//        fullNameLabel.setText(user_fullName);
//        emailLabel.setText(user_fullName);
//        fullName.getEditText().setText(user_fullName);
//        email.getEditText().setText(user_email);
//        phoneNo.getEditText().setText(user_phoneNumber);
//
//    }

}