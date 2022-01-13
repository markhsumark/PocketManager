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
import android.widget.Toast;

import com.mark.pocketmanager.R;

/*
    SharedPpreference's Data:
    {"budget" : String 預算金額, "ifRemind" : Boolean是否提醒}
*/
public class Budget extends AppCompatActivity {
    Switch remindSwitch;
    EditText bugetEdit;
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

        bugetEdit = findViewById(R.id.bugetEdit);
        remindSwitch = findViewById(R.id.remindSwitch);
        save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            String buget = bugetEdit.getText().toString();
            boolean ifRemind = remindSwitch.isChecked();
            if(ifRemind && buget.equals("0")){
                Toast.makeText(Budget.this ,"預算不得為0", Toast.LENGTH_LONG).show();
            }
            else {
//            Boolean ifRemind = remindSwitch.isChecked();
                SharedPreferences.Editor editor = settingData.edit();
                editor.putString("buget", buget);
                editor.putBoolean("ifRemind", ifRemind);
//            editor.putBoolean("ifRemind", ifRemind);
                editor.apply();
                finish();
            }
        });
        remindSwitch.setChecked(settingData.getBoolean("ifRemind",false));
        bugetEdit.setText(settingData.getString("buget","0"));
    }
}