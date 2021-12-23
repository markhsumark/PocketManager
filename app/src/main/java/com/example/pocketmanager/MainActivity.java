package com.example.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences accountData;
    private GoogleDriveService mGoogleDriveService = new GoogleDriveService();
    private GoogleDriveUtil mGoogleDriveUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_google);
        accountData = getSharedPreferences("Account_config", MODE_PRIVATE);;
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.upload_button).setOnClickListener(this);
        findViewById(R.id.download_button).setOnClickListener(this);
        findViewById(R.id.LogOut).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        TextView text = (TextView)findViewById(R.id.givenname);
        switch (v.getId()) {
            case R.id.sign_in_button:
                Intent clientIntent = mGoogleDriveService.getSignInIntent(this);
                startActivityForResult(clientIntent, mGoogleDriveService.RC_SIGN_IN);
                mGoogleDriveService.requestStoragePremission(this);
                break;
            case R.id.LogOut:
                mGoogleDriveService.logOut();
                accountData = mGoogleDriveService.clearAccountData(accountData);
                refreshView();
                break;
            case R.id.upload_button:
                mGoogleDriveService.backUpToDrive();
                break;
            case R.id.download_button:
                mGoogleDriveService.restoreFromDrive();
                break;

            // ...
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mGoogleDriveService.RC_SIGN_IN) {
            mGoogleDriveService.handleSignInResult(data);
            accountData = mGoogleDriveService.setAccountData(accountData);
            mGoogleDriveService.setDrive(data, this);
            refreshView();
        }
        else{
            Log.i("processing LogIn", "fail!"+ requestCode + ", " + resultCode+ " ," + RESULT_OK);
        }

    }
    public void refreshView(){
        TextView text = (TextView)findViewById(R.id.givenname);
        text.setText(accountData.getString("givenname", "未登入"));
    }
}
