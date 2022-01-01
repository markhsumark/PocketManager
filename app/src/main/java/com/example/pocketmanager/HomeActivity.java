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
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private TextView month;
    private ImageButton monthPicker;
    private FloatingActionButton previousStep, nextStep, editor, adder;
    private RecyclerView externalRecyclerView, internalRecyclerView;
    private RecyclerView.LayoutManager exLayoutManager, inLayoutManager;
    private ExAdapter exAdapter;
    List<Account> data = new ArrayList<>();
    AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Calendar calendar = Calendar.getInstance();
        month = findViewById(R.id.month);
        month.setText(calendar.get(Calendar.YEAR)+","+calendar.get(Calendar.MONTH)+"月");
        monthPicker = findViewById(R.id.monthPicker);
        monthPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rackMonthPicker(v, month);
            }
        });
        if((getBaseContext().getResources().getConfiguration().uiMode& Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
            monthPicker.setBackgroundColor(Color.parseColor("#000000"));


        //  點選新增按鈕會跳轉頁面
        externalRecyclerView = findViewById(R.id.externalRecyclerView);
        externalRecyclerView.setHasFixedSize(true);
        exLayoutManager = new LinearLayoutManager(this);
        externalRecyclerView.setLayoutManager(exLayoutManager);
        exAdapter = new ExAdapter();
        externalRecyclerView.setAdapter(exAdapter);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getAllAccountsLive().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                data = new ArrayList<>();
                for (int i = 0; i < accounts.size(); i++) {
                    data.add(accounts.get(i));
                }
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

    public void rackMonthPicker(View v, TextView t){

        new RackMonthPicker(this)
                .setLocale(Locale.ENGLISH)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        String[] time = monthLabel.split(",");
                        if(time[0].equals("Jan")) time[0]="1";
                        else if (time[0].equals("Feb")) time[0]="2";
                        else if (time[0].equals("Mar")) time[0]="3";
                        else if (time[0].equals("Apr")) time[0]="4";
                        else if (time[0].equals("May")) time[0]="5";
                        else if (time[0].equals("Jun")) time[0]="6";
                        else if (time[0].equals("Jul")) time[0]="7";
                        else if (time[0].equals("Aug")) time[0]="8";
                        else if (time[0].equals("Sep")) time[0]="9";
                        else if (time[0].equals("Oct")) time[0]="10";
                        else if (time[0].equals("Nov")) time[0]="11";
                        else if (time[0].equals("Dec")) time[0]="12";
                        t.setText(time[1]+","+time[0]+"月");
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
            public TextView description, money, category;

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
        public void onBindViewHolder(@NonNull ExAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.category.setText(data.get(position).getCategory());
            holder.description.setText(data.get(position).getNote());
            holder.money.setText(Integer.toString(data.get(position).getAmount()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, AddOrEditActivity.class);
                    intent.putExtra("mode", "edit");
                    intent.putExtra("Id", data.get(position).getId());
                    intent.putExtra("Property", data.get(position).getProperty());
                    intent.putExtra("InOut", data.get(position).getInOut());
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