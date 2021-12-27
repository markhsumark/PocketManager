package com.example.pocketmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddOrEditActivity extends AppCompatActivity {
    private TextView title;
    private Button datePickButton, timePickButton;
    private Button back, delete, done;
    Spinner typePicker, assetPicker, categoryPicker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit_page);
        Intent intent = getIntent();
        boolean mode = intent.getBooleanExtra("mode", false);

        title = findViewById(R.id.title);
        typePicker = findViewById(R.id.typePicker);
        assetPicker = findViewById(R.id.assetPicker);
        categoryPicker = findViewById(R.id.categoryPicker);
        datePickButton = findViewById(R.id.datePickButton);
        timePickButton = findViewById(R.id.timePickButton);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        done = findViewById(R.id.done);
        if (mode) {  //edit mode
            title.setText("編輯頁面");
            EditText description = findViewById(R.id.descriptionEditor);
            EditText money = findViewById(R.id.moneyEditor);

            typePicker.setSelection(Integer.parseInt(intent.getStringExtra("type")));
            assetPicker.setSelection(Integer.parseInt(intent.getStringExtra("asset")));
            categoryPicker.setSelection(Integer.parseInt(intent.getStringExtra("category")));
            description.setText(intent.getStringExtra("description"));
            money.setText(intent.getStringExtra("money"));
        } else {
            title.setText("新增頁面");
            delete.setVisibility(View.GONE);
///*  設置點進adder按鈕時的日期
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String date = String.valueOf(year) + "-" + (month < 10 ? "0" + String.valueOf(month) : String.valueOf(month))
                    + "-" + (day < 10 ? "0" + String.valueOf(day) : String.valueOf(day));
            String time = (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour)) +
                    ":" + (minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute));
            datePickButton.setText(date);  //set initial value
            timePickButton.setText(time);  //set initial value
//*/
        }
///*  設置type,asset,category綁定的資源，並可於頁面中更改選取的物件
        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this
                , R.array.type, android.R.layout.simple_dropdown_item_1line);
        typePicker.setAdapter(typeAdapter);
        typePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter assetAdapter = ArrayAdapter.createFromResource(this
                , R.array.asset, android.R.layout.simple_dropdown_item_1line);
        assetPicker.setAdapter(assetAdapter);
        assetPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this
                , R.array.category, android.R.layout.simple_dropdown_item_1line);
        categoryPicker.setAdapter(categoryAdapter);
        categoryPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });
//*/
///*  設置返回、完成更動、刪除按鈕的功能
        back.setOnClickListener(new View.OnClickListener() {  //返回HomePage
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {  //刪除現有資料
            @Override
            public void onClick(View v) {
                //TODO
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {  //新增OR編輯完成
            @Override
            public void onClick(View v) {
                //TODO
                finish();
            }
        });
//*/
    }
///*  選日期，選完自動跳去選時間
    public void datePicker(View v){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = String.valueOf(year)+"-"+(month<10?"0"+String.valueOf(month):String.valueOf(month))
                        +"-"+(day<10?"0"+String.valueOf(day):String.valueOf(day));
                datePickButton.setText(dateTime);
                timePicker(v);
            }

        }, year, month, day).show();
    }
//*/
///*  選時間
    public void timePicker(View v){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String dateTime = (hour<10?"0"+String.valueOf(hour):String.valueOf(hour))+
                        ":"+(minute<10?"0"+String.valueOf(minute):String.valueOf(minute));
                timePickButton.setText(dateTime);
            }

        }, hour, minute, true).show();
    }
//*/
}
