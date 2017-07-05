//    Copyright (C) 2017 MD. Ibrahim Khan
//
//    Project Name: 
//    Author: MD. Ibrahim Khan
//    Author's Email: ib.arshad777@gmail.com
//
//    Redistribution and use in source and binary forms, with or without modification,
//    are permitted provided that the following conditions are met:
//
//    1. Redistributions of source code must retain the above copyright notice, this
//       list of conditions and the following disclaimer.
//
//    2. Redistributions in binary form must reproduce the above copyright notice, this
//       list of conditions and the following disclaimer in the documentation and/or
//       other materials provided with the distribution.
//
//    3. Neither the name of the copyright holder nor the names of the contributors may
//       be used to endorse or promote products derived from this software without
//       specific prior written permission.
//
//    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//    ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
//    WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
//    IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
//    INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING
//    BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
//    DATA, OR PROFITS; OR BUSINESS INTERRUPTIONS) HOWEVER CAUSED AND ON ANY THEORY OF
//    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
//    OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
//    OF THE POSSIBILITY OF SUCH DAMAGE.

package diu.edu.bd.diuclassroutine.ClassRoutine.RoutineDownloader;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import diu.edu.bd.diuclassroutine.CommonData.CommonData;

/**
 * This file was created by DIU on 7/18/2016.
 */
public class Downloader implements Runnable {
    private Context appContext;
    private Intent appIntent;
    private AppCompatActivity appActivity;
    private String downloadDirName;
    private String updateFileName;
    private String updateURL;
    private String downloadState;
    private Thread thread;
    private Handler handler;
    private boolean isDownloaded = false;
    private boolean isDownloading = false;
    private boolean restartApp = false;

    public Downloader(Context context, Intent intent, AppCompatActivity acitvity, String directory, String name, String URL) {
        appContext = context;
        appIntent = intent;
        appActivity = acitvity;
        updateFileName = name;
        updateURL = URL;
        downloadDirName = directory;
        handler = new Handler();
    }

    public void execute() {
        thread = new Thread(this, "Downloader Thread");
        thread.start();

        Toast.makeText(appContext, CommonData.databaseDownloadStarted, Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        if(isDownloading) {
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        int count;
        InputStream input = null;
        OutputStream output = null;
        isDownloaded = true;

        try {
            URL downloadURL = new URL(updateURL);
            URLConnection connection = downloadURL.openConnection();
            connection.connect();

            // InputStream to read file, with 8K buffer
            input = new BufferedInputStream((downloadURL.openStream()));

            // OutputStream to write file
            output = new FileOutputStream(new File(downloadDirName, updateFileName));

            byte data[] = new byte[1024];

            while((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            isDownloaded = true;

            downloadState = CommonData.databaseDownloadSuccess;
            handler.post(showStatusToast);
            restartApp = true;
        } catch (Exception e) {
            e.printStackTrace();
            isDownloaded = false;

            downloadState = CommonData.databaseDownloadFailed;
            handler.post(showStatusToast);
        } finally {
            // Closing streams
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        isDownloading = false;

        if(restartApp) {
            restartAndroidApp();
        }
    }

    public synchronized boolean isDownloading() {
        return isDownloading;
    }

    public synchronized boolean isDownloaded() {
        return isDownloaded;
    }

    private final Runnable showStatusToast = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(appContext, downloadState, Toast.LENGTH_SHORT).show();
        }
    };

    private void restartAndroidApp() {
        appActivity.finish();
       appContext.startActivity(appIntent);
    }
}
