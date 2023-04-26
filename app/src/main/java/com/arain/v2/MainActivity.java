package com.arain.v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView humidity, waterLevel, soilMoisture, temperature, manualIrrigate;
    DatabaseReference dref;
    String status;
    int soilMoistureThreshold = 100;
    int soilMoistureValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setOnClickListener(this);
        ImageView manualIrrigateIcon = (ImageView) findViewById(R.id.manualIrrigateIcon);
        manualIrrigateIcon.setOnClickListener(this);
        manualIrrigate = (TextView) findViewById(R.id.manualIrrigate);
        manualIrrigate.setOnClickListener(this);
        humidity = (TextView ) findViewById(R.id.humidityData);
        waterLevel = (TextView) findViewById(R.id.waterLevelData);
        soilMoisture = (TextView) findViewById(R.id.soilMoistureData);
        temperature = (TextView) findViewById(R.id.temperatureData);

        //hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //ToolBar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(true);
        menu.findItem(R.id.nav_profile).setVisible(true);

        //fetch sensor realtime data from the firebase
        dref = FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("humidity").getValue().toString();
                humidity.setText(status);

                status = dataSnapshot.child("soilMoisture").getValue().toString();
//                soilMoisture.setText(status);
                soilMoistureValue = Integer.parseInt(status);
                if (soilMoistureValue < soilMoistureThreshold) {
                    soilMoisture.setText("DRY");
                } else {
                    soilMoisture.setText("WET");
                }

                //soilMoisture.setText(status);

                status = dataSnapshot.child("waterLevel").getValue().toString();
                //waterLevel.setText(status);
                switch (status) {
                    case "1":
                        waterLevel.setText("LOW");
                        break;
                    default:
                        waterLevel.setText("HIGH");
                        break;
                }

                status = dataSnapshot.child("temperature").getValue().toString();
                temperature.setText(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_status);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView3:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.manualIrrigateIcon:
            case R.id.manualIrrigate:
                startActivity(new Intent(this, ManualIrrigationActivity.class));
                break;

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_data:
                Intent intent = new Intent(this, WaterConsumptionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_report:
                Intent intent1 = new Intent(this, ReportsActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_profile:
                Intent intent2 = new Intent(this, ProfileActivity2.class);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                finish();
                break;
            case R.id.nav_schedules:
                Intent intent4 = new Intent(this, ScheduleActivity.class);
                startActivity(intent4);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}