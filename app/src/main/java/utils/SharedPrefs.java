package utils;

import android.content.Context;
import android.content.SharedPreferences;

import sanguinebits.com.ezyfoods.R;


/**
 * Created by vivek on 05/05/18.
 */

public class SharedPrefs {
    private static final String TAG = SharedPrefs.class.getSimpleName();

    private static SharedPrefs instance;
    private static Object LOCK = new Object();

    public static SharedPrefs getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK){

                if (instance == null){ //double check lock
                    instance = new SharedPrefs(context);
                }

                LOCK.notifyAll();
            }
        }
        return instance;
    }

    private SharedPreferences sharedPreferences;

    private SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getResources()
                        .getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void setString(String key, String value){
        if (value == null){
            value = "";
        }

        try{
            this.sharedPreferences.edit().putString(key, value).commit();
        }catch (Exception ex){
        }
    }

    public String getString(String key){
        return this.sharedPreferences.getString(key, "");
    }

    public String getString(String key, String def){
        return this.sharedPreferences.getString(key, def);
    }


    public void setBoolean(String key, boolean value){
        try{
            this.sharedPreferences.edit().putBoolean(key, value).commit();
        }catch (Exception ex){
        }
    }

    public boolean getBoolean(String key, boolean def){
        return sharedPreferences.getBoolean(key, def);
    }

    public void clear() {
        try{
            sharedPreferences.edit().clear().commit();
        }catch (Exception ex){
        }
    }
}
