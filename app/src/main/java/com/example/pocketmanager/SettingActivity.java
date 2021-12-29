package com.example.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;

public class SettingActivity extends AppCompatActivity {
    //if user is login
    private Boolean isLogIn = false;

    private GoogleDriveService mGDS = new GoogleDriveService();

    private SharedPreferences accountData;

    Button google_button,hand_backup,auto_backup,edit_category,property;
    Switch remind_switch,speak_switch,notice_switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        accountData = this.getSharedPreferences("Account_Data", MODE_PRIVATE);
        //使用 accountData.getString(INPUTA, INPUTB) 回傳email(String 型態)
        // INPUTA 是keyword,可以是 email, givenName, displayName
        //INPUTB 是預設的文字

        auto_backup = findViewById(R.id.auto_backup);
        auto_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, BackUpActivity.class);
                startActivity(intent);
            }
        });
        edit_category = findViewById(R.id.edit_catogory);
        edit_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
        property = findViewById(R.id.property);
        property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, PropertyActivity.class);
                startActivity(intent);
            }
        });
        google_button = findViewById(R.id.google_button);
        google_button.setText("連結帳號");
        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogIn){
                    mGDS.logOut();
                    isLogIn = false;
                    Toast.makeText(SettingActivity.this, "登出", Toast.LENGTH_SHORT);
                    google_button.setText("連結帳號");
                }else {
                    Intent intent = mGDS.getSignInIntent(SettingActivity.this);
                    startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
                }
            }
        });

        remind_switch=findViewById(R.id.remind_switch);
        remind_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
                else{

                }
            }
        });
        speak_switch=findViewById(R.id.speak_switch);
        speak_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
                else{

                }
            }
        });
        notice_switch=findViewById(R.id.notice_switch);
        notice_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
                else{

                }
            }
        });
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
                    google_button.setText(accountData.getString("email", "已登入"));

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