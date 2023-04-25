package com.arain.v2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

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

        TextView totalTextView = (TextView) findViewById(R.id.total_water);

        recyclerView = findViewById(R.id.waterConsumptionReports);
        databaseReference = FirebaseDatabase.getInstance().getReference("Reports");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        waterConsumptionAdapter = new WaterConsumptionAdapter(this, list);
        recyclerView.setAdapter(waterConsumptionAdapter);

        //Getting the total water consumption
        databaseReference.addValueEventListener(new ValueEventListener() {
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

        databaseReference.orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<WaterConsumptionReports> tempList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    WaterConsumptionReports waterConsumptionReports = dataSnapshot.getValue(WaterConsumptionReports.class);
                    tempList.add(0, waterConsumptionReports); // add at the beginning of the list for descending order
                }
                list.clear();
                list.addAll(tempList);
                waterConsumptionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
