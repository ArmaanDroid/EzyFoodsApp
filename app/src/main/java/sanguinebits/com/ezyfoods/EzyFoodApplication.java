package sanguinebits.com.ezyfoods;

import android.provider.Settings;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.iid.FirebaseInstanceId;

import model.Notification;
import services.NotificationController;

public class EzyFoodApplication extends MultiDexApplication {

        public static String firebaseToken;
        public static String deviceID;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        firebaseToken = FirebaseInstanceId.getInstance().getToken();

        deviceID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        NotificationController.init(getApplicationContext());
    }
}
