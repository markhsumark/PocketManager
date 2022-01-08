package com.example.pocketmanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.tabs.TabLayout;

public class AddOrEditFragment extends Fragment {

    int type;
    Button delete, done;
    Button datePickButton, timePickButton;
    String mode;
    Spinner assetPicker, categoryPicker;
    EditText note, amount;
    AccountViewModel accountViewModel;
    Calendar calendar = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat time = new SimpleDateFormat("a hh:mm");
    List<String> types = Arrays.asList("收入", "支出", "轉帳");
    List<String> assets = Arrays.asList("現金", "帳戶");
    List<String> inCategories = Arrays.asList("打工", "吃飯飯");
    List<String> outCategories = Arrays.asList("娛樂", "交通", "家居", "健康", "教育");

    public AddOrEditFragment(int type) {
        // Required empty public constructor
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_or_edit, container, false);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        Intent intent = getActivity().getIntent();
        mode = intent.getStringExtra("mode");
        assetPicker = v.findViewById(R.id.assetPicker);
        datePickButton = v.findViewById(R.id.datePickButton);
        categoryPicker = v.findViewById(R.id.categoryPicker);
        timePickButton = v.findViewById(R.id.timePickButton);
        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        note = v.findViewById(R.id.noteEditor);
        amount = v.findViewById(R.id.amountEditor);
        done = v.findViewById(R.id.done);
        delete = v.findViewById(R.id.delete);
        calendar.set(Calendar.YEAR, intent.getIntExtra("Year",Calendar.getInstance().get(Calendar.YEAR)));
        calendar.set(Calendar.MONTH, intent.getIntExtra("Month",Calendar.getInstance().get(Calendar.MONTH)));
        calendar.set(Calendar.DAY_OF_MONTH, intent.getIntExtra("Day",Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        calendar.set(Calendar.HOUR_OF_DAY, intent.getIntExtra("Hour",Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        calendar.set(Calendar.MINUTE, intent.getIntExtra("Minute",Calendar.getInstance().get(Calendar.MINUTE)));
        datePickButton.setText(date.format(calendar.getTime()));  //set initial value
        timePickButton.setText(time.format(calendar.getTime()));  //set initial value

        ArrayAdapter assetAdapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_spinner_dropdown_item, assets);
        assetPicker.setAdapter(assetAdapter);
        assetPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        if(type == 0) {
            ArrayAdapter categoryAdapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_spinner_dropdown_item, inCategories);
            categoryPicker.setAdapter(categoryAdapter);
            categoryPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        else if(type == 1){
            ArrayAdapter categoryAdapter = new ArrayAdapter(v.getContext(), android.R.layout.simple_spinner_dropdown_item, outCategories);
            categoryPicker.setAdapter(categoryAdapter);
            categoryPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        if (mode.equals("edit")) {  //edit mode
            //getSupportActionBar().setTitle("編輯頁面");
            assetPicker.setSelection(assets.indexOf(intent.getStringExtra("Property")));
            if (type==0)
                categoryPicker.setSelection(inCategories.indexOf(intent.getStringExtra("Category")));
            else
                categoryPicker.setSelection(outCategories.indexOf(intent.getStringExtra("Category")));
            note.setText(intent.getStringExtra("Note"));
            amount.setText(intent.getStringExtra("Price"));
            done.setText("儲存");
        } else if (mode.equals("add")) {
            //getSupportActionBar().setTitle("新增頁面");
            delete.setVisibility(View.GONE);
            done.setText("新增");
        }

        //設置返回、完成更動、刪除按鈕的功能
        delete.setOnClickListener(new View.OnClickListener() {  //刪除現有資料
            @Override
            public void onClick(View v) {
                accountViewModel.deleteAccounts(new Account(
                        intent.getIntExtra("Id",0)));
                getActivity().finish();
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
                    try {
                        Integer.parseInt(amount.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(v.getContext(),"請輸入合法金額",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (mode.equals("edit")) {
                        accountViewModel.updateAccounts(new Account(
                                intent.getIntExtra("Id", 0),
                                assetPicker.getSelectedItem().toString(),
                                types.get(type),
                                Integer.parseInt(amount.getText().toString()),
                                categoryPicker.getSelectedItem().toString(),
                                "子類別",
                                calendar,
                                note.getText().toString()));
                    } else if (mode.equals("add")) {
                        accountViewModel.insertAccounts(new Account(
                                assetPicker.getSelectedItem().toString(),
                                types.get(type),
                                Integer.parseInt(amount.getText().toString()),
                                categoryPicker.getSelectedItem().toString(),
                                "子類別",
                                calendar,
                                note.getText().toString()));
                    }
                    getActivity().finish();
                }
            }
        });

        return v;
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