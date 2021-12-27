package com.example.pocketmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PropertyActivity extends AppCompatActivity {
    Button back,property_add;

    Number cash_textbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_page);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PropertyActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        property_add = findViewById(R.id.property_add);

    }
}
