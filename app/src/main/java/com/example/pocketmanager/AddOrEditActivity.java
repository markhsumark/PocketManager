package com.example.pocketmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddOrEditActivity extends AppCompatActivity {
    Button datePickButton, timePickButton;
    Button delete, done;
    String mode;
    Spinner typePicker, assetPicker, categoryPicker;
    EditText description, money;
    AccountViewModel accountViewModel;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
    List<String> assets = Arrays.asList("現金", "帳戶");
    List<String> types = Arrays.asList("收入", "支出", "轉帳");
    List<String> categories = Arrays.asList("食物", "運動", "娛樂", "交通", "家居", "健康", "教育");
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.add_or_edit_page);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        typePicker = findViewById(R.id.typePicker);
        assetPicker = findViewById(R.id.assetPicker);
        categoryPicker = findViewById(R.id.categoryPicker);
        datePickButton = findViewById(R.id.datePickButton);
        timePickButton = findViewById(R.id.timePickButton);
        description = findViewById(R.id.descriptionEditor);
        money = findViewById(R.id.moneyEditor);
        delete = findViewById(R.id.delete);
        done = findViewById(R.id.done);
        calendar.set(Calendar.YEAR, intent.getIntExtra("Year",Calendar.getInstance().get(Calendar.YEAR)));
        calendar.set(Calendar.MONTH, intent.getIntExtra("Month",Calendar.getInstance().get(Calendar.MONTH)));
        calendar.set(Calendar.DAY_OF_MONTH, intent.getIntExtra("Day",Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        calendar.set(Calendar.HOUR_OF_DAY, intent.getIntExtra("Hour",Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        calendar.set(Calendar.MINUTE, intent.getIntExtra("Minute",Calendar.getInstance().get(Calendar.MINUTE)));
        datePickButton.setText(date.format(calendar.getTime()));  //set initial value
        timePickButton.setText(time.format(calendar.getTime()));  //set initial value

        ArrayAdapter typeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types);
        typePicker.setAdapter(typeAdapter);
        typePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter assetAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, assets);
        assetPicker.setAdapter(assetAdapter);
        assetPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categoryPicker.setAdapter(categoryAdapter);
        categoryPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        if (mode.equals("edit")) {  //edit mode
            //getSupportActionBar().setTitle("編輯頁面");
            typePicker.setSelection(types.indexOf(intent.getStringExtra("InOut")));
            assetPicker.setSelection(assets.indexOf(intent.getStringExtra("Property")));
            categoryPicker.setSelection(categories.indexOf(intent.getStringExtra("Category")));
            description.setText(intent.getStringExtra("Note"));
            money.setText(intent.getStringExtra("Price"));
        } else if (mode.equals("add")) {
            //getSupportActionBar().setTitle("新增頁面");
            delete.setVisibility(View.GONE);
        }
        //設置type,asset,category綁定的資源，並可於頁面中更改選取的物件


        //設置返回、完成更動、刪除按鈕的功能
        delete.setOnClickListener(new View.OnClickListener() {  //刪除現有資料
            @Override
            public void onClick(View v) {
                accountViewModel.deleteAccounts(new Account(
                        intent.getIntExtra("Id",0)));
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {  //新增OR編輯完成
            @Override
            public void onClick(View v) {
                if(mode.equals("edit")) {
                    accountViewModel.updateAccounts(new Account(
                            intent.getIntExtra("Id",0),
                            assetPicker.getSelectedItem().toString(),
                            typePicker.getSelectedItem().toString(),
                            Integer.parseInt(money.getText().toString()),
                            categoryPicker.getSelectedItem().toString(),
                            "子類別",
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            description.getText().toString()));
                }
                else if(mode.equals("add")) {
                    accountViewModel.insertAccounts(new Account(
                            assetPicker.getSelectedItem().toString(),
                            typePicker.getSelectedItem().toString(),
                            Integer.parseInt(money.getText().toString()),
                            categoryPicker.getSelectedItem().toString(),
                            "子類別",
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            description.getText().toString()));
                }
                finish();
            }
        });
    }

    // 選日期，選完自動跳去選時間
    public void datePicker(View v){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                datePickButton.setText(date.format(calendar.getTime()));  //set initial value
            }
        }, year, month, day).show();
    }

    // 選時間
    public void timePicker(View v){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);
                timePickButton.setText(time.format(calendar.getTime()));  //set initial value
            }
        }, hour, minute, false).show();
    }
}
