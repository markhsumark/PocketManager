package com.mark.pocketmanager.Setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mark.pocketmanager.R;

/*
    SharedPpreference's Data:
    {"budget" : String 預算金額, "ifRemind" : Boolean是否提醒}
*/
public class BudgetActivity extends AppCompatActivity {
    Switch remindSwitch;
    EditText budgetEdit;
    Button save;
    SharedPreferences settingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buget);

        View actionBar = findViewById(R.id.my_actionBar);
        ImageButton backButton = actionBar.findViewById(R.id.backButton);
        TextView title = actionBar.findViewById(R.id.title);
        title.setText("預算設定");
        backButton.setOnClickListener(v -> {
            finish();
        });

        settingData = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        budgetEdit = findViewById(R.id.budgetEdit);
        remindSwitch = findViewById(R.id.remindSwitch);
        save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            String buget = budgetEdit.getText().toString();
            boolean ifRemind = remindSwitch.isChecked();
            if(ifRemind && buget.equals("0")){
                Toast.makeText(BudgetActivity.this ,"預算不得為0", Toast.LENGTH_LONG).show();
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
        budgetEdit.setText(settingData.getString("buget","0"));
    }
}