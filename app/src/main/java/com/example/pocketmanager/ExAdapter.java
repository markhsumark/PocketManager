package com.example.pocketmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExAdapter extends RecyclerView.Adapter<ExAdapter.MyViewHolder>{

    private List<Account> data;
    private int layoutId;
    private Context context;

    public ExAdapter(List<Account> data, int layoutId, Context context){
        this.data = data;
        this.layoutId = layoutId;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView date, dayOfTheWeek, yearAndMonth, dailyInAmount, dailyOutAmount;
        public RecyclerView internalRecyclerView;

        public MyViewHolder(View v){
            super(v);
            itemView = v;
            date = itemView.findViewById(R.id.date);
            dayOfTheWeek = itemView.findViewById(R.id.dayOfTheWeek);
            yearAndMonth = itemView.findViewById(R.id.yearAndMonth);
            dailyInAmount = itemView.findViewById(R.id.dailyInAmount);
            dailyOutAmount = itemView.findViewById(R.id.dailyOutAmount);
            internalRecyclerView = itemView.findViewById(R.id.internalRecyclerView);
        }
    }

    @NonNull
    @Override
    public ExAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.daily_records, parent,false);
        return new ExAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.date.setText("幾號");
        holder.dayOfTheWeek.setText("星期幾");
        holder.yearAndMonth.setText("幾年幾月");
        holder.dailyInAmount.setText("$ " + Integer.toString(data.get(position).getAmount()));
        holder.dailyOutAmount.setText("$ " + Integer.toString(data.get(position).getAmount()));

        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.internalRecyclerView.setLayoutManager(manager);
        holder.internalRecyclerView.setAdapter(new InAdapter(data, layoutId, context));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Account> data) {
        this.data = data;
    }
}
