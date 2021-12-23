package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class HomeActivity extends AppCompatActivity {
    private Button adder;
    private RecyclerView externalRecyclerView, internalRecyclerView;
    private RecyclerView.LayoutManager exLayoutManager, inLayoutManager;
    private ExAdapter exAdapter;
    //private InAdapter inAdapter;
    private LinkedList<HashMap<String,String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        adder = findViewById(R.id.adder);
        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddOrEditActivity.class);
                intent.putExtra("mode", false);
                startActivity(intent);
            }
        });
/*
        internalRecyclerView = findViewById(R.id.internalRecyclerView);
        internalRecyclerView.setHasFixedSize(true);
        inLayoutManager = new LinearLayoutManager(this);
        internalRecyclerView.setLayoutManager(inLayoutManager);
*/
        externalRecyclerView = findViewById(R.id.externalRecyclerView);
        externalRecyclerView.setHasFixedSize(true);
        exLayoutManager = new LinearLayoutManager(this);
        externalRecyclerView.setLayoutManager(exLayoutManager);

        makeData();
/*
        inAdapter = new InAdapter();
        internalRecyclerView.setAdapter(inAdapter);
*/
        exAdapter = new ExAdapter();
        externalRecyclerView.setAdapter(exAdapter);


    }

    private void makeData(){
        data = new LinkedList<>();
        for(int i=1; i<20; i++){
            HashMap<String, String> row = new HashMap<>();
            row.put("category", "12/" + i / 2);
            row.put("description", "i=" + i);
            row.put("money", "123" + i);
            data.add(row);
        }
    }



    private class ExAdapter extends RecyclerView.Adapter<ExAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public  TextView category, description, money;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
                description = itemView.findViewById(R.id.description);
                money = itemView.findViewById(R.id.money);
            }
        }

        @NonNull
        @Override
        public ExAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.single_record, parent,false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExAdapter.MyViewHolder holder, int position) {
            holder.category.setText(data.get(position).get("category"));
            holder.description.setText(data.get(position).get("description"));
            holder.money.setText(data.get(position).get("money"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String category = data.get(position).get("category");
                    String description = data.get(position).get("description");
                    String money = data.get(position).get("money");
                    Intent intent = new Intent(HomeActivity.this, AddOrEditActivity.class);
                    intent.putExtra("category", category);
                    intent.putExtra("description", description);
                    intent.putExtra("money", money);
                    intent.putExtra("mode", true); //edit
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
/*
    private class ExAdapter extends RecyclerView.Adapter<ExAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public  TextView category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;

                category = itemView.findViewById(R.id.category);
            }
        }

        @NonNull
        @Override
        public ExAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.daily_records, parent,false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExAdapter.MyViewHolder holder, int position) {
            holder.category.setText(data.get(position).get("category"));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }*/
}