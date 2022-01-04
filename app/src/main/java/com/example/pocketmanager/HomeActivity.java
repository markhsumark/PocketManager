package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kal.rackmonthpicker.RackMonthPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private Button monthPicker;
    private ExAdapter exAdapter;
    private List<Account> data = new ArrayList<>();
    private AccountViewModel accountViewModel;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM月");
    private final Calendar date = Calendar.getInstance();
    private LiveData<List<Account>> listLiveData = null;

    public HomeActivity() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        //  點選新增按鈕會跳轉頁面
        ImageButton lastMonth = findViewById(R.id.lastMonth);
        ImageButton nextMonth = findViewById(R.id.nextMonth);
        monthPicker = findViewById(R.id.monthPicker);
        monthPicker.setText(dateFormat.format(date.getTime()));
        RecyclerView externalRecyclerView = findViewById(R.id.externalRecyclerView);
        externalRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager exLayoutManager = new LinearLayoutManager(this);
        externalRecyclerView.setLayoutManager(exLayoutManager);
        exAdapter = new ExAdapter();
        externalRecyclerView.setAdapter(exAdapter);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        resetLiveData();

        lastMonth.setOnClickListener(v -> {
            date.add(Calendar.MONTH,-1);
            monthPicker.setText(dateFormat.format(date.getTime()));
            resetLiveData();
        });

        nextMonth.setOnClickListener(v -> {
            date.add(Calendar.MONTH,1);
            monthPicker.setText(dateFormat.format(date.getTime()));
            resetLiveData();
        });

        FloatingActionButton previousStep = findViewById(R.id.previousStep);
        previousStep.setOnClickListener(v -> {
            //TODO
        });

        FloatingActionButton nextStep = findViewById(R.id.nextStep);
        nextStep.setOnClickListener(v -> {
            //TODO
        });

        FloatingActionButton editor = findViewById(R.id.editor);
        editor.setOnClickListener(v -> {
            //TODO
        });

        FloatingActionButton adder = findViewById(R.id.adder);
        adder.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddOrEditActivity.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetLiveData(){
        if(listLiveData != null && listLiveData.hasActiveObservers()){
            listLiveData.removeObservers(HomeActivity.this);
        }
        listLiveData = accountViewModel.getAccountsLive(date.get(Calendar.YEAR),date.get(Calendar.MONTH));
        listLiveData.observe(HomeActivity.this, accounts -> {
            data = accounts;
            Log.e("size",Integer.toString(accounts.size()));
            exAdapter.notifyDataSetChanged();
        });
    }

    public void rackMonthPicker(View v){
        new RackMonthPicker(this)
                .setLocale(Locale.TRADITIONAL_CHINESE)
                //.setSelectedMonth(date.get(Calendar.MONTH))
                //.setSelectedYear(date.get(Calendar.YEAR))
                .setNegativeText("取消")
                .setPositiveText("確認")
                .setPositiveButton((month, startDate, endDate, year, monthLabel) -> {
                    date.set(Calendar.YEAR, year);
                    date.set(Calendar.MONTH, month-1);
                    monthPicker.setText(dateFormat.format(date.getTime()));
                    resetLiveData();
                })
                .setNegativeButton(Dialog::cancel).show();
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

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ExAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.category.setText(data.get(position).getCategory());
            holder.asset.setText(data.get(position).getAsset());
            holder.amount.setText(Integer.toString(data.get(position).getAmount()));
            holder.itemView.setOnClickListener(v -> {
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
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}