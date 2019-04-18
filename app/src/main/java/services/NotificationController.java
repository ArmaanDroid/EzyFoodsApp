package services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Map;
import java.util.UUID;

import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.HomeActivity;

public class NotificationController {
    public static final String ACTION_CHAT = "2001";

    private static final String TAG = NotificationController.class.getSimpleName();
    private static final String EXPIRATION_GROUP = "2002";
    private static final int EXPIRATION_NOTIFICATION_ID = 2002;
    private static NotificationController instance;
    private static final String CHANNEL_ID = "3001";

    private final Context context;
    private final NotificationManagerCompat notificationManager;

    public static NotificationController getInstance() throws Exception {
        if (instance == null) {
            throw new Exception(TAG + ".init() has to be called first");
        } else {
            return instance;
        }
    }

    public static void init(Context context) {
        instance = new NotificationController(context);
    }

    private NotificationController(Context context) {
        this.context = context;
        notificationManager = NotificationManagerCompat.from(context);


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

    }


    public void notifyNewOrder(Map<String, String> data) {


//        Bitmap androidImage = BitmapFactory
//                .decodeResource(context.getResources(),R.drawable.app_icon);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_small)
//                .setLargeIcon(androidImage)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("message"))
//                .setOnlyAlertOnce(true)
                .setContentIntent(getReviewPendingIntent())
                .setGroup(data.get("senderId"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.channel_description);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,
                    NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription(description);

            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();


            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        } else {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);

            long[] vibrate = {0, 100, 200, 300};
            mBuilder.setVibrate(vibrate);
        }

        int notificationId = 1324;
        notificationManager.notify(notificationId, mBuilder.build());

    }

    public void dismissNotifcation(int id) {
        if (notificationManager != null) {
            notificationManager.cancel(id);
        }
    }

    private PendingIntent getReviewPendingIntent() {
        Intent openIntent = new Intent(context, HomeActivity.class);
        openIntent.setAction(ACTION_CHAT);

        return PendingIntent.getActivity(context,
                UUID.randomUUID().hashCode(),
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
