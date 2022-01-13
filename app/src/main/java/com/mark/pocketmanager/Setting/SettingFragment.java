package com.mark.pocketmanager.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mark.pocketmanager.R;

public class SettingFragment extends Fragment {
    private Boolean isLogIn = false;

    private GoogleDriveService mGDS = new GoogleDriveService();

    private SharedPreferences googleDriveData;
    Button connectGoogle,handBackup,autoBackup,income,expenditure,property,remind, handbutton;
    Switch noticeSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences preferences;
    //boolean notification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        googleDriveData = getContext().getSharedPreferences("GoogleDrive_Data", MODE_PRIVATE);
        //使用 accountData.getString(INPUTA, INPUTB) 回傳email(String 型態)
        // INPUTA 是keyword,可以是 email, givenName, displayName
        // INPUTB 是預設的文字

        connectGoogle = view.findViewById(R.id.connectGoogle);
        //判斷是否已經登入，若曾經登入則自動登入一次，未登入就顯示「連結帳號」
        if(getIsLogIn()){
            Intent intent = mGDS.getSignInIntent(this.getActivity());
            startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
        }else{
            connectGoogle.setText("連結帳號");
        }
        connectGoogle.setOnClickListener(v -> {
            if(getIsLogIn()){
                mGDS.logOut();
                updateIsLogIn(false);
                Toast.makeText(getContext(), "登出", Toast.LENGTH_SHORT).show();
                connectGoogle.setText("連結帳號");
            }else {
                Log.i("onclick", "start sign in");
                Intent intent = mGDS.getSignInIntent(SettingFragment.this.getActivity());
                startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
            }
        });

        handbutton = view.findViewById(R.id.handbackup);
        handbutton.setOnClickListener(v -> mGDS.backUpToDrive(SettingFragment.this.getContext()));
        handbutton = view.findViewById(R.id.handrestore);
        handbutton.setOnClickListener(v -> mGDS.restoreFileFromDrive(SettingFragment.this.getContext()));
        autoBackup = view.findViewById(R.id.autoBackup);
        autoBackup.setOnClickListener(v -> {
            mGDS.deleteAllBackupFromDrive(this.getContext());
//            Intent intent = new Intent();
//            intent.setClass(SettingActivity.this, BackUpActivity.class);
//            startActivity(intent);
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
                    googleDriveData = mGDS.setAccountData(googleDriveData);
                    String userEmail = googleDriveData.getString("email","已登入");
                    connectGoogle.setText(userEmail);

                    updateIsLogIn(true);
                    Toast.makeText(SettingFragment.this.getContext(), "已連結帳號"+userEmail, Toast.LENGTH_SHORT).show();
                    Log.i("sign in", "Sign in success");
                    updateIsLogIn(false);
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
        SharedPreferences.Editor editor = googleDriveData.edit();
        editor.putBoolean("isLogIn", TorF);
        editor.apply();
    }
    public Boolean getIsLogIn(){
        return googleDriveData.getBoolean("isLogIn", false);
    }
}