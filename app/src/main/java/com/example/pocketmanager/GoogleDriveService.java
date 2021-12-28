package com.example.pocketmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

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
    private Drive driveService;
    private GoogleSignInClient client;
    private GoogleSignInAccount account;
    private GoogleDriveUtil GDU;
    private GoogleAccountCredential credential;
    private String fileId;

//    return sign in intent
    public Intent getSignInIntent(Activity activity) {
        Log.i("log", "get sign in intent");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE),
                        new Scope(DriveScopes.DRIVE_APPDATA),
                        new Scope(DriveScopes.DRIVE_READONLY),
                        new Scope(DriveScopes.DRIVE_METADATA),
                        new Scope(DriveScopes.DRIVE_SCRIPTS),
                        new Scope(DriveScopes.DRIVE_METADATA_READONLY),
                        new Scope(DriveScopes.DRIVE_PHOTOS_READONLY))
                .requestIdToken("1013631286690-5pdqknoqqql0o0ntdjbvmo5l04dc8s70.apps.googleusercontent.com")
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
        client.signOut();
        Log.i("isLogOut?", "yes!");
    }
//    handle the result of sign in intent, and reset account data
    public Boolean handleSignInResult(Intent data, Context context) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            account = task.getResult(ApiException.class);
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(googleSignInAccount -> {
                        // https://www.codenong.com/22142641/
                        credential = getCredential(context);
                        credential.setSelectedAccount(googleSignInAccount.getAccount());
                        driveService = getDrive();
                        GDU = new GoogleDriveUtil();
                        backUpToDrive();
                    })
                    .addOnFailureListener(e -> e.printStackTrace());
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            if(e.getStatusCode() == 7){
                Log.i("signin repo", "no internet connect");
            }
            return false;
        }
        return true;
    }
    private GoogleAccountCredential getCredential(Context context){
        return GoogleAccountCredential
                .usingOAuth2( context, Collections.singleton(DriveScopes.DRIVE_FILE));
    }
    private Drive getDrive(){
        return new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("PocketManager")
                .build();
    }

//    return account info to Activity
    public SharedPreferences setAccountData(SharedPreferences accountData){
        try{
            if(account != null) {
                SharedPreferences.Editor editor = accountData.edit();
                editor.putString("email", account.getEmail());
                editor.putString("giveName", account.getGivenName());
                editor.putString("displayName", account.getDisplayName());
//                editor.putString("displayPhoto", account.getPhotoUrl().toString());
                editor.commit();
            }
        }catch(Exception e){
            Log.w("TAG", e);
        }

        return accountData;
    }
//    clear account info form Activity
    public SharedPreferences clearAccountData(SharedPreferences accountData){
        try{
            SharedPreferences.Editor editor = accountData.edit();
            editor.clear();
            editor.commit();

        }catch(Exception e){
            Log.w("TAG", e);
        }

        return accountData;
    }
    public void backUpToDrive(){
        if(this.driveService == null){
            Log.e("b", "driveService is null");
        }
        GDU.createFileToDrive(driveService);
    }
    public void restoreFromDrive(){
        if(driveService== null) {
            Log.e("r", "driveService is null");
        }

    }

}
