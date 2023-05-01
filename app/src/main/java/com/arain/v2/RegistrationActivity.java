package com.arain.v2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, alreadyHaveAnAccount;
    private EditText editTextFullname, editTextPhoneNumber, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private Button registerUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    private boolean valid = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        alreadyHaveAnAccount = (TextView) findViewById(R.id.alreadyHaveAnAccount);
        alreadyHaveAnAccount.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullname = (EditText) findViewById(R.id.fullName);
        editTextFullname.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetter(source.charAt(i)) && source.charAt(i) != ' ') {
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Validate input using regular expression
                String input = s.toString().trim();
                String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*$"; // At least one character and no special characters exempt @ and .
                if (!input.matches(regex)) {
                    editTextEmail.setError("Invalid email address");
                } else {
                    editTextEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.banner:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.registerUser:
                registerUser();
                break;

            case R.id.alreadyHaveAnAccount:
                startActivity(new Intent(this, LoginActivity.class));
                break;

        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullname.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String role = "user";

        if (fullName.isEmpty()) {
            editTextFullname.setError("Full name is required!");
            editTextFullname.requestFocus();
            return;
        }
        if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Phone Number is required");
            editTextPhoneNumber.requestFocus();
            return;
        }
        if (phoneNumber.length() != 11) {
            editTextPhoneNumber.setError("Valid Phone Number is required");
            editTextPhoneNumber.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("fullName", fullName);
                        userMap.put("phoneNumber", phoneNumber);
                        userMap.put("email", email);
                        userMap.put("User", role);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(userMap).addOnCompleteListener(task1 -> {

                                    if (task1.isSuccessful()) {
                                        Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(this, LoginActivity.class));

                                        //redirect to Login layout
                                        registerUser.setOnClickListener(v -> startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)));

                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Failed to register, Try Again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });

                    } else {
                        Toast.makeText(RegistrationActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }
}