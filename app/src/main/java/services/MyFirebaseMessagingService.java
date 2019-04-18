package services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import utils.AppConst;


/**
 * Created by Sahil on 8/31/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID_ORDER = AppConst.PACKAGE + "orders";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String message = remoteMessage.getData().get("message");
        String messageType = remoteMessage.getData().get("messageType");
        Log.d("TAG:", "onMessageReceived: " + message);
        Log.d("TAG:", "onMessageReceived: " + messageType);
        try {
            NotificationController.getInstance().notifyNewOrder(remoteMessage.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
