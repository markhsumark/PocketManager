package com.example.pocketmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InAdapter extends RecyclerView.Adapter<InAdapter.MyViewHolder>{

    private List<Account> data;
    private int layoutId;
    private Context context;
    public InAdapter(List<Account> data, int layoutId, Context context){
        this.data = data;
        this.layoutId = layoutId;
        this.context = context;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView category, asset, amount;

        public MyViewHolder(View v){
            super(v);
            itemView = v;
            asset = itemView.findViewById(R.id.asset);
            amount = itemView.findViewById(R.id.amount);
            category = itemView.findViewById(R.id.category);
        }
    }

    @NonNull
    @Override
    public InAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from((parent.getContext()))
                .inflate(R.layout.single_record, parent,false);
        context = parent.getContext();
        return new InAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.category.setText(data.get(position).getCategory());
        holder.asset.setText(data.get(position).getAsset());
        holder.amount.setText(Integer.toString(data.get(position).getAmount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddOrEditActivity.class);
                intent.putExtra("mode", "edit");
                intent.putExtra("Id", data.get(position).getId());
                intent.putExtra("Property", data.get(position).getAsset());
                intent.putExtra("InOut", data.get(position).getType());
                intent.putExtra("Price", Integer.toString(data.get(position).getAmount()));
                intent.putExtra("Category", data.get(position).getCategory());
                intent.putExtra("SubCategory", data.get(position).getSubCategory());
                intent.putExtra("Year", data.get(position).getYear());
                intent.putExtra("Month", data.get(position).getMonth());
                intent.putExtra("Day", data.get(position).getDay());
                intent.putExtra("Hour", data.get(position).getHour());
                intent.putExtra("Minute", data.get(position).getMinute());
                intent.putExtra("Note", data.get(position).getNote());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
