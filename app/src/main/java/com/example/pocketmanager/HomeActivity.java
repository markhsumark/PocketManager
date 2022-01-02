package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private Button monthPicker;
    private FloatingActionButton previousStep, nextStep, editor, adder;
    private RecyclerView externalRecyclerView, internalRecyclerView;
    private RecyclerView.LayoutManager exLayoutManager, inLayoutManager;
    private ExAdapter exAdapter;
    private List<Account> data = new ArrayList<>();
    private AccountViewModel accountViewModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM月");
    private Calendar date = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        //  點選新增按鈕會跳轉頁面
        monthPicker = findViewById(R.id.monthPicker);
        monthPicker.setText(dateFormat.format(date.getTime()));
        externalRecyclerView = findViewById(R.id.externalRecyclerView);
        externalRecyclerView.setHasFixedSize(true);
        exLayoutManager = new LinearLayoutManager(this);
        externalRecyclerView.setLayoutManager(exLayoutManager);
        exAdapter = new ExAdapter();
        externalRecyclerView.setAdapter(exAdapter);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getAccountsLive(date.get(Calendar.YEAR),date.get(Calendar.MONTH)).observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                data = accounts;
                /*data = new ArrayList<>();
                for (int i = 0; i < accounts.size(); i++) {
                    data.add(accounts.get(i));
                }*/
                Log.e("size",Integer.toString(accounts.size()));
                exAdapter.notifyDataSetChanged();
            }
        });
        ///*  點選新增按鈕會跳轉頁面
        previousStep = findViewById(R.id.previousStep);
        previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        nextStep = findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        editor = findViewById(R.id.editor);
        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        adder = findViewById(R.id.adder);
        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddOrEditActivity.class);
                intent.putExtra("mode", "add");
                startActivity(intent);
            }
        });
    }

    public void rackMonthPicker(View v){

        new RackMonthPicker(this)
                .setLocale(Locale.TRADITIONAL_CHINESE)
                .setSelectedMonth(5)
                .setSelectedYear(2019)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, month-1);
                        monthPicker.setText(dateFormat.format(date.getTime()));
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.cancel();
                    }
                }).show();
    }

    // 顯示RecyclerView
    private class ExAdapter extends RecyclerView.Adapter<ExAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView asset, amount, category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
                asset = itemView.findViewById(R.id.asset);
                amount = itemView.findViewById(R.id.amount);
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
        public void onBindViewHolder(@NonNull ExAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.category.setText(data.get(position).getCategory());
            holder.asset.setText(data.get(position).getAsset());
            holder.amount.setText(Integer.toString(data.get(position).getAmount()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, AddOrEditActivity.class);
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
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}