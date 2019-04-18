package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.widget.Toast;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import sanguinebits.com.ezyfoods.BuildConfig;

/**
 * Created by vivek on 10/05/18.
 */

public class MiscUtils {

    public static boolean isEmptyText(String str){
        if (
                str == null ||
                str.length()<1 ||
                str.equals("") ||
                str.equals(" ")
        ){
            return true;
        }

        return false;
    }


    public static boolean isValidBloodGroup(String bloodGroup){
        String[] types = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O-", "O+"};
        if (Arrays.asList(types).contains(bloodGroup)){
            return true;
        }
        return false;
    }

    public static boolean isValidPhoneNum(String phoneNum) {
        if (phoneNum == null){
            return false;
        }

        if (phoneNum.length()>13){
            return false;
        }

        if (phoneNum.length()<10){
            return false;
        }

        return Patterns.PHONE.matcher(phoneNum).matches();
    }

    public static boolean isValidQuery(String query) {
        return true;
    }

    public static int getNotificationIdFromRequestId(String reqId){
        BigInteger bigInt = new BigInteger(reqId.getBytes());
        return bigInt.intValue();
    }

    private static int getNotificationIdFromTimeStamp(String timeStamp){
        try{
            return Integer.parseInt(
                    timeStamp.substring(timeStamp.length()-5,
                            timeStamp.length()-1));
        }catch (Exception ex){
            MyLog.e(ex);
            return 0;
        }
    }

    public static void openLocationSettings(Activity activity){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void openAppSettings(Activity activity){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public static void openUrl(Activity activity, String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static void openAppShare(Activity activity) {
        try{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey, need help for trivial things? Then checkout this " +
                            "app at: https://play.google.com/store/apps/details?id="
                            + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            activity.startActivity(Intent.createChooser(sendIntent, "Share app"));
        }catch (Exception ex){
            MyLog.e(ex);
            Toast.makeText(activity, "Error while sharing!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String getTimeInt(int i){
        if (i<10){
            return "0"+i;
        }

        return i+"";
    }

    public static boolean isTimeBetweenTwoTime(int hr1, int min1, int hr2, int min2) throws Exception {
        String initialTime = getTimeInt(hr1)+":"+getTimeInt(min1)+":00";
        String finalTime = getTimeInt(hr2)+":"+getTimeInt(min2)+":00";

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        String currentTime = getTimeInt(hour)+":"+getTimeInt(minute)+":00";

        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        if (initialTime.matches(reg) && finalTime.matches(reg) && currentTime.matches(reg)){
            boolean valid = false;
            //Start Time
            java.util.Date inTime = new SimpleDateFormat("HH:mm:ss").parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);

            //Current Time
            java.util.Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);

            //End Time
            java.util.Date finTime = new SimpleDateFormat("HH:mm:ss").parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);

            if (finalTime.compareTo(initialTime) < 0){
                calendar2.add(Calendar.DATE, 1);
//                calendar3.add(Calendar.DATE, 1);
            }

            java.util.Date actualTime = calendar3.getTime();
            if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0)
                    && actualTime.before(calendar2.getTime())){
                valid = true;
                return valid;
            } else {
                return false;
            }
        }

        return false;
    }


    public static boolean isValidInfo(String value, String[] values){
        if (isEmptyText(value)){
            return false;
        }

        for (String currVal : values){
            if (currVal.toLowerCase().equals(value.toLowerCase())){
                return true;
            }
        }

        return false;
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static void logFirebaseEvent(String event, String element, String info){
        if (element == null){
            element = "";
        }

        if (info == null){
            info = "";
        }
        try{
            Bundle params = new Bundle();
            params.putString("element", element);
            params.putString("info", info);
//            Ez.firebaseAnalytics.logEvent(event,
//                    params);
        }catch (Exception ex){
            MyLog.e(ex);
        }
    }


    public static boolean isNotConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ! (activeNetwork != null
                && activeNetwork.isConnectedOrConnecting());
    }

}
