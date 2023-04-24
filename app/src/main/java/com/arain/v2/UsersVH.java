package com.arain.v2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersVH extends RecyclerView.ViewHolder
{
    public TextView txt_name, txt_position, txt_option, txt_phoneNumber;


    public UsersVH(@NonNull View itemView)
    {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_position = itemView.findViewById(R.id.txt_position);
        txt_phoneNumber = itemView.findViewById(R.id.txt_phoneNumber);
        txt_option = itemView.findViewById(R.id.txt_option);
    }
}
