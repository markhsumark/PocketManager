package com.mark.pocketmanager.Setting;

import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.util.ArrayList;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveUtil {


//    會改為資料庫的路徑
    private static String rootPath = Environment.getDataDirectory().getPath();
    private static String appFolderPath = "/data/com.example.pocketmanager/databases/";
    private static String testFolderRootPath = "/data/com.example.pocketmanager/shared_prefs/";
//    會改為DB file name
    private static String dbFileName = "account_database";
    private static String testFileName = "Account_Data.xml";

    private static String mineType = "*/*";
    private static String testMineType = "plain/xml";

    public static String createFileToDrive(Drive driveService){
        String fileId;
        //  create new file object
        File fileMetadata = new File();
        //  fileMetadata.setName(dbFileName);
        fileMetadata.setName(testFileName);

        //  設定為上傳到app專用的Drive目錄
        //  ileMetadata.setParents(Collections.singletonList("appDataFolder"));

        //  實際檔案位置
        //  java.io.File filePath = new java.io.File(appFolderRootPath + dbFileName);
        java.io.File filePath = new java.io.File(rootPath + testFolderRootPath + testFileName);
        //  設定檔案內容
        FileContent mediaContent = new FileContent(testMineType, filePath);
        //  type表格：https://blog.csdn.net/github_35631540/article/details/103228868
        Log.i("Createfile: ", "create start");
        try {
            //  新增檔案，要求：檔案資訊和檔案內容
            File file = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            fileId = file.getId();
            Log.i("File ID: ", file.getId());
            Log.i("Createfile: ", "create successfully");
        } catch (Exception e) {
            fileId = null;
            Log.e("err when create file", e.getMessage());
        }
        return fileId;
    }

    public static void uploadFileToDrive(Drive driveService, String fileId){
        try {
            //  create new file object
            File fileMetadata = new File();
            //  fileMetadata.setName(dbFileName);
            fileMetadata.setName(testFileName);

            //  設定為上傳到app專用的Drive目錄
            //  fileMetadata.setParents(Collections.singletonList("appDataFolder"));

            //  實際檔案位置
            //  java.io.File filePath = new java.io.File(appFolderRootPath + dbFileName);
            java.io.File filePath = new java.io.File(rootPath +testFolderRootPath + testFileName);
            //  設定檔案內容
            FileContent mediaContent = new FileContent(testMineType, filePath);
            //  新增檔案，要求：檔案資訊和檔案內容
            File file = driveService.files().update(fileId, fileMetadata, mediaContent)
                    .execute();
            Log.i("Uploadfile: ", "upload to :"+file.getId());
            Log.i("Uploadfile: ", "upload successfully");
        } catch (Exception e) {
            fileId = null;
            Log.e("err when upload file", e.getMessage());
        }
    }
    public static void downloadFileFromDrive(Drive driveService, String fileId){
        Thread thr = new Thread(){
            @Override
            public void run(){
                try {
                    if(fileId != null) {

                        OutputStream outputStream = new ByteArrayOutputStream();
                        driveService.files().get(fileId)
                                .executeMediaAndDownloadTo(outputStream);
                        Log.i("download file context", outputStream.toString());

                        java.io.File dbFile = new java.io.File(rootPath + testFolderRootPath + testFileName);
                        FileOutputStream dbFileOutputStream = new FileOutputStream(dbFile);
                        dbFileOutputStream.write(outputStream.toString().getBytes());
                        dbFileOutputStream.close();
                        Log.i("download", "finish");
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
    public static void deleteFileFromDrive(Drive driveService, String fileId){
        try {
            driveService.files().delete(fileId)
                    .execute();
        }catch(IOException e){
            Log.e("err when delete file", e.getMessage());
        }
    }
    public static ArrayList<String> searchFileFromDrive(Drive driveService){
        if(driveService == null){
            Log.e("searching", "driveService is null");
        }
        FileList result;
        String id;
        try {
            Log.i("searching", "start");
            result = driveService.files().list()
                    .setQ("name ='"+testFileName+"'")
                    .setSpaces("drive")
//                    .setSpaces("appDataFolder")
                    .setFields("files(id, name)")
                    .execute();
        } catch (Exception e) {
            Log.e("Error in searchFile", e.getMessage());
            return null;
        }
        Log.i("searching", "end");
        if(result.getFiles().isEmpty()){
            return null;
        }
        else{
            ArrayList<String> ids = new ArrayList<String>();
            for (File file : result.getFiles()) {
                System.out.printf("Found file: %s (%s)\n",
                        file.getName(), file.getId());
                ids.add(file.getId());
            }
            return ids;
        }
    }
}

