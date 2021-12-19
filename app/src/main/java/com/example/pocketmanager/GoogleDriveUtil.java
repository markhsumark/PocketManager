package com.example.pocketmanager;

import android.nfc.Tag;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveUtil {

    private Drive driveService;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    GoogleDriveUtil(Drive driveService){
        this.driveService = driveService;
    }

    public Task<String> createFile(String filePath, String fileName)throws IOException{
        return Tasks.call(mExecutor,() ->{
            Log.e("file", filePath+","+fileName );

            File fileMetaData = new File();
            fileMetaData.setName(fileName);

            java.io.File file = new java.io.File(filePath);

            FileContent mediaContent = new FileContent("application/octet-stream",file);

            File myFile = null;
            try{
                myFile = driveService.files().create(fileMetaData,mediaContent)
                        .execute();

                Log.e("File" ,"Flie ID = " +myFile.getId());
            }catch (Exception e){
                e.printStackTrace();
            }

            if (myFile == null){
                throw new IOException("Null result when request file creation");
            }

            return myFile.getId();
        });
    }
    public Task<String> createFolder()throws IOException{
        return Tasks.call(mExecutor, ()->{
            // File's metadata.
            File body = new File();
            body.setName("test");
            body.setMimeType("application/vnd.google-apps.folder");
            File folder = driveService.files().create(body)
                    .setFields("id")
                    .execute();
            return folder.getId();
        });
    }
    public Task<String> searchFile(){
        return Tasks.call(mExecutor,() ->{
            String pageToken = null;

            try{
                do {
                    FileList reset =driveService.files().list()
                            .setQ("'xxxxxxxxxx@gmail.com' in writers")
                            .setSpaces("drive")
                            .setFields("nextPageToken, files(id,name,mimeType)")
                            .setPageToken(pageToken)
                            .execute();
                    for (File file : reset.getFiles()){
                        Log.e("File", "File: "+file.getName()+","+file.getId()+","+file.getMimeType());
                    }
                    pageToken = reset.getNextPageToken();
                }while (pageToken !=null);


            }catch (UserRecoverableAuthIOException e) {
                e.printStackTrace();
                Log.e("err",e.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("err",e.getMessage());

            }
            return null;
        });
    }
}
//    public java.io.File createTestFile(){
//        Log.w("Tag","create s: ");
//        try {
//
//        }catch(Exception e) {
//            Log.i("create file error: ", String.valueOf(e));
//        }
//        return tempFile;
//    }
