package com.mark.pocketmanager.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.mark.pocketmanager.R;

public class SettingFragment extends Fragment {

    private Boolean isLogIn = false;

    private GoogleDriveService mGDS = new GoogleDriveService();

    private SharedPreferences accountData;
    Button connectGoogle,handBackup,autoBackup,income,expenditure,property,remind;
    Switch noticeSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences preferences;
    //boolean notification;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        accountData = this.getContext().getSharedPreferences("Account_Data", MODE_PRIVATE);
        //使用 accountData.getString(INPUTA, INPUTB) 回傳email(String 型態)
        // INPUTA 是keyword,可以是 email, givenName, displayName
        // INPUTB 是預設的文字

        connectGoogle = view.findViewById(R.id.connectGoogle);

        //判斷是否已經登入，若登入則顯示email，未登入就顯示「連結帳號」
        if(getIsLogIn()){
            String userEmail = accountData.getString("email","");
            connectGoogle.setText(userEmail);
        }else{
            connectGoogle.setText("連結帳號");
        }
        connectGoogle.setOnClickListener(v -> {
            if(getIsLogIn()){
                mGDS.logOut();
                updateIsLogIn(false);
                Toast.makeText(SettingFragment.this.getContext(), "登出", Toast.LENGTH_SHORT).show();
                connectGoogle.setText("連結帳號");
            }else {
                Log.i("onclick", "start sign in");
                Intent intent = mGDS.getSignInIntent(SettingFragment.this.getActivity());
                startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
            }
        });
        autoBackup = view.findViewById(R.id.autoBackup);
        autoBackup.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(SettingFragment.this.getContext(), BackUpActivity.class);
            startActivity(intent);
        });
        income = view.findViewById(R.id.income);
        income.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type","收入");
            intent.setClass(SettingFragment.this.getContext(), CategoryActivity.class);
            startActivity(intent);
        });

        expenditure = view.findViewById(R.id.expenditure);
        expenditure.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type","支出");
            intent.setClass(SettingFragment.this.getContext(), CategoryActivity.class);
            startActivity(intent);
        });

        remind = view.findViewById(R.id.remind);
        remind.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(SettingFragment.this.getContext(), Budget.class);
            startActivity(intent);
        });

        sharedPreferences = getContext().getSharedPreferences("SHARED_PREF",MODE_PRIVATE);
        //preferences = getSharedPreferences("SHARED_PREF",MODE_PRIVATE);

        //boolean Remind = preferences.getBoolean("remind",false);
        //remindSwitch.setChecked(Remind);


        noticeSwitch=view.findViewById(R.id.noticeSwitch);
        noticeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean listener = noticeSwitch.isChecked();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("listener",listener);
            editor.apply();
        });
        noticeSwitch.setChecked(sharedPreferences.getBoolean("listener",false));

        return view;
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
                if(mGDS.handleSignInResult(data , SettingFragment.this.getContext())){
                    mGDS.requestStoragePremission(this.getActivity());
//                    mGDS.deleteAllBackupFromDrive(SettingActivity.this);
//                    mGDS.backUpToDrive(SettingActivity.this);
//                    mGDS.restoreFileFromDrive(SettingActivity.this);
                    accountData = mGDS.setAccountData(accountData);
                    String userEmail = accountData.getString("email","已登入");
                    connectGoogle.setText(userEmail);


                    Toast.makeText(SettingFragment.this.getContext(), "已連結帳號"+userEmail, Toast.LENGTH_SHORT).show();
                    Log.i("sign in", "Sign in success");
                    updateIsLogIn(true);
                }
                else{
                    Toast.makeText(SettingFragment.this.getContext(), "登入失敗", Toast.LENGTH_SHORT).show();
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