package com.mark.pocketmanager.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.mark.pocketmanager.R;

public class Buget extends AppCompatActivity {
    Switch remindSwitch;
    EditText bugetEdit,test;
    Button save;
    SharedPreferences settingData;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buget);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        settingData = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        remindSwitch=findViewById(R.id.remindSwitch);

        remindSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean notification = remindSwitch.isChecked();

                SharedPreferences.Editor editor = settingData.edit();
                editor.putBoolean("notification",notification);
                editor.apply();
                if(isChecked){

                }
            }
        });
        remindSwitch.setChecked(settingData.getBoolean("notification",false));

        bugetEdit=findViewById(R.id.bugetEdit);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buget = bugetEdit.getText().toString();

                SharedPreferences.Editor editor = settingData.edit();
                editor.putString("buget",buget);
                editor.apply();

                //Log.e("buget:", "test");
                //Log.e("buget:", Integer.toString(buget));
                //bugetEdit.setText(settingData.getString("buget","0"));
                finish();
            }
        });
        bugetEdit.setText(settingData.getString("buget","0"));
    }
}