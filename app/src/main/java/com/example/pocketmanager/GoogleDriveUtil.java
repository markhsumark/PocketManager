package com.example.pocketmanager;

import android.util.Log;

import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.io.OutputStream;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveUtil {

//    會改為資料庫的路徑
    private static String appFolderRootPath = "data/data/com.example.pocketmanager/databases/";
    private static String testFolderRootPath = "data/data/com.example.pocketmanager/shared_prefs/";
//    會改為DB file name
    private static String dbFileName = "account_database";
    private static String testFileName = "Account_Data.xml";

    private static String mineType = "*/*";
    private static String testMineType = "plain/xml";
    private String fileId;

//    傳入參數為：上傳的目的地drive 和上傳的file名稱
    public void createFileToDrive(Drive driveService){
//                create new file object
        File fileMetadata = new File();
//        fileMetadata.setName(dbFileName);
        fileMetadata.setName(testFileName);

        //                設定為上傳到app專用的Drive目錄
        //                fileMetadata.setParents(Collections.singletonList("appDataFolder"));

        //                實際檔案位置
//        java.io.File filePath = new java.io.File(appFolderRootPath + dbFileName);
        java.io.File filePath = new java.io.File(testFolderRootPath + testFileName);
        //                設定檔案內容
        FileContent mediaContent = new FileContent(testMineType, filePath);
        //                type表格：https://blog.csdn.net/github_35631540/article/details/103228868
        Thread thr = new Thread(){
            @Override
            public void run(){
                try {
                    //                    新增檔案，要求：檔案資訊和檔案內容
                    File file = driveService.files().create(fileMetadata, mediaContent)
                            .setFields("id")
                            .execute();
                    fileId = file.getId();
                    Log.i("File ID: ", file.getId());
                } catch (IOException e) {
                    fileId = null;
                    Log.e("err when create file", e.getMessage());
                }
            }
        };
        thr.start();
    }

    public static void downloadFileFromDrive(Drive driveService, String fileId){
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
    public static Boolean searchFileFromDrive(Drive driveService){

        String pageToken = null;
        FileList result = new FileList();
        Log.i("", "end searching");
        try {
            do {
                result = driveService.files().list()
                        .setSpaces("drive")
                        //                .setSpaces("appDataFolder")
                        .setQ("mimeType='*/*'")
                        .setFields("nextPageToken, files(id, name)")
                        .setPageToken(pageToken)
                        .execute();

                pageToken = result.getNextPageToken();
            } while (pageToken != null);
            Log.i("","end searching");
        } catch (Exception e) {
            Log.e("Error in searchFile", e.getMessage());
        }
        if(result.isEmpty()){
            return false;
        }else{
            return true;
        }


    }
    public String getID(){
        return fileId;
    }

}

