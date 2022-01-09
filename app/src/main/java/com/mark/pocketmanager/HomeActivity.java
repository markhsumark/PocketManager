package com.mark.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mark.pocketmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kal.rackmonthpicker.RackMonthPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private Button monthPicker;
    private TextView inAmount, outAmount, sumAmount, noData;
    private RecyclerView externalRecyclerView;
    private ExAdapter exAdapter;
    private List<Account> data = new ArrayList<>();
    private AccountViewModel accountViewModel;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM月");
    private final Calendar date = Calendar.getInstance();
    private LiveData<List<Account>> listLiveData = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        ImageButton lastMonth = findViewById(R.id.lastMonth);
        ImageButton nextMonth = findViewById(R.id.nextMonth);
        inAmount = findViewById(R.id.inAmount);
        outAmount = findViewById(R.id.outAmount);
        sumAmount = findViewById(R.id.sumAmount);
        noData = findViewById(R.id.noData);
        monthPicker = findViewById(R.id.monthPicker);
        monthPicker.setText(dateFormat.format(date.getTime()));
        externalRecyclerView = findViewById(R.id.externalRecyclerView);
        externalRecyclerView.setHasFixedSize(true);
        externalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = externalRecyclerView.getContext();
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

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void resetLiveData(){
        if(listLiveData != null && listLiveData.hasActiveObservers()){
            listLiveData.removeObservers(HomeActivity.this);
        }
        listLiveData = accountViewModel.getAccountsLive(date.get(Calendar.YEAR), date.get(Calendar.MONTH));
        listLiveData.observe(HomeActivity.this, accounts -> {
            long inAmountValue = accountViewModel.getMonthAmount(date.get(Calendar.YEAR), date.get(Calendar.MONTH),"收入");
            long outAmountValue = accountViewModel.getMonthAmount(date.get(Calendar.YEAR), date.get(Calendar.MONTH),"支出");
            long sumAmountValue = inAmountValue - outAmountValue;
            inAmount.setText(Long.toString(inAmountValue));
            outAmount.setText(Long.toString(outAmountValue));
            sumAmount.setText(Long.toString(sumAmountValue));
            data = accounts;
            Log.e("size",Integer.toString(accounts.size()));
            if(accounts.size() != 0)
                noData.setVisibility(View.GONE);
            else
                noData.setVisibility(View.VISIBLE);
            exAdapter = new ExAdapter(data, context, accountViewModel);
            externalRecyclerView.setAdapter(exAdapter);
            exAdapter.notifyDataSetChanged();
        });
    }

    public void rackMonthPicker(View v){
        new RackMonthPicker(this)
            .setLocale(Locale.TRADITIONAL_CHINESE)
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

}