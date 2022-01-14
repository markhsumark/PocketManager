package com.mark.pocketmanager.Setting;

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

import java.util.ArrayList;
import java.util.Collections;

public class GoogleDriveService {

    public static final int RC_SIGN_IN = 400;
    private GoogleSignInClient client;
    private GoogleSignInAccount account;

    private Drive driveService;

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
//                .requestIdToken("1013631286690-3gfib9ndh7r8t7snq6k98ukc4p814119.apps.googleusercontent.com")
                .requestProfile()
                .build();

        client = GoogleSignIn.getClient(activity, gso);
        return client.getSignInIntent();
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
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(googleSignInAccount -> {

                        GoogleAccountCredential credential = GoogleAccountCredential
                                .usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE));
                        credential.setSelectedAccount(googleSignInAccount.getAccount());

                        driveService = new Drive.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName("PocketManager")
                                .build();
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
            account = task.getResult(ApiException.class);
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            if(e.getStatusCode() == 7){
                Log.i("signin repo", "no internet connect");
                Toast.makeText(context, "失去網路連接", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
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
                editor.apply();
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
            editor.apply();

        }catch(Exception e){
            Log.w("TAG", e);
        }

        return accountData;
    }
    public void backUpToDrive(Context context){
        driveService= getDriveService(context);
        Thread thr = new Thread(() -> {
            for(String filename: GoogleDriveUtil.dbFileNames){
                ArrayList<String> ids = GoogleDriveUtil.searchFileFromDrive(driveService, filename);
                if(ids == null){
                    GoogleDriveUtil.createFileToDrive(driveService, filename);
                }else{
                    GoogleDriveUtil.uploadFileToDrive(driveService, ids.get(0), filename);
                }
            }
        });
        thr.start();
        while(thr.isInterrupted()){
            Toast.makeText(context, "備份失敗", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteAllBackupFromDrive(Context context){
        driveService= getDriveService(context);
        if (!isLogIn(driveService)){
            Toast.makeText(context, "請先登入", Toast.LENGTH_SHORT).show();
            return;
        }
        Thread thr = new Thread(() -> {
            for(String filename: GoogleDriveUtil.dbFileNames) {
                try {
                    ArrayList<String> ids = GoogleDriveUtil.searchFileFromDrive(driveService, filename);
                    for (String s : ids) {
                        Log.i("delete id", s);
                        GoogleDriveUtil.deleteFileFromDrive(driveService, s);
                    }
                } catch (NullPointerException e) {
                    Log.w("delete id", "there isn't exist file");
                }
            }

        });
        thr.start();
        while(thr.isInterrupted()){
            Toast.makeText(context, "刪除失敗", Toast.LENGTH_SHORT).show();
        }

    }
    public void restoreFileFromDrive(Context context){
        driveService= getDriveService(context);
        if (!isLogIn(driveService)){
            Toast.makeText(context, "請先登入", Toast.LENGTH_SHORT).show();
            return;
        }
        Thread thr = new Thread(() -> {
            for(String filename: GoogleDriveUtil.dbFileNames) {
                ArrayList<String> ids = GoogleDriveUtil.searchFileFromDrive(driveService, filename);
                try {
                    Log.i("download id", ids.get(0));
                    GoogleDriveUtil.downloadFileFromDrive(driveService, ids.get(0), filename);
                } catch (NullPointerException e) {
                    Log.w("delete id", "there isn't exist file");
                }
            }
        });
        thr.start();
    }
    private Boolean isLogIn(Drive drive){
        return drive != null;
    }
    private Drive getDriveService(Context context){
        GoogleAccountCredential credential = GoogleAccountCredential
                .usingOAuth2(context, Collections.singleton(DriveScopes.DRIVE_FILE));
        if(account == null) {
            Log.e("getDrive", "account is null");
            return null;
        }
        credential.setSelectedAccount(account.getAccount());

        driveService = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("PocketManager")
                .build();
        return driveService;
    }
    public Boolean ifConnected(){
        return client != null;
    }
}
