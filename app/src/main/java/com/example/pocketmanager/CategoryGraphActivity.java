package com.example.pocketmanager;

<<<<<<< HEAD
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
=======
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
>>>>>>> 1110448de4ab88f302655130f7e6acd4b2bcbd48
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.PieChart;

public class CategoryGraphActivity extends AppCompatActivity {
    ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_graph);

        PieChart catetogyincomePieChart;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("CatergoryChart");


        toggleButton = findViewById(R.id.categoryChartToggleButton);//圖表切換按鈕
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()){
                    case R.id.categoryChartToggleButton:
                        if(compoundButton.isChecked()) {
                            Toast.makeText(CategoryGraphActivity.this,"長條圖模式", Toast.LENGTH_SHORT).show();
                            /*inCatPieChart.setVisibility(View.GONE);
                            exCatPieChart.setVisibility(View.GONE);
                            inCatBarChart.setVisibility(View.VISIBLE);
                            exCatBarChart.setVisibility(View.VISIBLE);*/
                        }
                        else{
                            Toast.makeText(CategoryGraphActivity.this,"圓餅圖模式",Toast.LENGTH_SHORT).show();
                            /*inCatPieChart.setVisibility(View.VISIBLE);
                            exCatPieChart.setVisibility(View.VISIBLE);
                            inCatBarChart.setVisibility(View.GONE);
                            exCatBarChart.setVisibility(View.GONE);*/
                        }
                        if(compoundButton.isChecked()) Toast.makeText(CategoryGraphActivity.this,"長條圖模式", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(CategoryGraphActivity.this,"圓餅圖模式",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


}