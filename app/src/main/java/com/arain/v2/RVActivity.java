package com.arain.v2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RVActivity extends AppCompatActivity
{
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter adapter;
    DOAUsers doaUsers;
    boolean isLoading = false;
    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvactivity);
        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RVAdapter(this);
        recyclerView.setAdapter(adapter);
        doaUsers = new DOAUsers();
        loadData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
                {
                    if(!isLoading)
                    {
                        isLoading = true;
                        loadData();
                    }
                }
            }
        });
    }

    private void loadData()
    {
        swipeRefreshLayout.setRefreshing(true);
        doaUsers.get(key).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ArrayList<Users> use = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    Users users = data.getValue(Users.class);
                    users.setKey(data.getKey());
                    use.add(users);
                    key = data.getKey();
                }
                adapter.setItems(use);
                adapter.notifyDataSetChanged();
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}