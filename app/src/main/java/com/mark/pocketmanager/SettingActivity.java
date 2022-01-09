package com.mark.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.mark.pocketmanager.R;

public class SettingActivity extends AppCompatActivity {
    private Boolean isLogIn = false;

    private GoogleDriveService mGDS = new GoogleDriveService();

    private SharedPreferences accountData;
    Button connectGoogle,handBackup,autoBackup,income,expenditure,property,remind;
    Switch noticeSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences preferences;
    //boolean notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        accountData = this.getSharedPreferences("Account_Data", MODE_PRIVATE);
        //使用 accountData.getString(INPUTA, INPUTB) 回傳email(String 型態)
        // INPUTA 是keyword,可以是 email, givenName, displayName
        //INPUTB 是預設的文字

        connectGoogle = findViewById(R.id.connectGoogle);
        connectGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogIn){
                    mGDS.logOut();
                    isLogIn = false;
                    Toast.makeText(SettingActivity.this, "登出", Toast.LENGTH_SHORT);
                    connectGoogle.setText("連結帳號");
                }else {
                    Log.i("onclick", "start sign in");
                    Intent intent = mGDS.getSignInIntent(SettingActivity.this);
                    startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
                }
            }
        });
        autoBackup = findViewById(R.id.autoBackup);
        autoBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, BackUpActivity.class);
                startActivity(intent);
            }
        });
        income = findViewById(R.id.income);
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        expenditure = findViewById(R.id.expenditure);
        expenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        remind = findViewById(R.id.remind);
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, Buget.class);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        //preferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);

        //boolean Remind = preferences.getBoolean("remind",false);
        //remindSwitch.setChecked(Remind);




        noticeSwitch=findViewById(R.id.noticeSwitch);
        noticeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean listener = noticeSwitch.isChecked();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("listener",listener);
                editor.apply();
            }
        });
        noticeSwitch.setChecked(sharedPreferences.getBoolean("listener",false));

    }

    public void intentBuget(){
//        Intent intent = new Intent();
//        intent.setClass(SettingActivity.this, ExpenditureThreshold.class);
//        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case GoogleDriveService.RC_SIGN_IN:
                if(mGDS.handleSignInResult(data , SettingActivity.this)){
                    Toast.makeText(SettingActivity.this, "登入成功", Toast.LENGTH_SHORT);
                    Log.i("sign in", "Sign in success");

//                    mGDS.deleteAllBackupFromDrive(SettingActivity.this);
//                    mGDS.backUpToDrive(SettingActivity.this);
//                    mGDS.restoreFileFromDrive(SettingActivity.this);

                    accountData = mGDS.setAccountData(accountData);
                    mGDS.requestStoragePremission(this);
                    connectGoogle.setText(accountData.getString("email", "已登入"));

//                    File tfile  = Environment.getDataDirectory();
//                    Log.i("root", tfile.getAbsolutePath());
                    isLogIn = true;
                }
                else{
                    isLogIn = false;
                    Toast.makeText(SettingActivity.this, "登入失敗", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}