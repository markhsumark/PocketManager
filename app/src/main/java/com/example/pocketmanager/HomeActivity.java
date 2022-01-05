package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private RecyclerView externalRecyclerView;
    private RecyclerView.LayoutManager exLayoutManager;
    private ExAdapter exAdapter;
    private List<Account> data = new ArrayList<>();
    private AccountViewModel accountViewModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM月");
    private Calendar date = Calendar.getInstance();
    private Context context;

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
        context = externalRecyclerView.getContext();
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
                exAdapter = new ExAdapter(data ,R.layout.add_or_edit_page, context);
                externalRecyclerView.setAdapter(exAdapter);
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

}