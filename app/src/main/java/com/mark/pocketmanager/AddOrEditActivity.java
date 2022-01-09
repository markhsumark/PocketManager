package com.mark.pocketmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.mark.pocketmanager.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AddOrEditActivity extends AppCompatActivity {

    private String[] tabs = {"支出","收入"};
    private List<AddOrEditFragment> tabFragmentList = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.add_or_edit_page);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        //添加tab
        for(int i=0; i<tabs.length; i++){
                tabLayout.addTab(tabLayout.newTab().setText(tabs[i]));
                tabFragmentList.add(new AddOrEditFragment(i));
        }

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return tabFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return tabFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position){
                return tabs[position];
            }
        });

        tabLayout.setupWithViewPager(viewPager,false);

        //設定預設頁面
        Intent intent = getIntent();
        if(intent.getStringExtra("mode").equals("edit")) {
            if (intent.getStringExtra("InOut").equals("支出")) {
                tabLayout.getTabAt(0).select();
            }
            else if (intent.getStringExtra("InOut").equals("收入")) {
                tabLayout.getTabAt(1).select();
            }
        }
        else if (intent.getStringExtra("mode").equals("add"))
            tabLayout.getTabAt(0).select();
    }
}
