package com.example.pocketmanager;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;

public class MainActivity extends TabActivity{

    TabHost tabHost=null;      //选项卡控制器
    TabHost.TabSpec tabSpecA,tabSpecB, tabSpecC=null;   //选项卡,这里选项卡最好不用混用，有几个选项卡就设置几个对象
    TabWidget tabWidget;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获得TabHost实例；
        tabHost=getTabHost();
        //深色模式-->改tab背景顏色
        tabWidget = findViewById(android.R.id.tabs);
        if((getBaseContext().getResources().getConfiguration().uiMode&Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
            tabWidget.setBackgroundColor(Color.parseColor("#4F4F4F"));
        //获得TabHost.TabSpec对象实例；
        tabSpecA=tabHost.newTabSpec("帳單");
        //为TabSpec对象设置指示器
        tabSpecA.setIndicator("帳單",getResources().getDrawable(android.R.drawable.ic_media_play));
        //为选项卡设置内容，这里需要创建一个intent对象
        Intent intentA=new Intent();
        intentA.setClass(this, HomeActivity.class);
        tabSpecA.setContent(intentA);

        //for chart：
        tabSpecB=tabHost.newTabSpec("統計");
        tabSpecB.setIndicator("統計",getResources().getDrawable(android.R.drawable.ic_media_next));
        Intent intentB=new Intent();
        intentB.setClass(this, GraphActivity.class);
        tabSpecB.setContent(intentB);


        //for setting：
        tabSpecC=tabHost.newTabSpec("設定");
        tabSpecC.setIndicator("設定",getResources().getDrawable(android.R.drawable.ic_media_next));
        Intent intentC=new Intent();
        intentC.setClass(this, SettingActivity.class);
        tabSpecC.setContent(intentC);

        //最后一步，把两个选项卡TabSpec添加到选项卡控件TabHost中
        tabHost.addTab(tabSpecA);
        tabHost.addTab(tabSpecB);
        tabHost.addTab(tabSpecC);





    }
}