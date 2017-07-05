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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import diu.edu.bd.diuclassroutine.ClassRoutine.RoutineDownloader.UpdateCheck;
import diu.edu.bd.diuclassroutine.CommonData.CommonData;
import diu.edu.bd.diuclassroutine.StaticMethods;

/**
 * This file was created by DIU on 6/29/2016.
 */
public class ClassRoutine extends AppCompatActivity {
    public static String LOG_TAG = "ClassRoutine";
    private Context appContext;
    private SQLiteDatabase routineDatabase = null;
    private String dbLocation;
    private String dbName;
    private String updateCheckURL = new CommonData.updateURL().updateCheckURL();

    private String tabalNames[] = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
    private String tableExtraInfo = "extrainfo";
    private String tableDeadlines = "deadlines";
    private String tableVersionInfo = "version";
    private String columnNames[] = {"index", "coursecode", "coursename", "room", "time", "courseteacher"};
    private String columnIndex = "index";

    public ClassRoutine(Context context, String dbLocation, String dbName) {
        this.appContext = context;
        this.dbLocation = dbLocation;
        this.dbName = dbName;
    }

    private boolean openDatabase() {
        try {
            routineDatabase = SQLiteDatabase.openDatabase(dbLocation + "/" + dbName, null, SQLiteDatabase.OPEN_READONLY);
        } catch (android.database.sqlite.SQLiteCantOpenDatabaseException ex) {
            return false;
        }
        return true;
    }

    private void closeDatabase() {
        if(routineDatabase != null) {
            routineDatabase.close();
        }
    }

    public String[][] getRoutineData(int day) {
        boolean dbState = openDatabase();

        if(dbState) {
            ArrayList<String[]> list = new ArrayList<>();
            Cursor tmpCursor;
            String[] tmpString;
            int i;
            try {
                tmpCursor = routineDatabase.rawQuery("SELECT * from `" + tabalNames[day - 1] + "` ORDER BY `" + columnIndex + "` ASC;", null);
            } catch (Exception e) {
                return null;
            }
            if(tmpCursor != null) {
                if(tmpCursor.moveToFirst()) {
                    do {
                        tmpString = new String[5];
                        for (i = 1; i < 6; i++) {
                            tmpString[i - 1] = tmpCursor.getString(i);
                        }
                        list.add(tmpString);
                    }
                    while (tmpCursor.moveToNext());

                    int dataSize = list.size();
                    String[][] tmpData = new String[dataSize][5];
                    for (i = 0; i < dataSize; i++) {
                        tmpData[i] = list.get(i);
                    }

                    return tmpData;
                }
            }
            String[][] tmp = CommonData.noClassesToday;

            if(tmpCursor != null) {
                tmpCursor.close();
            }
            closeDatabase();
            return tmp;
        }

        return CommonData.noDatabaseFound;
    }

    public String[][] getExtraInfo() {
        boolean dbState = openDatabase();

        if(dbState) {
            ArrayList<String[]> list = new ArrayList<>();
            String[] tmpString;
            int i;
            Cursor tmpCursor;
            try {
                tmpCursor = routineDatabase.rawQuery("SELECT * from `" + tableExtraInfo + "`;", null);
            } catch (Exception e) {
                return null;
            }
            if(tmpCursor != null) {
                if(tmpCursor.moveToFirst()) {
                    do {
                        tmpString = new String[5];
                        for (i = 0; i < 5; i++) {
                            tmpString[i] = tmpCursor.getString(i);
                        }
                        list.add(tmpString);
                    }
                    while (tmpCursor.moveToNext());

                    int dataSize = list.size();
                    String[][] tmpData = new String[dataSize][5];
                    for (i = 0; i < dataSize; i++) {
                        tmpData[i] = list.get(i);
                    }

                    return tmpData;
                }
            }
            String[][] tmp = CommonData.noDataAvailable;

            if(tmpCursor != null) {
                tmpCursor.close();
            }
            closeDatabase();
            return tmp;
        }

        return CommonData.noDatabaseFound;
    }

    public String[][] getDeadLines() {
        boolean dbState = openDatabase();

        if(dbState) {
            ArrayList<String[]> list = new ArrayList<>();
            String[] tmpString;
            int i;
            Cursor tmpCursor;
            try {
                tmpCursor = routineDatabase.rawQuery("SELECT * from `" + tableDeadlines + "` ORDER BY `" + columnIndex + "` DESC;", null);
            } catch (Exception e) {
                return null;
            }
            if(tmpCursor != null) {
                if(tmpCursor.moveToFirst()) {
                    do {
                        tmpString = new String[5];
                        for (i = 1; i < 6; i++) {
                            tmpString[i - 1] = tmpCursor.getString(i);
                        }
                        list.add(tmpString);
                    }
                    while (tmpCursor.moveToNext());

                    int dataSize = list.size();
                    String[][] tmpData = new String[dataSize][5];
                    for (i = 0; i < dataSize; i++) {
                        tmpData[i] = list.get(i);
                    }

                    return tmpData;
                }
            }
            String[][] tmp = CommonData.noDataAvailable;

            if(tmpCursor != null) {
                tmpCursor.close();
            }
            closeDatabase();
            return tmp;
        }

        return CommonData.noDatabaseFound;
    }

    public String[][] getAboutDev() {
        return new CommonData.AboutDev().getDevData();
    }

    public String getDayName(int day) {
        return tabalNames[day - 1];
    }

    public NextClass getNextClassTime(int day) {
        openDatabase();

        NextClass nextClass = new NextClass();
        nextClass.time = new int[]{-1, -1};

        Cursor tmpCursor;
        try {
            tmpCursor = routineDatabase.rawQuery("SELECT `" + columnNames[2] + "`,`" + columnNames[3] + "`,`" + columnNames[4] + "` from `" + tabalNames[day - 1] + "`;", null);
        } catch (Exception e) {
            return null;
        }
        if(tmpCursor != null) {
            if(tmpCursor.moveToFirst()) {
                do {
                    nextClass.courseName = tmpCursor.getString(0);
                    nextClass.room = tmpCursor.getString(1);
                    nextClass.timeString = tmpCursor.getString(2);
                    nextClass.time = StaticMethods.getHourMinutesInArray(nextClass.timeString);
                    if(!StaticMethods.hasPassed(nextClass.time[0], nextClass.time[1])) {
                        tmpCursor.close();
                        closeDatabase();
                        return nextClass;
                    }
                }
                while (tmpCursor.moveToNext());
            }
        }

        if(tmpCursor != null) {
            tmpCursor.close();
        }
        closeDatabase();
        return nextClass;
    }

    public static class NextClass {
        public int[] time = new int[2];
        public String courseName;
        public String room;
        public String timeString;
    }

    public int getVersionInfo() {
        int version = -1;

        if(StaticMethods.isRoutineDownloaded(appContext)) {
            openDatabase();
            Cursor tmpCursor = null;
            try {
                tmpCursor = routineDatabase.rawQuery("SELECT * from `" + tableVersionInfo + "`;", null);
            } catch (Exception e) {
                Log.e(LOG_TAG, Arrays.toString(e.getStackTrace()));
            }
            if(tmpCursor != null) {
                if(tmpCursor.moveToFirst()) {
                    version = Integer.parseInt(tmpCursor.getString(0));
                }
            }

            if(tmpCursor != null) {
                tmpCursor.close();
            }
            closeDatabase();
        }

        return version;
    }

    public void checkRoutineUpdate(Context appContext) {
        UpdateCheck updateChecker = new UpdateCheck();
        updateChecker.checkUpdate(updateCheckURL, getVersionInfo(), appContext);
    }
}
