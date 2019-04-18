package utils;

//import com.crashlytics.android.Crashlytics;

/**
 * Created by vivek on 10/05/18.
 */

public class MyLog {

    public static void e(Exception ex){
        ex.printStackTrace();
//        Crashlytics.logException(ex);
    }

}
