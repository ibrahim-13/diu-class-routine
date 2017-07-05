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

import java.io.File;
import java.util.Calendar;

import diu.edu.bd.diuclassroutine.ClassRoutine.RoutineUpdater;

/**
 * is file was created by DIU on 7/10/2016.
 */
public class StaticMethods {
    public static int[] getHourMinutesInArray(String hhmm) {
        int[] returnVal = new int[2];

        String tmp =  String.valueOf(hhmm.charAt(0)) + String.valueOf(hhmm.charAt(1));
        if(String.valueOf(hhmm.charAt(5)).equals("p")) {
            returnVal[0] = longTimeFormat(tmp);
        } else {
            returnVal[0] = Integer.parseInt(tmp);
        }

        tmp = String.valueOf(hhmm.charAt(3)) + String.valueOf(hhmm.charAt(4));
        returnVal[1] = Integer.parseInt(tmp);

        return returnVal;
    }

    public static int longTimeFormat(String hour) {
        int time = Integer.parseInt(hour);

        time += 12;
        if(time == 24) {
            time = 0;
        }

        return time;
    }

    public static boolean hasPassed(int hh, int mm) {
        Calendar calendar = Calendar.getInstance();

        if(calendar.get(Calendar.HOUR_OF_DAY) > hh) {
            return true;
        } else if(calendar.get(Calendar.HOUR_OF_DAY) == hh) {
            if(calendar.get(Calendar.MINUTE) > mm) {
                return true;
            }
        }

        return false;
    }

    public static boolean isRoutineDownloaded(Context appContext) {
        File routine = new File(RoutineUpdater.getSaveDirectory(appContext), RoutineUpdater.updateDatabaseName);

        return routine.exists();
    }
}
