package com.example.pocketmanager;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

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
import com.google.api.services.drive.DriveScopes;

public class GoogleDriveService {

    public static final int RC_SIGN_IN = 400;
    private GoogleSignInClient client;

    public Intent signIn(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                        new Scope(DriveScopes.DRIVE_APPDATA))
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
        if(client != null){
            client.signOut();
        }
    }
    public void handleSignInIntent(Task<GoogleSignInAccount> completedTask, SharedPreferences accountData) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            SharedPreferences.Editor editor = accountData.edit();
            editor.putString("email", account.getEmail());
            editor.putString("displayName", account.getDisplayName());
            editor.putString("displayPhoto", account.getPhotoUrl().toString());
            editor.commit();
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

}
