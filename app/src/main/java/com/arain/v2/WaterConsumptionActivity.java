package com.arain.v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WaterConsumptionActivity extends AppCompatActivity {

    //variables
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    WaterConsumptionAdapter waterConsumptionAdapter;
    ArrayList<WaterConsumptionReports> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_consumption_report);

        recyclerView = findViewById(R.id.waterConsumptionReports);
        databaseReference = FirebaseDatabase.getInstance().getReference("Reports");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        waterConsumptionAdapter = new WaterConsumptionAdapter(this, list);
        recyclerView.setAdapter(waterConsumptionAdapter);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    WaterConsumptionReports waterConsumptionReports = dataSnapshot.getValue(WaterConsumptionReports.class);
                    list.add(waterConsumptionReports);

                }
                waterConsumptionAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
