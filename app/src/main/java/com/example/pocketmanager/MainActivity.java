package com.example.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    Button google_button,hand_backup,auto_backup,edit_category,property;
    Switch remind_switch,speak_switch,notice_switch;
    public Integer n = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auto_backup = findViewById(R.id.auto_backup);
        auto_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainActivity6.class);
                startActivity(intent);
            }
        });
        edit_category=findViewById(R.id.edit_catogory);
        edit_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });
        property=findViewById(R.id.property);
        property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainActivity9.class);
                startActivity(intent);
            }
        });
        google_button=findViewById(R.id.google_button);
        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(n==0) {
                    google_button.setText("登入google");
                    n=1;
                }
                else {
                    google_button.setText("連結google");
                    n=0;
                }
            }
        });
        remind_switch=findViewById(R.id.remind_switch);
        remind_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
                else{

                }
            }
        });
        speak_switch=findViewById(R.id.speak_switch);
        speak_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
                else{

                }
            }
        });
        notice_switch=findViewById(R.id.notice_switch);
        notice_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
                else{

                }
            }
        });
    }
}