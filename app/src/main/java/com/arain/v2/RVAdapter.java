package com.arain.v2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    ArrayList<Users> list = new ArrayList<>();

    public RVAdapter(Context ctx)
    {
        this.context = ctx;
    }
    public void setItems(ArrayList<Users> users)
    {
        list.addAll(users);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.adminitem,parent,false);
        return new UsersVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Users u = null;
        this.onBindViewHolder(holder,position,u);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, Users u)
    {
        UsersVH vh = (UsersVH) holder;
        Users users = u==null? list.get(position):u;
        vh.txt_name.setText(users.getFullName());
        vh.txt_position.setText(users.getEmail());
        vh.txt_phoneNumber.setText(users.getPhoneNumber());
        vh.txt_option.setOnClickListener(v->
        {
            PopupMenu popupMenu = new PopupMenu(context,vh.txt_option);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item ->
            {
                switch (item.getItemId())
                {
                    case R.id.menu_edit:
                        Intent intent=new Intent(context, AdminActivity.class);
                        intent.putExtra("EDIT",users);
                        context.startActivity(intent);
                        break;
                    case R.id.menu_remove:
                        DOAUsers doaUsers = new DOAUsers();
                        doaUsers.remove(users.getKey()).addOnSuccessListener(suc->
                        {
                            Toast.makeText(context, "Record is removed", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                            list.remove(users);
                        }).addOnFailureListener(er->
                        {
                            Toast.makeText(context, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        break;
                }
                return false;
            });
            popupMenu.show();
        });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
