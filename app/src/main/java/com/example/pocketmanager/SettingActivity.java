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

import com.google.android.gms.common.SignInButton;

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

        //判斷是否已經登入，若登入則顯示email，未登入就顯示「連結帳號」
        if(getIsLogIn()){
            String userEmail = accountData.getString("email","");
            connectGoogle.setText(userEmail);
        }else{
            connectGoogle.setText("連結帳號");
        }
        connectGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIsLogIn()){
                    mGDS.logOut();
                    updateIsLogIn(false);
                    Toast.makeText(SettingActivity.this, "登出", Toast.LENGTH_SHORT).show();
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
                    mGDS.requestStoragePremission(this);
//                    mGDS.deleteAllBackupFromDrive(SettingActivity.this);
//                    mGDS.backUpToDrive(SettingActivity.this);
//                    mGDS.restoreFileFromDrive(SettingActivity.this);
                    accountData = mGDS.setAccountData(accountData);
                    String userEmail = accountData.getString("email","已登入");
                    connectGoogle.setText(userEmail);


                    Toast.makeText(SettingActivity.this, "已連結帳號"+userEmail, Toast.LENGTH_SHORT).show();
                    Log.i("sign in", "Sign in success");
                    updateIsLogIn(true);
                }
                else{
                    Toast.makeText(SettingActivity.this, "登入失敗", Toast.LENGTH_SHORT).show();
                    updateIsLogIn(false);
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void updateIsLogIn(Boolean TorF){
        SharedPreferences.Editor editor = accountData.edit();
        editor.putBoolean("isLogIn", TorF);
        editor.apply();
    }
    public Boolean getIsLogIn(){
        return accountData.getBoolean("isLogIn", false);
    }
}