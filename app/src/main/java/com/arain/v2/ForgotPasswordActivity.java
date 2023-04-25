package com.arain.v2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        TextView banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        emailEditText = (EditText) findViewById(R.id.email);
        Button resetPasswordButton = (Button) findViewById(R.id.resetPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button backButton = (Button) findViewById(R.id.back);
        auth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(v -> startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class)));

        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.banner) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        });

    }
}