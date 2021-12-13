package com.example.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences accountData;
    private GoogleDriveService mGoogleDriveService = new GoogleDriveService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_google);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.create_button).setOnClickListener(this);
        findViewById(R.id.LogOut).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Intent clientIntent = mGoogleDriveService.signIn(this);
                startActivityForResult(clientIntent, mGoogleDriveService.RC_SIGN_IN);
                mGoogleDriveService.requestStoragePremission(this);
                break;
            case R.id.LogOut:
                mGoogleDriveService.logOut();
                break;
            // ...
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mGoogleDriveService.RC_SIGN_IN && resultCode == RESULT_OK && data != null && data.getExtras() != null) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            accountData = getSharedPreferences("Account_config", MODE_PRIVATE);
            mGoogleDriveService.handleSignInIntent(task, accountData);
            Toast.makeText(this, "登入成功!!", Toast.LENGTH_SHORT).show();
        }
    }


//    public String getAccountEmail(){
//        return accountData.getString("email", "xxx@gmail.com");
//    }
//    public String getAccountdisplayName(){
//        return accountData.getString("displayName", "XXX-XXX");
//    }
}
