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
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import diu.edu.bd.diuclassroutine.CommonData.CommonData;

/**
 * This file was created by Arshad on 8/24/2016.
 */
public class UpdateCheck implements Runnable {

    public static String LOG_TAG = "UpdateCheck";
    private Thread t;

    private String updateURL;
    private int currentRoutineVersion;
    private Context appContext;
    private Handler handler;
    private String msg;

    public UpdateCheck() {
        t = new Thread(this, "UpdateCheckerThread");
        handler = new Handler();
    }

    public void checkUpdate(String updateURL, int currentRoutineVersion, Context appContext) {
        this.updateURL = updateURL;
        this.currentRoutineVersion = currentRoutineVersion;
        this.appContext = appContext;

        t.start();
    }

    public void run() {
        if (currentRoutineVersion == -1) {
            return;
        }
        URL updateCheckFile = null;
        BufferedReader bufferedReader = null;

        try {
            updateCheckFile = new URL(updateURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (updateCheckFile != null) {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(updateCheckFile.openStream()));
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getLocalizedMessage());
                e.printStackTrace();

                msg = CommonData.updateCheckNoInternet;
                handler.post(showStatusToast);
            }
            if (bufferedReader != null) {
                try {
                    String line = bufferedReader.readLine();
                    int version = Integer.parseInt(line);
                    if (version > currentRoutineVersion) {
                        msg = CommonData.updateCheckNewUPDAvailable;
                        handler.post(showStatusToast);
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, Arrays.toString(e.getStackTrace()));
                    msg = CommonData.updateCheckNoInternet;
                    handler.post(showStatusToast);
                }
            }
        }
    }

    private final Runnable showStatusToast = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show();
        }
    };
}
