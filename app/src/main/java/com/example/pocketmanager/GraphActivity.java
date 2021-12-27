package com.example.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    Button categoryButton;
    ToggleButton toggleButton;
    PieChart pieChart;//圖表
    TextView propertyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        setContentView(R.layout.activity_graph);
        categoryButton = findViewById(R.id.categoryButton);
        toggleButton = findViewById(R.id.chart_toggleButton);//圖表切換按鈕

        Spinner spinner = (Spinner) findViewById(R.id.categoryspinner);//資產選項
        //下拉式選單
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.property_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(spnOnItemSelected);

        /*MonthPickText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, monthEditer.class);
                startActivity(intent);
            }
        });*/

        //長條圓餅提示切換
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switch (compoundButton.getId()){
                    case R.id.chart_toggleButton:
                        if(compoundButton.isChecked()) Toast.makeText(GraphActivity.this,"長條圖模式", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(GraphActivity.this,"圓餅圖模式",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(GraphActivity.this, categoryListActivity.class);
//                startActivity(intent);
            }
        });
        //靜態載入佈局
        //setContentView(R.layout.activity_main);
        //初始化介面
        show();

    }

    private void show() {
        pieChart = (PieChart) findViewById(R.id.incomePieChart);
        //如果啟用此選項,則圖表中的值將以百分比形式繪製,而不是以原始值繪製
        pieChart.setUsePercentValues(true);
        //如果這個元件應該啟用(應該被繪製)FALSE如果沒有。如果禁用,此元件的任何內容將被繪製預設
        pieChart.getDescription().setEnabled(false);
        //將額外的偏移量(在圖表檢視周圍)附加到自動計算的偏移量
        pieChart.setExtraOffsets(5, 10, 5, 5);
        //較高的值表明速度會緩慢下降 例如如果它設定為0,它會立即停止。1是一個無效的值,並將自動轉換為0.999f。
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        //設定中間字型
        pieChart.setCenterText("收入");
        //設定為true將餅中心清空
        pieChart.setDrawHoleEnabled(true);
        //套孔,繪製在PieChart中心的顏色
        pieChart.setHoleColor(Color.WHITE);
        //設定透明圓應有的顏色。
        pieChart.setTransparentCircleColor(Color.WHITE);
        //設定透明度圓的透明度應該有0 =完全透明,255 =完全不透明,預設值為100。
        pieChart.setTransparentCircleAlpha(110);
        //設定在最大半徑的百分比餅圖中心孔半徑(最大=整個圖的半徑),預設為50%
        pieChart.setHoleRadius(58f);
        //設定繪製在孔旁邊的透明圓的半徑,在最大半徑的百分比在餅圖*(max =整個圖的半徑),預設55% -> 5%大於中心孔預設
        pieChart.setTransparentCircleRadius(61f);
        //將此設定為true,以繪製顯示在pie chart
        pieChart.setDrawCenterText(true);
        //集度的radarchart旋轉偏移。預設270f -->頂(北)
        pieChart.setRotationAngle(0);
        //設定為true,使旋轉/旋轉的圖表觸控。設定為false禁用它。預設值:true
        pieChart.setRotationEnabled(true);
        //將此設定為false,以防止由抽頭姿態突出值。值仍然可以通過拖動或程式設計高亮顯示。預設值:真
        pieChart.setHighlightPerTapEnabled(true);
        //建立Legend物件
        Legend l = pieChart.getLegend();
        //設定垂直對齊of the Legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //設定水平of the Legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //設定方向
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        //其中哪一個將畫在圖表或外
        l.setDrawInside(false);
        //設定水平軸上圖例項之間的間距
        l.setXEntrySpace(7f);
        //設定在垂直軸上的圖例項之間的間距
        l.setYEntrySpace(0f);
        //設定此軸上標籤的所使用的y軸偏移量 更高的偏移意味著作為一個整體的Legend將被放置遠離頂部。
        l.setYOffset(0f);
        //設定入口標籤的顏色。
        pieChart.setEntryLabelColor(Color.WHITE);
        //設定入口標籤的大小。預設值:13dp
        pieChart.setEntryLabelTextSize(12f);
        //模擬的資料來源
        PieEntry x1 = new PieEntry(15.8f , "one" , R.color.darkGreen) ;
        PieEntry x2 = new PieEntry(15.8f , "two") ;
        PieEntry x3 = new PieEntry(15.8f , "three") ;
        PieEntry x4 = new PieEntry(15.8f , "four") ;
        PieEntry x5 = new PieEntry(15.8f , "five") ;
        PieEntry x6 = new PieEntry(15.8f , "six") ;
        PieEntry x7 = new PieEntry(15.8f , "seven") ;
        PieEntry x8 = new PieEntry(15.8f , "eight") ;
        PieEntry x9 = new PieEntry(15.8f , "nine") ;
        PieEntry x10 = new PieEntry(15.8f , "ten") ;
        //新增到List集合
        List<PieEntry> list = new ArrayList<>() ;
        list.add(x1) ;
        list.add(x2) ;
        list.add(x3) ;
        list.add(x4) ;
        list.add(x5) ;
        list.add(x6) ;
        list.add(x7) ;
        list.add(x8) ;
        list.add(x9) ;
        list.add(x10) ;
        //設定到PieDataSet物件
        PieDataSet set = new PieDataSet(list , "表一") ;
        set.setDrawValues(false);//設定為true,在圖表繪製y
        set.setAxisDependency(YAxis.AxisDependency.LEFT);//設定Y軸,這個資料集應該被繪製(左或右)。預設值:左
        set.setAutomaticallyDisableSliceSpacing(false);//當啟用時,片間距將是0時,最小值要小於片間距本身
        set.setSliceSpace(5f);//間隔
        set.setSelectionShift(10f);//點選伸出去的距離
        /**
         * 設定該資料集前應使用的顏色。顏色使用只要資料集所代表的條目數目高於顏色陣列的大小。
         * 如果您使用的顏色從資源, 確保顏色已準備好(通過呼叫getresources()。getColor(…))之前,將它們新增到資料集
         * */
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        set.setColors(colors);
        //傳入PieData
        PieData data = new PieData(set);
        //為圖表設定新的資料物件
        pieChart.setData(data);
        //重新整理
        pieChart.invalidate();
        //動畫圖上指定的動畫時間軸的繪製
        //pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }

    private AdapterView.OnItemSelectedListener spnOnItemSelected
            = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            String sPos=String.valueOf(pos);
            String sInfo=parent.getItemAtPosition(pos).toString();
            //String sInfo=parent.getSelectedItem().toString();

        }
        public void onNothingSelected(AdapterView<?> parent) {
            //
        }
    };
}