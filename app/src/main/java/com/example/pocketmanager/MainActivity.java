package com.example.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 400;
    private GoogleSignInClient client;
    private SharedPreferences accountData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_google);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.create_button).setOnClickListener(this);
        findViewById(R.id.LogOut).setOnClickListener(this);
//        updateUI(account);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                requestStoragePremission();
                break;
            case R.id.LogOut:
                logOut();
                break;
            // ...
        }
    }
    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE_APPDATA))
                .requestIdToken("1013631286690-5pdqknoqqql0o0ntdjbvmo5l04dc8s70.apps.googleusercontent.com")
                .requestProfile()
                .build();

        client = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void requestStoragePremission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK && data != null && data.getExtras() != null) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInIntent(task);
            Toast.makeText(this, "登入成功!!", Toast.LENGTH_SHORT).show();
        }
    }
    public void logOut(){
        if(client != null){
            client.signOut();
            Toast.makeText(this, "登出成功!!", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleSignInIntent(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            accountData = getSharedPreferences("Account_config", MODE_PRIVATE);
            SharedPreferences.Editor editor = accountData.edit();
            editor.putString("email", account.getEmail());
            editor.putString("displayName", account.getDisplayName());
            editor.putString("displayPhoto", account.getPhotoUrl().toString());
            editor.commit();
        } catch (ApiException e) {
            Toast.makeText(this, "登入失敗", Toast.LENGTH_SHORT).show();
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public String getAccountEmail(){
        return accountData.getString("email", "xxx@gmail.com");
    }
    public String getAccountdisplayName(){
        return accountData.getString("displayName", "XXX-XXX");
    }
}