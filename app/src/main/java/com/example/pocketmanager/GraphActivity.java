package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    private RecyclerView incomeRecyclerView,expenseRecyclerView;
    private RecyclerView.LayoutManager incomeLayoutManager,expenseLayoutManager;
    private GraphActivity.incomeGraphAdapter incomeGraphAdapter;
    private GraphActivity.expenseGraphAdapter expenseGraphAdapter;
    private LinkedList<HashMap<String,String>> data;//長條圖data
    private ArrayList<BarEntry> values = new ArrayList<>();//
    private List<PieEntry> piedata;
    Button categoryButton;
    ToggleButton toggleButton;
    PieChart incomePieChart,expensePieChart;//圖表
    BarChart monthBarChart,incomeBarChart,expenseBarChart;
    View inPieChart,exPieChart,inBarChart,exBarChart;
    BarDataSet set1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        categoryButton = findViewById(R.id.categoryButton);
        toggleButton = findViewById(R.id.chart_toggleButton);//圖表切換按鈕
        incomeRecyclerView = findViewById(R.id.incomeRecyclerView);
        expenseRecyclerView = findViewById(R.id.expenseRecyclerView);
        Spinner spinner = (Spinner) findViewById(R.id.categoryspinner);//資產選項
        incomePieChart = (PieChart) findViewById(R.id.incomePieChart);
        expensePieChart = (PieChart) findViewById(R.id.expensePieChart);
        monthBarChart = (BarChart) findViewById(R.id.monthBarChart);
        incomeBarChart = (BarChart) findViewById(R.id.incomeBarChart);
        expenseBarChart = (BarChart) findViewById(R.id.expenseBarChart);
        inPieChart = (View) findViewById(R.id.incomePieChart);
        exPieChart = (View) findViewById(R.id.expensePieChart);
        inBarChart = (View) findViewById(R.id.incomeBarChart);
        exBarChart = (View) findViewById(R.id.expenseBarChart);


        //incomeRecycleView
        incomeRecyclerView.setHasFixedSize(true);
        incomeLayoutManager = new LinearLayoutManager(this);
        incomeRecyclerView.setLayoutManager(incomeLayoutManager);
        makeData();
        incomeGraphAdapter = new GraphActivity.incomeGraphAdapter();
        incomeRecyclerView.setAdapter(incomeGraphAdapter);
        //expenseRecycleView
        expenseRecyclerView.setHasFixedSize(true);
        expenseLayoutManager = new LinearLayoutManager(this);
        expenseRecyclerView.setLayoutManager(expenseLayoutManager);
        expenseGraphAdapter = new GraphActivity.expenseGraphAdapter();
        expenseRecyclerView.setAdapter(expenseGraphAdapter);


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
                        if(compoundButton.isChecked()) {
                            Toast.makeText(GraphActivity.this,"長條圖模式", Toast.LENGTH_SHORT).show();
                            inPieChart.setVisibility(View.GONE);
                            exPieChart.setVisibility(View.GONE);
                            inBarChart.setVisibility(View.VISIBLE);
                            exBarChart.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(GraphActivity.this,"圓餅圖模式",Toast.LENGTH_SHORT).show();
                            inPieChart.setVisibility(View.VISIBLE);
                            exPieChart.setVisibility(View.VISIBLE);
                            inBarChart.setVisibility(View.GONE);
                            exBarChart.setVisibility(View.GONE);
                        }
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
        //setContentView(R.layout.activity_graph);
        //當月收支長條圖
        monthChartShow();
        //圓餅圖介面
        incomeShow();
        expenseShow();
        //長條圖
        inShow();
        exShow();
    }

    private void incomeShow() {
        //如果啟用此選項,則圖表中的值將以百分比形式繪製,而不是以原始值繪製
        incomePieChart.setUsePercentValues(true);
        //如果這個元件應該啟用(應該被繪製)FALSE如果沒有。如果禁用,此元件的任何內容將被繪製預設
        incomePieChart.getDescription().setEnabled(false);
        //將額外的偏移量(在圖表檢視周圍)附加到自動計算的偏移量
        incomePieChart.setExtraOffsets(5, 10, 5, 5);
        //較高的值表明速度會緩慢下降 例如如果它設定為0,它會立即停止。1是一個無效的值,並將自動轉換為0.999f。
        incomePieChart.setDragDecelerationFrictionCoef(0.95f);
        //設定中間字型
        incomePieChart.setCenterText("收入");
        //設定為true將餅中心清空
        incomePieChart.setDrawHoleEnabled(true);
        //套孔,繪製在PieChart中心的顏色
        incomePieChart.setHoleColor(Color.WHITE);
        //設定透明圓應有的顏色。
        incomePieChart.setTransparentCircleColor(Color.WHITE);
        //設定透明度圓的透明度應該有0 =完全透明,255 =完全不透明,預設值為100。
        incomePieChart.setTransparentCircleAlpha(110);
        //設定在最大半徑的百分比餅圖中心孔半徑(最大=整個圖的半徑),預設為50%
        incomePieChart.setHoleRadius(58f);
        //設定繪製在孔旁邊的透明圓的半徑,在最大半徑的百分比在餅圖*(max =整個圖的半徑),預設55% -> 5%大於中心孔預設
        incomePieChart.setTransparentCircleRadius(61f);
        //將此設定為true,以繪製顯示在pie chart
        incomePieChart.setDrawCenterText(true);
        //集度的radarchart旋轉偏移。預設270f -->頂(北)
        incomePieChart.setRotationAngle(0);
        //設定為true,使旋轉/旋轉的圖表觸控。設定為false禁用它。預設值:true
        incomePieChart.setRotationEnabled(true);
        //將此設定為false,以防止由抽頭姿態突出值。值仍然可以通過拖動或程式設計高亮顯示。預設值:真
        incomePieChart.setHighlightPerTapEnabled(true);
        //建立Legend物件
        Legend l = incomePieChart.getLegend();
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
        incomePieChart.setEntryLabelColor(Color.WHITE);
        //設定入口標籤的大小。預設值:13dp
        incomePieChart.setEntryLabelTextSize(12f);
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
        incomePieChart.setData(data);
        //重新整理
        incomePieChart.invalidate();
        //動畫圖上指定的動畫時間軸的繪製
        //incomePieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }

    private void expenseShow() {

        //如果啟用此選項,則圖表中的值將以百分比形式繪製,而不是以原始值繪製
        expensePieChart.setUsePercentValues(true);
        //如果這個元件應該啟用(應該被繪製)FALSE如果沒有。如果禁用,此元件的任何內容將被繪製預設
        expensePieChart.getDescription().setEnabled(false);
        //將額外的偏移量(在圖表檢視周圍)附加到自動計算的偏移量
        expensePieChart.setExtraOffsets(5, 10, 5, 5);
        //較高的值表明速度會緩慢下降 例如如果它設定為0,它會立即停止。1是一個無效的值,並將自動轉換為0.999f。
        expensePieChart.setDragDecelerationFrictionCoef(0.95f);
        //設定中間字型
        expensePieChart.setCenterText("支出");
        //設定為true將餅中心清空
        expensePieChart.setDrawHoleEnabled(true);
        //套孔,繪製在PieChart中心的顏色
        expensePieChart.setHoleColor(Color.WHITE);
        //設定透明圓應有的顏色。
        expensePieChart.setTransparentCircleColor(Color.WHITE);
        //設定透明度圓的透明度應該有0 =完全透明,255 =完全不透明,預設值為100。
        expensePieChart.setTransparentCircleAlpha(110);
        //設定在最大半徑的百分比餅圖中心孔半徑(最大=整個圖的半徑),預設為50%
        expensePieChart.setHoleRadius(58f);
        //設定繪製在孔旁邊的透明圓的半徑,在最大半徑的百分比在餅圖*(max =整個圖的半徑),預設55% -> 5%大於中心孔預設
        expensePieChart.setTransparentCircleRadius(61f);
        //將此設定為true,以繪製顯示在pie chart
        expensePieChart.setDrawCenterText(true);
        //集度的radarchart旋轉偏移。預設270f -->頂(北)
        expensePieChart.setRotationAngle(0);
        //設定為true,使旋轉/旋轉的圖表觸控。設定為false禁用它。預設值:true
        expensePieChart.setRotationEnabled(true);
        //將此設定為false,以防止由抽頭姿態突出值。值仍然可以通過拖動或程式設計高亮顯示。預設值:真
        expensePieChart.setHighlightPerTapEnabled(true);
        //建立Legend物件
        Legend l = expensePieChart.getLegend();
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
        expensePieChart.setEntryLabelColor(Color.WHITE);
        //設定入口標籤的大小。預設值:13dp
        expensePieChart.setEntryLabelTextSize(12f);
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
        expensePieChart.setData(data);
        //重新整理
        expensePieChart.invalidate();
        //動畫圖上指定的動畫時間軸的繪製
        //pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }


    private void monthChartShow() {
        monthBarChart.getDescription().setEnabled(false);
//設置最大值條目，超出之後不會有值
        monthBarChart.setMaxVisibleValueCount(60);
//分別在x軸和y軸上進行縮放
        monthBarChart.setPinchZoom(true);
//設置剩餘統計圖的陰影
        monthBarChart.setDrawBarShadow(false);
//設置網格佈局
        monthBarChart.setDrawGridBackground(true);
//通過自定義一個x軸標籤來實現2,015 有分割符符bug
        ValueFormatter custom = new MyValueFormatter(" ");
//獲取x軸線
        XAxis xAxis = monthBarChart.getXAxis();
//設置x軸的顯示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//設置網格佈局
        xAxis.setDrawGridLines(true);
//圖表將避免第一個和最後一個標籤條目被減掉在圖表或屏幕的邊緣
        xAxis.setAvoidFirstLastClipping(false);
//繪製標籤  指x軸上的對應數值 默認true
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(custom);
//縮放後x 軸數據重疊問題
        xAxis.setGranularityEnabled(true);
//獲取右邊y標籤
        YAxis axisRight = monthBarChart.getAxisRight();
        axisRight.setStartAtZero(true);
//獲取左邊y軸的標籤
        YAxis axisLeft = monthBarChart.getAxisLeft();
//設置Y軸數值 從零開始
        axisLeft.setStartAtZero(true);
        monthBarChart.getAxisLeft().setDrawGridLines(false);
//設置動畫時間
        // monthBarChart.animateXY(600,600);
        monthBarChart.getLegend().setEnabled(true);
        getData();

        if (monthBarChart.getData() != null &&
                monthBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) monthBarChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            monthBarChart.getData().notifyDataChanged();
            monthBarChart.notifyDataSetChanged();
        }
        else {
            set1 = new BarDataSet(values, "日期");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            monthBarChart.setData(data);
            monthBarChart.setFitBars(true);
        }
        //繪製圖表
        monthBarChart.invalidate();
//設置柱形統計圖上的值
        monthBarChart.getData().setValueTextSize(10);
        for (IDataSet set : monthBarChart.getData().getDataSets()){
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
    }

    private void inShow() {
        incomeBarChart.getDescription().setEnabled(false);
        //incomeBarChart.setDescription("收入");

        //設置最大值條目，超出之後不會有值
        incomeBarChart.setMaxVisibleValueCount(60);
        //分別在x軸和y軸上進行縮放
        incomeBarChart.setPinchZoom(false);
        //雙擊縮放
        incomeBarChart.setDoubleTapToZoomEnabled(false);
        //設置剩餘統計圖的陰影
        incomeBarChart.setDrawBarShadow(false);
        //設置網格佈局
        incomeBarChart.setDrawGridBackground(true);
        //通過自定義一個x軸標籤來實現2,015 有分割符符bug
        ValueFormatter custom = new MyValueFormatter(" ");
        //獲取x軸線
        XAxis xAxis = incomeBarChart.getXAxis();
        //設置x軸的顯示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //設置網格佈局
        xAxis.setDrawGridLines(true);
        //圖表將避免第一個和最後一個標籤條目被減掉在圖表或屏幕的邊緣
        xAxis.setAvoidFirstLastClipping(false);
        //繪製標籤  指x軸上的對應數值 默認true
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(custom);
        //縮放後x 軸數據重疊問題
        xAxis.setGranularityEnabled(true);
        //獲取右邊y標籤
        YAxis axisRight = incomeBarChart.getAxisRight();
        axisRight.setStartAtZero(true);
        //獲取左邊y軸的標籤
        YAxis axisLeft = incomeBarChart.getAxisLeft();
        //設置Y軸數值 從零開始
        axisLeft.setStartAtZero(true);
        incomeBarChart.getAxisLeft().setDrawGridLines(false);
        //設置動畫時間
        // monthBarChart.animateXY(600,600);
        incomeBarChart.getLegend().setEnabled(true);
        //取得data
        getData();
        if (incomeBarChart.getData() != null &&
                incomeBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) incomeBarChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            incomeBarChart.getData().notifyDataChanged();
            incomeBarChart.notifyDataSetChanged();
        }
        else {
            set1 = new BarDataSet(values, "類別");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            incomeBarChart.setData(data);
            incomeBarChart.setFitBars(false);
        }

        //設置柱形統計圖上的值
        incomeBarChart.getData().setValueTextSize(10);
        for (IDataSet set : incomeBarChart.getData().getDataSets()){
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
    }

    private void exShow() {
        expenseBarChart.getDescription().setEnabled(false);

        //設置最大值條目，超出之後不會有值
        expenseBarChart.setMaxVisibleValueCount(60);
        //分別在x軸和y軸上進行縮放
        expenseBarChart.setPinchZoom(false);
        //雙擊縮放
        expenseBarChart.setDoubleTapToZoomEnabled(false);
        //設置剩餘統計圖的陰影
        expenseBarChart.setDrawBarShadow(false);
        //設置網格佈局
        expenseBarChart.setDrawGridBackground(true);
        //通過自定義一個x軸標籤來實現2,015 有分割符符bug
        ValueFormatter custom = new MyValueFormatter(" ");
        //獲取x軸線
        XAxis xAxis = expenseBarChart.getXAxis();
        //設置x軸的顯示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //設置網格佈局
        xAxis.setDrawGridLines(true);
        //圖表將避免第一個和最後一個標籤條目被減掉在圖表或屏幕的邊緣
        xAxis.setAvoidFirstLastClipping(false);
        //繪製標籤  指x軸上的對應數值 默認true
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(custom);
        //縮放後x 軸數據重疊問題
        xAxis.setGranularityEnabled(true);
        //獲取右邊y標籤
        YAxis YaxisRight = expenseBarChart.getAxisRight();
        YaxisRight.setStartAtZero(true);
        //獲取左邊y軸的標籤
        YAxis YaxisLeft = expenseBarChart.getAxisLeft();
        //設置Y軸數值 從零開始
        YaxisLeft.setStartAtZero(true);
        expenseBarChart.getAxisLeft().setDrawGridLines(false);
        //設置動畫時間
        // monthBarChart.animateXY(600,600);
        expenseBarChart.getLegend().setEnabled(true);
        //取得data
        getData();
        if (expenseBarChart.getData() != null &&
                expenseBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) expenseBarChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            expenseBarChart.getData().notifyDataChanged();
            expenseBarChart.notifyDataSetChanged();
        }
        else {
            set1 = new BarDataSet(values, "類別");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            expenseBarChart.setData(data);
            expenseBarChart.setFitBars(true);
        }
        //設置柱形統計圖上的值
        expenseBarChart.getData().setValueTextSize(10);
        for (IDataSet set : expenseBarChart.getData().getDataSets()){
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
    }

    public void getData(){
        Float aFloat = Float.valueOf("1");
        Log.v("xue","aFloat+++++"+aFloat);
        BarEntry barEntry = new BarEntry(aFloat,Float.valueOf("100"));
        BarEntry barEntry1 = new BarEntry(Float.valueOf("2"),Float.valueOf("210"));
        BarEntry barEntry2 = new BarEntry(Float.valueOf("3"),Float.valueOf("300"));
        BarEntry barEntry3 = new BarEntry(Float.valueOf("4"),Float.valueOf("450"));
        BarEntry barEntry4 = new BarEntry(Float.valueOf("5"),Float.valueOf("300"));
        BarEntry barEntry5 = new BarEntry(Float.valueOf("6"),Float.valueOf("650"));
        BarEntry barEntry6 = new BarEntry(Float.valueOf("7"),Float.valueOf("740"));
        values.add(barEntry);
        values.add(barEntry1);
        values.add(barEntry2);
        values.add(barEntry3);
        values.add(barEntry4);
        values.add(barEntry5);
        values.add(barEntry6);
    }

    private AdapterView.OnItemSelectedListener spnOnItemSelected
            = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            String sPos=String.valueOf(pos);
            String sInfo=parent.getItemAtPosition(pos).toString();

            Toast.makeText(GraphActivity.this,sPos+" "+sInfo, Toast.LENGTH_SHORT).show();
            //String sInfo=parent.getSelectedItem().toString();

        }
        public void onNothingSelected(AdapterView<?> parent) {
            //
        }
    };


    private void makeData(){
        data = new LinkedList<>();
        for(int i=1; i<20; i++){
            HashMap<String, String> row = new HashMap<>();
            row.put("type", String.valueOf(i/10));
            row.put("asset", String.valueOf(i/8));
            row.put("category", String.valueOf(i/5));
            row.put("description", "i=" + i);
            row.put("money", "123" + i);
            data.add(row);
        }
    }

    private class incomeGraphAdapter extends RecyclerView.Adapter<incomeGraphAdapter.MyViewHolder>{
        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView amount, asset, category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
                asset = itemView.findViewById(R.id.asset);
                amount = itemView.findViewById(R.id.amount);
            }
        }

        @NonNull
        @Override
        public incomeGraphAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.single_record, parent,false);

            return new GraphActivity.incomeGraphAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull  incomeGraphAdapter.MyViewHolder  holder, int position) {
            Resources res=getResources();
            holder.category.setText(res.getStringArray(R.array.category)[Integer.parseInt(data.get(position).get("category"))]);
            holder.asset.setText(data.get(position).get("description"));
            holder.amount.setText(data.get(position).get("money"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = data.get(position).get("type");
                    String assets = data.get(position).get("asset");
                    String category = data.get(position).get("category");
                    String description = data.get(position).get("description");
                    String money = data.get(position).get("money");
                    Intent intent = new Intent(GraphActivity.this, CategoryGraphActivity.class);
                    /*intent.putExtra("type", type);
                    intent.putExtra("asset", assets);
                    intent.putExtra("category", category);
                    intent.putExtra("description", description);
                    intent.putExtra("money", money);
                    intent.putExtra("mode", true); */
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class expenseGraphAdapter extends RecyclerView.Adapter<expenseGraphAdapter.MyViewHolder>{
        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView asset, amount, category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
                asset = itemView.findViewById(R.id.asset);
                amount = itemView.findViewById(R.id.amount);
            }
        }

        @NonNull
        @Override
        public expenseGraphAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.single_record, parent,false);

            return new GraphActivity.expenseGraphAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull  expenseGraphAdapter.MyViewHolder  holder, int position) {
            Resources res=getResources();
            holder.category.setText(res.getStringArray(R.array.category)[Integer.parseInt(data.get(position).get("category"))]);
            holder.asset.setText(data.get(position).get("description"));
            holder.amount.setText(data.get(position).get("money"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type = data.get(position).get("type");
                    String assets = data.get(position).get("asset");
                    String category = data.get(position).get("category");
                    String description = data.get(position).get("description");
                    String money = data.get(position).get("money");
                    Intent intent = new Intent(GraphActivity.this, CategoryGraphActivity.class);
                    /*intent.putExtra("type", type);
                    intent.putExtra("asset", assets);
                    intent.putExtra("category", category);
                    intent.putExtra("description", description);
                    intent.putExtra("money", money);
                    intent.putExtra("mode", true); */
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    public class MyValueFormatter extends ValueFormatter{
        private final DecimalFormat mFormat;
        private String suffix;
        public MyValueFormatter(String suffix) {
            mFormat = new DecimalFormat("00");
            this.suffix = suffix;
        }
        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + suffix;
        }
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            if (axis instanceof XAxis) {
                return mFormat.format(value);
            } else if (value > 0) {
                return mFormat.format(value) + suffix;
            } else {
                return mFormat.format(value);
            }
        }
    }

    public abstract class ValueFormatter implements IAxisValueFormatter, IValueFormatter {
        /**
         * <b>DO NOT USE</b>, only for backwards compatibility and will be removed in future versions.
         *
         * @param value the value to be formatted
         * @param axis  the axis the value belongs to
         * @return formatted string label
         */
        @Override
        @Deprecated
        public String getFormattedValue(float value, AxisBase axis) {
            return getFormattedValue(value);
        }
        /**
         * <b>DO NOT USE</b>, only for backwards compatibility and will be removed in future versions.
         * @param value           the value to be formatted
         * @param entry           the entry the value belongs to - in e.g. BarChart, this is of class BarEntry
         * @param dataSetIndex    the index of the DataSet the entry in focus belongs to
         * @param viewPortHandler provides information about the current chart state (scale, translation, ...)
         * @return formatted string label
         */
        @Override
        @Deprecated
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return getFormattedValue(value);
        }
        /**
         * Called when drawing any label, used to change numbers into formatted strings.
         *
         * @param value float to be formatted
         * @return formatted string label
         */
        public String getFormattedValue(float value) {
            return String.valueOf(value);
        }
        /**
         * Used to draw axis labels, calls {@link #getFormattedValue(float)} by default.
         *
         * @param value float to be formatted
         * @param axis  axis being labeled
         * @return formatted string label
         */
        public String getAxisLabel(float value, AxisBase axis) {
            return getFormattedValue(value);
        }
        /**
         * Used to draw bar labels, calls {@link #getFormattedValue(float)} by default.
         *
         * @param barEntry bar being labeled
         * @return formatted string label
         */
        public String getBarLabel(BarEntry barEntry) {
            return getFormattedValue(barEntry.getY());
        }
        /**
         * Used to draw stacked bar labels, calls {@link #getFormattedValue(float)} by default.
         *
         * @param value        current value to be formatted
         * @param stackedEntry stacked entry being labeled, contains all Y values
         * @return formatted string label
         */
        public String getBarStackedLabel(float value, BarEntry stackedEntry) {
            return getFormattedValue(value);
        }
        /**
         * Used to draw line and scatter labels, calls {@link #getFormattedValue(float)} by default.
         *
         * @param entry point being labeled, contains X value
         * @return formatted string label
         */
        public String getPointLabel(Entry entry) {
            return getFormattedValue(entry.getY());
        }
        /**
         * Used to draw pie value labels, calls {@link #getFormattedValue(float)} by default.
         *
         * @param value    float to be formatted, may have been converted to percentage
         * @param pieEntry slice being labeled, contains original, non-percentage Y value
         * @return formatted string label
         */
        public String getPieLabel(float value, PieEntry pieEntry) {
            return getFormattedValue(value);
        }
        /**
         * Used to draw radar value labels, calls {@link #getFormattedValue(float)} by default.
         *
         * @param radarEntry entry being labeled
         * @return formatted string label
         */
        public String getRadarLabel(RadarEntry radarEntry) {
            return getFormattedValue(radarEntry.getY());
        }
        /**
         * Used to draw bubble size labels, calls {@link #getFormattedValue(float)} by default.
         *
         * @param bubbleEntry bubble being labeled, also contains X and Y values
         * @return formatted string label
         */
        public String getBubbleLabel(BubbleEntry bubbleEntry) {
            return getFormattedValue(bubbleEntry.getSize());
        }
        /**
         * Used to draw high labels, calls {@link #getFormattedValue(float)} by default.
         *
         * @param candleEntry candlestick being labeled
         * @return formatted string label
         */
        public String getCandleLabel(CandleEntry candleEntry) {
            return getFormattedValue(candleEntry.getHigh());
        }
    }
}