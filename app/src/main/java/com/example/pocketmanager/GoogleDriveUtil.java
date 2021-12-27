package com.example.pocketmanager;

import android.util.Log;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.io.OutputStream;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class GoogleDriveUtil {

//    會改為資料庫的路徑
    private static String appFolderRootPath = "data/data/com.example.pocketmanager/databases/";
//    會改為DB file name
    private static String dbFileName = "account_database";
    private String fileId;

//    傳入參數為：上傳的目的地drive 和上傳的file名稱
    public void createFileToDrive(Drive driveService, String fileName){
        Thread thr = new Thread(){
            @Override
            public void run(){
//                create new file object
                File fileMetadata = new File();
                fileMetadata.setName(dbFileName);

//                設定為上傳到app專用的Drive目錄
//                fileMetadata.setParents(Collections.singletonList("appDataFolder"));

//                實際檔案位置
                java.io.File filePath = new java.io.File(appFolderRootPath +dbFileName);
//                設定檔案內容
                FileContent mediaContent = new FileContent("*/*", filePath);
//                type表格：https://blog.csdn.net/github_35631540/article/details/103228868

                try {
//                    新增檔案，要求：檔案資訊和檔案內容
                    File file = driveService.files().create(fileMetadata, mediaContent)
                            .setFields("id")
                            .execute();
                    fileId = file.getId();

                    Log.i("File ID: " , file.getId());
                }catch(IOException e){
                    Log.e("err when create file",e.getMessage());
                }
            }
        };
        thr.start();
    }
//  未測試
    public void updateFileToDrive(Drive driveService){
        Thread thr = new Thread(){
            @Override
            public void run(){
                OutputStream outputStream = new ByteArrayOutputStream();
                //create a new file
                File fileMetadata = new File();

                //set "new" file property
//                fileMetadata.setName(fileName);

                //get media file's content
                java.io.File filePath = new java.io.File(appFolderRootPath+dbFileName);
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
    public void downloadFileFromDrive(Drive driveService, String fileId){
        Thread thr = new Thread(){
                    @Override
                    public void run(){
                        try {
                            if(fileId != null) {
                                // image/jpeg：jpg檔
                                File file = driveService.files().get(fileId)
                                        .execute();
                                Log.i("info", "download success");
                            }
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
    public void setFileId(String id){
        fileId = id;
    }
    public String getFileId(){
        return fileId;
    }

}

