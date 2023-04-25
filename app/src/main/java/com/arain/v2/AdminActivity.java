package com.arain.v2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        final EditText edit_name = findViewById(R.id.edit_name);
        final EditText edit_email = findViewById(R.id.edit_email);
        final EditText edit_phoneNumber = findViewById(R.id.edit_phoneNumber);
        Button btn = findViewById(R.id.btn_submit);
        Button btn_open = findViewById(R.id.btn_open);
        btn_open.setOnClickListener(v ->
        {
            Intent intent = new Intent(AdminActivity.this, RVActivity.class);
            startActivity(intent);
        });

        DOAUsers doaUsers = new DOAUsers();
        Users users_edit = (Users) getIntent().getSerializableExtra("EDIT");
        if (users_edit != null) {
            btn.setText("UPDATE");
            edit_name.setText(users_edit.getFullName());
            edit_email.setText(users_edit.getEmail());
            edit_phoneNumber.setText(users_edit.getPhoneNumber());
            btn_open.setVisibility(View.GONE);
        } else {
            btn.setText("SUBMIT");
            btn_open.setVisibility(View.VISIBLE);
        }
        btn.setOnClickListener(v ->
        {
            Users users = new Users(edit_name.getText().toString(), edit_email.getText().toString(), edit_phoneNumber.getText().toString());
            if (users_edit == null) {
                doaUsers.add(users).addOnSuccessListener(suc ->
                        Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT).show()).addOnFailureListener(er ->
                        Toast.makeText(this, " " + er.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("fullName", edit_name.getText().toString());
                hashMap.put("email", edit_email.getText().toString());
                hashMap.put("phoneNumber", edit_phoneNumber.getText().toString());
                doaUsers.update(users_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                {
                    Toast.makeText(this, "Record is updated", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(er ->
                        Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show());

            }
        });
    }
}