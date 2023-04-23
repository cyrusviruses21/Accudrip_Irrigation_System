package com.arain.v2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WaterConsumptionAdapter extends RecyclerView.Adapter<WaterConsumptionAdapter.WaterViewHolder> {


    Context context;
    ArrayList<WaterConsumptionReports> list;

    public WaterConsumptionAdapter(Context context, ArrayList<WaterConsumptionReports> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.waterconsumptionitem, parent, false);
        return new WaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterViewHolder holder, int position) {

        WaterConsumptionReports waterConsumptionReports = list.get(position);
        holder.schedDate.setText(waterConsumptionReports.getDate());
        holder.waterConsumption.setText(waterConsumptionReports.getWaterConsumption());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class WaterViewHolder extends RecyclerView.ViewHolder{

        TextView waterConsumption, schedDate;

        public WaterViewHolder(@NonNull View itemView) {
            super(itemView);

            waterConsumption = itemView.findViewById(R.id.waterConsumption);
            schedDate = itemView.findViewById(R.id.schedDate);
        }
    }


}
