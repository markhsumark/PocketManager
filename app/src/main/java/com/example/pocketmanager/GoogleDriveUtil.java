package com.example.pocketmanager;

import android.content.Context;

import android.util.Log;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class GoogleDriveUtil {

    private Drive driveService;
    private String fileId;
    private static String appFolderRootPath = "data/data/com.example.pocketmanager/";

    GoogleDriveUtil(Drive driveService){
        this.driveService = driveService;
    }

    public void createFile(){
        Thread thr = new Thread(){
            @Override
            public void run(){
//                create new file object
                File fileMetadata = new File();
                fileMetadata.setName("Account_config.xml");

//                上傳到app目錄用
//                fileMetadata.setParents(Collections.singletonList("appDataFolder"));

//                實際檔案位置
                java.io.File filePath = new java.io.File(appFolderRootPath +"shared_prefs/Account_config.xml");
//                設定檔案內容
                FileContent mediaContent = new FileContent("text/xml", filePath);
//                type表格：https://developers.google.com/drive/api/v3/ref-export-formats

                try {
//                    新增檔案，要求：檔案資訊和檔案內容
                    File file = driveService.files().create(fileMetadata, mediaContent)
                            .setFields("id")
                            .execute();
                    fileId = file.getId();

                    Log.i("File ID: " , file.getId());
                    Log.i("File ID: " , file.getDescription());
                }catch(IOException e){
                    Log.e("err when create file",e.getMessage());
                }
            }
        };
        thr.start();
    }
//未測試
    public void updateFile(){
        Thread thr = new Thread(){
            @Override
            public void run(){
                OutputStream outputStream = new ByteArrayOutputStream();
                //create a new file
                File fileMetadata = new File();

                //set new file property
                fileMetadata.setName("Account_config.xml");

                //get media file's content
                java.io.File filePath = new java.io.File(appFolderRootPath+"shared_prefs/Account_config.xml");
                FileContent mediaContent = new FileContent("text/xml", filePath);

                try {
                    if(fileId != null) {
                        driveService.files().update(fileId, fileMetadata, mediaContent)
                                .execute();
                    }
                }catch(IOException e){
                    Log.e("Error", e.getMessage());
                }
            }
        };
        thr.start();
    }
    public void downloadFile(){
        Thread thr = new Thread(){
                    @Override
                    public void run(){
                        OutputStream outputStream = new ByteArrayOutputStream();
                        try {
                            if(fileId != null) {
                                // image/jpeg：jpg檔
                                driveService.files().get(fileId)
                                        .executeMediaAndDownloadTo(outputStream);
                            }
                            Log.i("info", "download success");
//                            Log.i("" , file.getDescription());
                        }catch(IOException e){
                            if(e.getMessage() == "416") {
                                Log.e("Error!!the file might empty", e.getMessage());
                            }else{
                                Log.e("Error", e.getMessage());
                            }
                        }
                    }
                };
                thr.start();
    }


}

