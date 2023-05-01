package com.arain.v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;

    ArrayList<ScheduleReports> list;

    public Adapter(Context context, ArrayList<ScheduleReports> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ScheduleReports scheduleReports = list.get(position);
        holder.schedDate.setText(scheduleReports.getDate());
        holder.schedDuration.setText(scheduleReports.getDuration());
        holder.humidityStat.setText(scheduleReports.getHumidity());
//        holder.soilMoistureStat.setText(scheduleReports.getSoilMoisture());
        holder.tempStat.setText(scheduleReports.getTemperature());
        holder.schedTime.setText(scheduleReports.getTime());
//        holder.waterLevelStat.setText(scheduleReports.getWaterLevel());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView schedDate, schedDuration, humidityStat, soilMoistureStat, tempStat, schedTime, waterLevelStat;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            schedDate = itemView.findViewById(R.id.schedDate);
            schedDuration = itemView.findViewById(R.id.schedDuration);
            humidityStat = itemView.findViewById(R.id.humidityStat);
//            soilMoistureStat = itemView.findViewById(R.id.soilMoistureStat);
            tempStat = itemView.findViewById(R.id.tempStat);
            schedTime = itemView.findViewById(R.id.schedTime);
//            waterLevelStat = itemView.findViewById(R.id.waterLevelStat);

        }
    }

}