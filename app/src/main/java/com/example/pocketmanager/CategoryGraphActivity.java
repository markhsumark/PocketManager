package com.example.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CategoryGraphActivity extends AppCompatActivity {
    ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_graph);

        toggleButton = findViewById(R.id.categoryChartToggleButton);//圖表切換按鈕
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()){
                    case R.id.categoryChartToggleButton:
                        if(compoundButton.isChecked()) Toast.makeText(CategoryGraphActivity.this,"長條圖模式", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(CategoryGraphActivity.this,"圓餅圖模式",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


}