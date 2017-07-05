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

package diu.edu.bd.diuclassroutine.NotificationManager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import diu.edu.bd.diuclassroutine.ClassRoutine.ClassRoutine;
import diu.edu.bd.diuclassroutine.ClassRoutine.RoutineUpdater;
import diu.edu.bd.diuclassroutine.R;
import diu.edu.bd.diuclassroutine.Time.TimeManager;

/**
 * This file war created by Arshad on 7/12/2016.
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {
    public static String LOG_TAG = "NotificationBroadRec";
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if(!sharedPreferences.getBoolean(context.getString(R.string.settings_notification_key), Boolean.parseBoolean(context.getString(R.string.settings_notification_default)))) {
            return;
        }

        TimeManager timeManager = new TimeManager();

        Calendar nextTime = Calendar.getInstance();

        ClassRoutine classRoutine = new ClassRoutine(context, RoutineUpdater.getSaveDirectory(context), RoutineUpdater.updateDatabaseName);

        ClassRoutine.NextClass nextClass =  classRoutine.getNextClassTime(timeManager.getDayOfWeek());

        if(nextClass == null) {
            Log.e(LOG_TAG, "Database is corrupted or not downloaded properly, Restarting broadcast");
            restartBroadcast(context);
            return;
        } else if(nextClass.time[0] == -1 && nextClass.time[1] == -1) {
            Log.i(LOG_TAG, "No classes today, Restarting broadcast");
            restartBroadcast(context);
            return;
        } else {
            nextTime.set(Calendar.HOUR_OF_DAY, nextClass.time[0]);
            nextTime.set(Calendar.MINUTE, nextClass.time[1]);
            nextTime.set(Calendar.SECOND, 0);
        }

        Calendar cal = Calendar.getInstance();
        long diff = nextTime.getTimeInMillis() - cal.getTimeInMillis();

        if(sharedPreferences.getString(context.getString(R.string.settings_notification_time_key), context.getString(R.string.settings_notification_time_default)).equals("1")) {
            // Show notification before 20 minutes
            if(diff <= 1200000 && diff > 0) {
                Log.i(LOG_TAG, "Showing notification");
                showNotification(context, nextClass.courseName, nextClass.room + " : " + nextClass.timeString.substring(0, 7));
            }
        } else {
            // Show notification before 1 hour 5 minutes
            if(diff <= 3900000 && diff > 2700000) {
                Log.i(LOG_TAG, "Showing notification");
                showNotification(context, nextClass.courseName, nextClass.room + " : " + nextClass.timeString.substring(0, 7));
            }
        }

        restartBroadcast(context);
    }

    private void showNotification(Context context, String title, String text) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.ic_notification_smallicon);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(Color.parseColor("#61007b81"));
        }

        if(sharedPreferences.getBoolean(context.getString(R.string.settings_vibration_key), Boolean.parseBoolean(context.getString(R.string.settings_vibration_default)))) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }

        if(sharedPreferences.getBoolean(context.getString(R.string.settings_sound_key), Boolean.parseBoolean(context.getString(R.string.settings_sound_default)))) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }

        if(sharedPreferences.getBoolean(context.getString(R.string.settings_led_key), Boolean.parseBoolean(context.getString(R.string.settings_led_default)))) {
            if(sharedPreferences.getString(context.getString(R.string.settings_led_color_key), context.getString(R.string.settings_led_color_default)).equals("1")) {
                builder.setLights(Color.GREEN, 825, 825);
            } else {
                builder.setLights(Color.RED, 825, 825);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        Notification notification = builder.build();

        notificationManager.notify(0, notification);
    }

    private void restartBroadcast(Context context) {
        Intent intentN = new Intent(context, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentN, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // intervalInMillis is the time that the broadcast will be called after.
        // 150000 in milliseconds means 2.5 minutes
        long intervalInMillis = SystemClock.elapsedRealtime() + 150000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, intervalInMillis, pendingIntent);

        // This is the second alarm to call the broadcast receiver
        // 1800000 in milliseconds means 30 minutes
        intervalInMillis = SystemClock.elapsedRealtime() + 1800000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, intervalInMillis, pendingIntent);

        // This is the third alarm to call the broadcast receiver
        // 3600000 in milliseconds means 1 hour
        intervalInMillis = SystemClock.elapsedRealtime() + 3600000;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, intervalInMillis, pendingIntent);
    }
}
