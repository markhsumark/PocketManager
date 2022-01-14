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
    private final GoogleDriveService mGDS = new GoogleDriveService();

    private SharedPreferences googleDriveData;
    Button connectGoogle,autoBackup,income,expenditure,remind, handbutton, handrestore;
    Switch noticeSwitch;
    SharedPreferences sharedPreferences;
    //boolean notification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        googleDriveData = this.getContext().getSharedPreferences("GoogleDrive_Data", MODE_PRIVATE);
        //使用 accountData.getString(INPUTA, INPUTB) 回傳email(String 型態)
        // INPUTA 是keyword,可以是 email, givenName, displayName
        // INPUTB 是預設的文字

        connectGoogle = view.findViewById(R.id.connectGoogle);

        //判斷是否已經登入，若登入則自動點擊連結帳號button
        if(mGDS.ifConnected() && ifLogInBefore()){
            //有登入數據，也已經登入
            String userEmail = googleDriveData.getString("email","已登入");
            connectGoogle.setText(userEmail);
        }else if(ifLogInBefore() && !mGDS.ifConnected()){
            //曾經登入過（有email紀錄），但沒有登入數據
            Log.w("auto","自動登入");
            Intent intent = mGDS.getSignInIntent(this.getActivity());
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
                Toast.makeText(this.getContext(), "登出", Toast.LENGTH_SHORT).show();
                connectGoogle.setText("連結帳號");
            }else {
                Log.i("onclick", "start sign in");
                Intent intent = mGDS.getSignInIntent(this.getActivity());
                startActivityForResult(intent, GoogleDriveService.RC_SIGN_IN);
            }
        });
        handbutton = view.findViewById(R.id.handbackup);
        handbutton.setOnClickListener(v -> {
            if (!ifLogInBefore()){
                Toast.makeText(this.getContext(), "請先登入", Toast.LENGTH_SHORT).show();
            }else {
                mGDS.backUpToDrive(this.getContext());
            }
        });
        handrestore = view.findViewById(R.id.handrestore);
        handrestore.setOnClickListener(v -> {
            if (!ifLogInBefore()){
                Toast.makeText(this.getContext(), "請先登入", Toast.LENGTH_SHORT).show();
            }else {
                mGDS.restoreFileFromDrive(this.getContext());
            }
        });
        autoBackup = view.findViewById(R.id.autoBackup);
        autoBackup.setOnClickListener(v -> {
            if (!ifLogInBefore()){
                Toast.makeText(this.getContext(), "請先登入", Toast.LENGTH_SHORT).show();
            }else {
                mGDS.deleteAllBackupFromDrive(this.getContext());
            }
//            Intent intent = new Intent();
//            intent.setClass(SettingActivity.this, BackUpActivity.class);
//            startActivity(intent);
        });
        income = view.findViewById(R.id.income);
        income.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type","收入");
            intent.setClass(this.getContext(), CategoryActivity.class);
            startActivity(intent);
        });

        expenditure = view.findViewById(R.id.expenditure);
        expenditure.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type","支出");
            intent.setClass(this.getContext(), CategoryActivity.class);
            startActivity(intent);
        });

        remind = view.findViewById(R.id.remind);
        remind.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this.getContext(), Budget.class);
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
        if (requestCode == GoogleDriveService.RC_SIGN_IN) {
            if (mGDS.handleSignInResult(data, this.getContext())) {
                mGDS.requestStoragePremission(this.getActivity());
//                    mGDS.deleteAllBackupFromDrive(SettingActivity.this);
//                    mGDS.backUpToDrive(SettingActivity.this);
//                    mGDS.restoreFileFromDrive(SettingActivity.this);
                googleDriveData = mGDS.setAccountData(googleDriveData);
                String userEmail = googleDriveData.getString("email", "已登入");
                connectGoogle.setText(userEmail);

                updateIsLogIn(true);
                Toast.makeText(this.getContext(), "已連結帳號" + userEmail, Toast.LENGTH_SHORT).show();
                Log.i("sign in", "Sign in success");
            } else {
                Toast.makeText(this.getContext(), "登入失敗", Toast.LENGTH_SHORT).show();
                updateIsLogIn(false);
            }
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