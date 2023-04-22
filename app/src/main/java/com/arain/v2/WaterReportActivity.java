package com.arain.v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class WaterReportActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    //variables
    private TextView textView3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_report);

        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setOnClickListener(this);

        //hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //toolbar
        setSupportActionBar(toolbar);


        //Navigation Drawer Menu
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(true);
        menu.findItem(R.id.nav_profile).setVisible(true);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_data);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView3:
                startActivity(new Intent(this, MainActivity.class));
                break;


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
        switch (menuitem.getItemId()){
            case R.id.nav_data:
                break;
            case R.id.nav_status:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_report:
                Intent intent1 = new Intent(this, ReportsActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_profile:
                Intent intent2 = new Intent(this, ProfileActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                startActivity(intent3);
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
