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

package diu.edu.bd.diuclassroutine.CommonData;

/**
 * Created by DIU on 9/12/2016.
 */
public class CommonData {
    public static String[][] noDatabaseFound = {{"No Database found !!!", "", "", "", ""}};
    public static String[][] corruptedDatabase = {{"Error !!!", "Database is", "either corrupted","or not downloaded", "properly"}};
    public static String[][] noClassesToday = {{"No Classes Today", "", "    :-D", "", ""}};
    public static String[][] noDataAvailable = {{"No data available", "", "", "", ""}};

    public static String databaseFileName = "classroutine.db";
    public static String databaseDownloadStarted = "Downloading Update...";
    public static String databaseDownloadSuccess = "Class Routine downloaded successfully.";
    public static String databaseDownloadFailed = "Class Routine not downloaded !!!";

    public static String updateCheckNoInternet = "Could not check update, please review internet settings.";
    public static String updateCheckNewUPDAvailable = "New update available !";

    public static class updateURL {
        private String updateCheckURL = "https://the-bogeyman.github.io/android_classroutine/section_e/updateInfoFile.upd";

        public String updateCheckURL() {
            return updateCheckURL;
        }
    }

    public static class databaseURL {
        private String databaseFileURL = "https://the-bogeyman.github.io/android_classroutine/section_e/classRoutine.db";

        public String databaseFileURL() {
            return databaseFileURL;
        }
    }

    public static class AboutDev {
        private String[][] aboutDev = {{"Ibrahim Khan Arshad", "    Founder: SohojWeb.com", "    Daffodil International University", "    Department: CSE", "    ibrahim15-4739@diu.edu.bd"}};

        public String[][] getDevData() {
            return aboutDev;
        }
    }
}
