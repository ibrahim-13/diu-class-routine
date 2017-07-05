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

package diu.edu.bd.diuclassroutine;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import diu.edu.bd.diuclassroutine.CardView.RecyclerAdapter;
import diu.edu.bd.diuclassroutine.ClassRoutine.ClassRoutine;
import diu.edu.bd.diuclassroutine.ClassRoutine.RoutineUpdater;

public class InfoActivity extends AppCompatActivity {
    public static String LOG_TAG = "InfoActivity";

    private Context appContext;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();
        setContentView(R.layout.activity_info);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);

        if(sharedPreferences.getBoolean(getString(R.string.settings_light_status_bar_key), Boolean.parseBoolean(getString(R.string.settings_light_status_bar_default)))) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View mView = findViewById(R.id.recycler_view_info);
                if(mView != null) {
                    mView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    Log.e(LOG_TAG, "Could not set windowLightStatusBar property. findViewById() has returned null.");
                }

                Window mWindow = getWindow();
                mWindow.setStatusBarColor(Color.parseColor("#ffffff"));
            }
        }

        ClassRoutine classRoutine = new ClassRoutine(appContext, RoutineUpdater.getSaveDirectory(appContext), RoutineUpdater.updateDatabaseName);

        loadRecyclerView(classRoutine.getExtraInfo());
    }

    private void loadRecyclerView(String[][] data) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_info);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager recyclerLayout = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(recyclerLayout);
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(data);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }
}
