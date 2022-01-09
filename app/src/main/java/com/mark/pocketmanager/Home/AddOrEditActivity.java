package com.mark.pocketmanager.Home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mark.pocketmanager.Account.Account;
import com.mark.pocketmanager.Account.AccountViewModel;
import com.mark.pocketmanager.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddOrEditActivity extends AppCompatActivity {
    Button inButton, outButton;
    Button datePickButton, timePickButton;
    Button delete, done;
    String mode;
    Spinner assetPicker, categoryPicker;
    EditText note, amount;
    AccountViewModel accountViewModel;
    Calendar calendar = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
    List<String> assets = Arrays.asList("現金", "帳戶");
    List<String> types = Arrays.asList("收入", "支出", "轉帳");
    List<String> categories = Arrays.asList("食物", "運動", "娛樂", "交通", "家居", "健康", "教育");
    List<String> inCategories = Arrays.asList("家居", "健康", "教育");
    List<String> outCategories = Arrays.asList("食物", "運動", "娛樂");

    String type;
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
        inButton = findViewById(R.id.inButton);
        outButton = findViewById(R.id.outButton);
        assetPicker = findViewById(R.id.assetPicker);
        categoryPicker = findViewById(R.id.categoryPicker);
        datePickButton = findViewById(R.id.datePickButton);
        timePickButton = findViewById(R.id.timePickButton);
        note = findViewById(R.id.noteEditor);
        amount = findViewById(R.id.amountEditor);
        delete = findViewById(R.id.delete);
        done = findViewById(R.id.done);
        calendar.set(Calendar.YEAR, intent.getIntExtra("Year",Calendar.getInstance().get(Calendar.YEAR)));
        calendar.set(Calendar.MONTH, intent.getIntExtra("Month",Calendar.getInstance().get(Calendar.MONTH)));
        calendar.set(Calendar.DAY_OF_MONTH, intent.getIntExtra("Day",Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        calendar.set(Calendar.HOUR_OF_DAY, intent.getIntExtra("Hour",Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        calendar.set(Calendar.MINUTE, intent.getIntExtra("Minute",Calendar.getInstance().get(Calendar.MINUTE)));
        datePickButton.setText(date.format(calendar.getTime()));  //set initial value
        timePickButton.setText(time.format(calendar.getTime()));  //set initial value

        ArrayAdapter assetAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, assets);
        assetPicker.setAdapter(assetAdapter);
        assetPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter inCategoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, inCategories);
        ArrayAdapter outCategoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, outCategories);

        //點進頁面
        if (mode.equals("edit")) {  //edit mode
            //getSupportActionBar().setTitle("編輯頁面");
            if(intent.getStringExtra("InOut").equals("收入")) {
                inButton.setSelected(true);
                outButton.setSelected(false);
                inButton.setTextColor(Color.parseColor("#0072E3"));
                outButton.setTextColor(Color.parseColor("#000000"));
                categoryPicker.setAdapter(inCategoryAdapter);
                categoryPicker.setSelection(inCategories.indexOf(intent.getStringExtra("Category")));
            }
            else if(intent.getStringExtra("InOut").equals("支出")) {
                inButton.setSelected(false);
                outButton.setSelected(true);
                outButton.setTextColor(Color.parseColor("#FF0000"));
                inButton.setTextColor(Color.parseColor("#000000"));
                categoryPicker.setAdapter(outCategoryAdapter);
                categoryPicker.setSelection(outCategories.indexOf(intent.getStringExtra("Category")));
            }
            assetPicker.setSelection(assets.indexOf(intent.getStringExtra("Property")));
            note.setText(intent.getStringExtra("Note"));
            amount.setText(intent.getStringExtra("Price"));
            done.setText("儲存");
        } else if (mode.equals("add")) {
            //getSupportActionBar().setTitle("新增頁面");
            delete.setVisibility(View.GONE);
            done.setText("新增");
            type = "支出";
            categoryPicker.setAdapter(outCategoryAdapter);
            inButton.setSelected(false);
            outButton.setSelected(true);
            outButton.setTextColor(Color.parseColor("#FF0000"));
            inButton.setTextColor(Color.parseColor("#000000"));
        }
        //設置type,asset,category綁定的資源，並可於頁面中更改選取的物件

        inButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inButton.setTextColor(Color.parseColor("#0072E3"));
                outButton.setSelected(false);
                outButton.setTextColor(Color.parseColor("#000000"));
                type = "收入";

                categoryPicker.setAdapter(inCategoryAdapter);
            }
        });

        outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outButton.setTextColor(Color.parseColor("#FF0000"));
                inButton.setSelected(false);
                inButton.setTextColor(Color.parseColor("#000000"));
                type = "支出";

                categoryPicker.setAdapter(outCategoryAdapter);
            }
        });

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
                if(TextUtils.isEmpty(amount.getText())){
                    Toast.makeText(v.getContext(),"請輸入金額",Toast.LENGTH_LONG).show();
                }
                else if(amount.getText().toString().startsWith("0") || amount.getText().toString().startsWith("-")){
                    Toast.makeText(v.getContext(),"請輸入合法金額",Toast.LENGTH_LONG).show();
                }
                else {
                    if (mode.equals("edit")) {
                        accountViewModel.updateAccounts(new Account(
                                intent.getIntExtra("Id", 0),
                                assetPicker.getSelectedItem().toString(),
                                type,
                                Integer.parseInt(amount.getText().toString()),
                                categoryPicker.getSelectedItem().toString(),
                                "子類別",
                                calendar,
                                note.getText().toString()));
                    } else if (mode.equals("add")) {
                        accountViewModel.insertAccounts(new Account(
                                assetPicker.getSelectedItem().toString(),
                                type,
                                Integer.parseInt(amount.getText().toString()),
                                categoryPicker.getSelectedItem().toString(),
                                "子類別",
                                calendar,
                                note.getText().toString()));
                    }
                    finish();
                }
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
