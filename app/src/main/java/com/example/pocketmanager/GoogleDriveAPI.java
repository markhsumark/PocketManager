package com.example.pocketmanager;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.Collections;

public class GoogleDriveAPI {
    private static final String TAG = "Log:";
    private static final int REQUEST_CODE_SIGN_IN = 400;

    private void requestSignIn() {
        //GoogleSignInOptions Doc :https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInOptions.Builder?hl=zh-tw
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        //獲取使用者的email address
                        .requestEmail()
                        //設定請求範圍：DriveScope Doc:https://developers.google.com/resources/api-libraries/documentation/drive/v2/java/latest/com/google/api/services/drive/DriveScopes.html
                        //DRIVE_APPDATA：檢視和管理使用者的GoogleDrive的配置
                        //DRIVE_FILE：檢視和管理APP在GoogleDrive開啟和新增的檔案和資料夾
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE),
                                new Scope(DriveScopes.DRIVE_APPDATA))
                        .requestIdToken("722xxxxxxxxxxxxxxxxxxxxxxxxxxxxx9ks.apps.googleusercontent.com")//創作者的token
                        .build();
        //https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInClient
        GoogleSignInClient client = GoogleSignIn.getClient(getActivity(), signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        //畫面會跳出讓使用者同意授權的 Dialog
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);

    }
    //要回到主頁面時，會把需要傳回的資料放在intent，再執行此method
    //https://developer.android.com/training/basics/intents/result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                handleSignInResult(data);
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d(TAG, "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    getActivity(), Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("AppName")
                                    .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper();
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Unable to sign in.", exception);
                });
    }
}