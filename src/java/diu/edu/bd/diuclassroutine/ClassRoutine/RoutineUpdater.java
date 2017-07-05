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

package diu.edu.bd.diuclassroutine.ClassRoutine;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import diu.edu.bd.diuclassroutine.ClassRoutine.RoutineDownloader.Downloader;
import diu.edu.bd.diuclassroutine.CommonData.CommonData;

/**
 * This file was created by Arshad on 6/16/2016.
 */
public class RoutineUpdater {
    public static String LOG_TAG = "RoutineUpdater";
    public static String updateDatabaseName = CommonData.databaseFileName;
    public boolean isUpdating = false;
    public boolean isDownloaded = false;

    private String updateFileURL = new CommonData.databaseURL().databaseFileURL();
    private Context appContext;

    public RoutineUpdater(Context context) {
        appContext = context;
    }

    public void updateRoutine(Intent intent, AppCompatActivity activity) {
        isUpdating = true;

        Downloader downManager = new Downloader(appContext, intent, activity, getDownloadSaveLocation(),updateDatabaseName, updateFileURL);
        downManager.execute();

        long waitingTime = SystemClock.elapsedRealtime() + 60000;
        while(downManager.isDownloading())
        {
            // Wait while the download is happening...
            // Will break after 1 minute
            if(waitingTime < SystemClock.elapsedRealtime()) {
                downManager.cancel();
                Log.e(LOG_TAG, "Download canceled after waiting for 1 minute.");
                break;
            }
        }
        if(downManager.isDownloaded()) {
            isDownloaded = downManager.isDownloaded();
        }
        isUpdating = false;
    }

    private String getDownloadSaveLocation() {
        File downloadDir = new File(getSaveDirectory(appContext));
        if(!downloadDir.exists()) {
            boolean result = downloadDir.mkdirs();
            if(!result) {
                Log.e(LOG_TAG, "Couldn't create create directory to download routine database file.");
            }
        }
        return downloadDir.getAbsolutePath();
    }

    public static String getSaveDirectory(Context appContext) {
        return (appContext.getFilesDir().getAbsolutePath() + "/DIUClassRoutine");
    }
}
