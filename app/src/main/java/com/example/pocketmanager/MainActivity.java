package com.example.pocketmanager;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

public class MainActivity extends TabActivity{

    TabHost tabHost=null;      //选项卡控制器
    TabHost.TabSpec tabSpecA,tabSpecB, tabSpecC=null;   //选项卡,这里选项卡最好不用混用，有几个选项卡就设置几个对象

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获得TabHost实例；
        tabHost=getTabHost();
        //获得TabHost.TabSpec对象实例；
        tabSpecA=tabHost.newTabSpec("Home");
        //为TabSpec对象设置指示器
        tabSpecA.setIndicator("Home",getResources().getDrawable(android.R.drawable.ic_media_play));
        //为选项卡设置内容，这里需要创建一个intent对象
        Intent intentA=new Intent();
        intentA.setClass(this, HomeActivity.class);
        tabSpecA.setContent(intentA);

        //for chart：
        tabSpecB=tabHost.newTabSpec("Chart");
        tabSpecB.setIndicator("Chart",getResources().getDrawable(android.R.drawable.ic_media_next));
        Intent intentB=new Intent();
        intentB.setClass(this, GraphActivity.class);
        tabSpecB.setContent(intentB);


        //for setting：
        tabSpecC=tabHost.newTabSpec("Setting");
        tabSpecC.setIndicator("Setting",getResources().getDrawable(android.R.drawable.ic_media_next));
        Intent intentC=new Intent();
        intentC.setClass(this, SettingActivity.class);
        tabSpecC.setContent(intentC);

        //最后一步，把两个选项卡TabSpec添加到选项卡控件TabHost中
        tabHost.addTab(tabSpecA);
        tabHost.addTab(tabSpecB);
        tabHost.addTab(tabSpecC);





    }
}