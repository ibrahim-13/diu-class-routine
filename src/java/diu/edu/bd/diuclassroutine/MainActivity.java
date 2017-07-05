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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import diu.edu.bd.diuclassroutine.CardView.RecyclerAdapter;
import diu.edu.bd.diuclassroutine.ClassRoutine.ClassRoutine;
import diu.edu.bd.diuclassroutine.CommonData.CommonData;
import diu.edu.bd.diuclassroutine.NotificationManager.NotificationBroadcastReceiver;
import diu.edu.bd.diuclassroutine.Time.TimeManager;
import diu.edu.bd.diuclassroutine.ClassRoutine.RoutineUpdater;

public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = "MainActivity";
    public static boolean isNotificationStarted = false;
    private Context appContext;
    private Intent appIntent;
    private TimeManager timeManager = new TimeManager();
    private RoutineUpdater routineUpdater;
    public ClassRoutine classRoutine;
    private Intent about_intent;
    private Intent info_intent;
    private Intent settings_intent;
    private Intent deadlines_intent;

    private RecyclerAdapter recyclerAdapter;
    private SharedPreferences sharedPreferences;

    private TextView dayOfWeekLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appContext = getApplicationContext();
        appIntent = getIntent();

        // **No need for storage permission any more because routine database was moved from external storage to internal storage
        //checkStoragePermission();

        classRoutine = new ClassRoutine(appContext, RoutineUpdater.getSaveDirectory(appContext), RoutineUpdater.updateDatabaseName);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);

        // Initialize intents
        about_intent = new Intent(MainActivity.this, AboutActivity.class);
        info_intent = new Intent(MainActivity.this, InfoActivity.class);
        settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
        deadlines_intent = new Intent(MainActivity.this, DeadlinesActivity.class);

        // Initiate routine updater class
        routineUpdater = new RoutineUpdater(appContext);

        if(sharedPreferences.getBoolean(getString(R.string.settings_check_update_key), Boolean.parseBoolean(getString(R.string.settings_check_update_default)))) {
            classRoutine.checkRoutineUpdate(appContext);
        }

        setRoutine();

        if(sharedPreferences.getBoolean(getString(R.string.settings_light_status_bar_key), Boolean.parseBoolean(getString(R.string.settings_light_status_bar_default)))) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View mView = findViewById(R.id.buttonNext);
                if(mView != null) {
                    mView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    Log.e(LOG_TAG, "Could not set windowLightStatusBar property. findViewById() has returned null.");
                }

                Window mWindow = getWindow();
                mWindow.setStatusBarColor(Color.parseColor("#ffffff"));
            }
        }

        if(StaticMethods.isRoutineDownloaded(appContext)) {
            startNotificationService();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_update:
            {
                routineUpdater.updateRoutine(appIntent, this);

                long waitingTime = SystemClock.elapsedRealtime() + 61000;
                while(routineUpdater.isUpdating)
                {
                    // Wait while the download is happening...
                    // Will break after 1 minute
                    if(waitingTime < SystemClock.elapsedRealtime()) {
                        Log.e(LOG_TAG, "Routine couldn't be updated after waiting 1 minute 10 seconds.");
                        break;
                    }
                }
                if(StaticMethods.isRoutineDownloaded(appContext)) {
                    classRoutine = new ClassRoutine(appContext, RoutineUpdater.getSaveDirectory(appContext), RoutineUpdater.updateDatabaseName);
                    updateRecyclerView(classRoutine.getRoutineData(timeManager.getDayOfWeek()));
                    startNotificationService();
                } else {
                    if(!StaticMethods.isRoutineDownloaded(appContext)) {
                        updateRecyclerView(CommonData.noDatabaseFound);
                    }
                }
                break;
            }
            case R.id.action_about:
            {
                startActivity(about_intent);
                break;
            }
            case R.id.action_info:
            {
                startActivity(info_intent);
                break;
            }
            case R.id.action_settings:
            {
                startActivity(settings_intent);
                break;
            }
            default:
            {
                Log.e(LOG_TAG, "Selected menu ID does not exist");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRoutine() {
        setContentView(R.layout.activity_main);

        if(StaticMethods.isRoutineDownloaded(appContext)) {
            classRoutine = new ClassRoutine(appContext, RoutineUpdater.getSaveDirectory(appContext), RoutineUpdater.updateDatabaseName);
            loadRecyclerView(classRoutine.getRoutineData(timeManager.getDayOfWeek()));

            dayOfWeekLabel = (TextView) findViewById(R.id.textViewDayOfWeek);
            dayOfWeekLabel.setText(classRoutine.getDayName(timeManager.getDayOfWeek()).toUpperCase());
        } else {
            loadRecyclerView(CommonData.noDatabaseFound);
        }
    }

    private void loadRecyclerView(String[][] data) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager recyclerLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayout);

        recyclerAdapter = new RecyclerAdapter(data);

        recyclerView.setAdapter(recyclerAdapter);
    }

    private void updateRecyclerView(String[][] data) {
        recyclerAdapter.setNewData(data);
        recyclerAdapter.notifyDataSetChanged();
    }

    public void nextButtonPressed(View view) {
        int day = timeManager.getNextDayOfWeek();
        updateRecyclerView(classRoutine.getRoutineData(day));

        dayOfWeekLabel = (TextView) findViewById(R.id.textViewDayOfWeek);
        dayOfWeekLabel.setText(classRoutine.getDayName(day).toUpperCase());
    }

    public void deadlineButtonPressed(View view) {
        startActivity(deadlines_intent);
    }

    public void resetButtonPressed(View view) {

        updateRecyclerView(classRoutine.getRoutineData(timeManager.getDayOfWeek()));

        dayOfWeekLabel = (TextView) findViewById(R.id.textViewDayOfWeek);
        dayOfWeekLabel.setText(classRoutine.getDayName(timeManager.getDayOfWeek()).toUpperCase());
    }

    /*
    public void checkStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permission_storage_write = ActivityCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission_storage_write != PackageManager.PERMISSION_GRANTED) {
                int REQUEST_CODE_ASK_PERMISSION = 128;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSION);
            }
        }

        while(ActivityCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(SystemClock.currentThreadTimeMillis() > 3000) {
                System.exit(-1);
            }
        }
    }
    */

    private void startNotificationService() {
        if(sharedPreferences.getBoolean(getString(R.string.settings_notification_key), Boolean.parseBoolean(getString(R.string.settings_notification_default)))) {
            if(!isNotificationStarted) {
                Intent notificationIntent = new Intent(appContext, NotificationBroadcastReceiver.class);
                PendingIntent pendingNotificationService_intent = PendingIntent.getBroadcast(appContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                long futureInMillis = SystemClock. elapsedRealtime() + 1000;
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingNotificationService_intent);

                isNotificationStarted = true;
            }
        }
    }
}
