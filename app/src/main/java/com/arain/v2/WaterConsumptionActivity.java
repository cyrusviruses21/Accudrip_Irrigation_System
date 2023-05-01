package com.arain.v2;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class WaterConsumptionActivity extends AppCompatActivity {

    //variables
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    WaterConsumptionAdapter waterConsumptionAdapter;
    ArrayList<WaterConsumptionReports> list;
    LineChart lineChart;
    TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_consumption_report);

        totalTextView = findViewById(R.id.total_water);
        lineChart = findViewById(R.id.line_chart);
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    WaterConsumptionReports waterConsumptionReports = dataSnapshot.getValue(WaterConsumptionReports.class);
                    tempList.add(0, waterConsumptionReports); // add at the beginning of the list for descending order
                }
                list.clear();
                list.addAll(tempList);
                waterConsumptionAdapter.notifyDataSetChanged();

                // Update line chart data
                updateLineChart(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateLineChart(ArrayList<WaterConsumptionReports> tempList) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < tempList.size(); i++) {
            WaterConsumptionReports report = tempList.get(i);
            String waterConsumption = report.getWaterConsumption(); // get the water consumption value as String
            int consumption = Integer.parseInt(waterConsumption); // convert the String to int

            String date = report.getDate();
            entries.add(new Entry(i, consumption));
            labels.add(getFormattedDate(date)); // format the date and add to labels list
        }
        // create line data set
        LineDataSet lineDataSet = new LineDataSet(entries, "Water Consumption");
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setCircleHoleRadius(2.5f);
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setDrawValues(false);

        // create line data
        LineData lineData = new LineData(lineDataSet);
        lineData.setDrawValues(false);
        lineData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int) value;
                if (index >= 0 && index < labels.size()) {
                    return labels.get(index);
                }
                return "";
            }
        });
        // set line chart data
        lineChart.setData(lineData);
        lineChart.animateX(1000);
        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // set x-axis
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value >= 0 && value < labels.size()) {
                    return labels.get((int) value);
                }
                return "";
            }
        });

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45f);

        // set y-axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        // set legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        // refresh chart
        lineChart.invalidate();
    }

    private String getFormattedDate(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = format.parse(dateStr);
            format.applyPattern("MMM d");
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }
}