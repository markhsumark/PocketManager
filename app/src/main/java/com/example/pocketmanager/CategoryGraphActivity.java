package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
                        break;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}