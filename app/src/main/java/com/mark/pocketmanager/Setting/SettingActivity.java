package com.mark.pocketmanager.Setting;

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

    private SharedPreferences googleDriveData;
    Button connectGoogle,handBackup,autoBackup,income,expenditure,property,remind, handbutton, handrestore;
    Switch noticeSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences preferences;
    //boolean notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        googleDriveData = this.getSharedPreferences("Account_Data", MODE_PRIVATE);
        //使用 accountData.getString(INPUTA, INPUTB) 回傳email(String 型態)
        // INPUTA 是keyword,可以是 email, givenName, displayName
        // INPUTB 是預設的文字

        connectGoogle = findViewById(R.id.connectGoogle);

        //判斷是否已經登入，若登入則自動點擊連結帳號button
        if(mGDS.ifConnected() && ifLogInBefore()){
            //有登入數據，也已經登入
            String userEmail = googleDriveData.getString("email","已登入");
            connectGoogle.setText(userEmail);
        }else if(ifLogInBefore() && !mGDS.ifConnected()){
            //曾經登入過（有email紀錄），但沒有登入數據
            Log.w("auto","自動登入");
            Intent intent = mGDS.getSignInIntent(SettingActivity.this);
            startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
        }else{
            Log.w("auto","完全登出");
            //登出了（沒有email紀錄），也沒有登入數據
            connectGoogle.setText("連結帳號");
        }
        connectGoogle.setOnClickListener(v -> {
            Log.i("ifLogInBefore", ifLogInBefore().toString());
            if(ifLogInBefore() && mGDS.ifConnected()){
                mGDS.logOut();
                mGDS.clearAccountData(googleDriveData);
                updateIsLogIn(false);
                Toast.makeText(SettingActivity.this, "登出", Toast.LENGTH_SHORT).show();
                connectGoogle.setText("連結帳號");
            }else {
                Log.i("onclick", "start sign in");
                Intent intent = mGDS.getSignInIntent(SettingActivity.this);
                startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
            }
        });
        handbutton = findViewById(R.id.handbackup);
        handbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ifLogInBefore()){
                    Toast.makeText(SettingActivity.this, "請先登入", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    mGDS.backUpToDrive(SettingActivity.this);
                }
            }
        });
        handrestore = findViewById(R.id.handrestore);
        handrestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ifLogInBefore()){
                    Toast.makeText(SettingActivity.this, "請先登入", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    mGDS.restoreFileFromDrive(SettingActivity.this);
                }
            }
        });
        autoBackup = findViewById(R.id.autoBackup);
        autoBackup.setOnClickListener(v -> {
            if (!ifLogInBefore()){
                Toast.makeText(SettingActivity.this, "請先登入", Toast.LENGTH_SHORT).show();
                return;
            }else {
                mGDS.deleteAllBackupFromDrive(SettingActivity.this);
            }
//            Intent intent = new Intent();
//            intent.setClass(SettingActivity.this, BackUpActivity.class);
//            startActivity(intent);
        });
        income = findViewById(R.id.income);
        income.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type","收入");
            intent.setClass(SettingActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        expenditure = findViewById(R.id.expenditure);
        expenditure.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type","支出");
            intent.setClass(SettingActivity.this, CategoryActivity.class);
            startActivity(intent);
        });

        remind = findViewById(R.id.remind);
        remind.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(SettingActivity.this, Budget.class);
            startActivity(intent);
        });

        sharedPreferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        //preferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);

        //boolean Remind = preferences.getBoolean("remind",false);
        //remindSwitch.setChecked(Remind);


        noticeSwitch=findViewById(R.id.noticeSwitch);
        noticeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean listener = noticeSwitch.isChecked();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("listener",listener);
            editor.apply();
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
                    googleDriveData = mGDS.setAccountData(googleDriveData);
                    String userEmail = googleDriveData.getString("email","已登入");
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
        SharedPreferences.Editor editor = googleDriveData.edit();
        editor.putBoolean("isLogIn", TorF);
        editor.apply();
    }
    private Boolean ifLogInBefore(){
        return googleDriveData.getBoolean("isLogIn", false);
    }
}