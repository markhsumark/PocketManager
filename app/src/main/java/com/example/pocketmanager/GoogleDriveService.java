package com.example.pocketmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.util.Collections;

public class GoogleDriveService {

    public static final int RC_SIGN_IN = 400;
    public Drive driveService;
    private GoogleSignInClient client;
    private GoogleSignInAccount account;
//    private GoogleDriveUtil GUtil;

    public Intent getSignInIntent(Activity activity) {
        Log.i("log", "get sign in intent");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE_APPDATA))
//                .requestIdToken("1013631286690-5pdqknoqqql0o0ntdjbvmo5l04dc8s70.apps.googleusercontent.com")
                .requestProfile()
                .build();

        client = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = client.getSignInIntent();
        return signInIntent;
    }
    public void requestStoragePremission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    public void logOut(){
        if(client != null){
            client.signOut();
            Log.i("isLogOut?", "yes!");
        }
    }
    public void handleSignInResult(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            account = task.getResult(ApiException.class);
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            if(e.getStatusCode() == 7){
                Log.i("signin repo", "no internet connect");
            }

        }
    }
    public void setDrive(Intent data, Context context){
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(googleSignInAccount -> {
                    // https://www.codenong.com/22142641/
                    GoogleAccountCredential credential = GoogleAccountCredential
                            .usingOAuth2( context, Collections.singleton(DriveScopes.DRIVE_FILE));

                    credential.setSelectedAccount(googleSignInAccount.getAccount());

                    drive(credential);
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }
    private void drive(GoogleAccountCredential credential) {
        driveService =
                new Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(),
                        credential)
                        .setApplicationName("PocketManager")
                        .build();
//        GUtil = new GoogleDriveUtil(driveService);
    }
    public SharedPreferences setAccountData(SharedPreferences accountData){
        try{
            if(account != null) {
                SharedPreferences.Editor editor = accountData.edit();
                editor.putString("email", account.getEmail());
                editor.putString("givenname", account.getGivenName());
                editor.putString("displayName", account.getDisplayName());
//                editor.putString("displayPhoto", account.getPhotoUrl().toString());
                editor.commit();
            }
        }catch(Exception e){
            Log.w("TAG", e);
        }

        return accountData;
    }
    public SharedPreferences clearAccountData(SharedPreferences accountData){
        try{
            if(account != null) {
                SharedPreferences.Editor editor = accountData.edit();
                editor.clear();
                editor.commit();
            }
        }catch(Exception e){
            Log.w("TAG", e);
        }

        return accountData;
    }


}
