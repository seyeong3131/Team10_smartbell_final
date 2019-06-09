package team10.smartbell;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URL;
import java.util.Map;

public class MenuFirebaseMessagingService extends FirebaseMessagingService {
    public static final String NOTIFICATION_CHANNEL_ID      = "Team10";
    public static final String NOTIFICATION_CHANNEL_NAME    = "SmartBell";


    public static void init(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
        );

        channel.setDescription(NOTIFICATION_CHANNEL_NAME);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setShowBadge(true);
        channel.setLightColor(Color.GREEN);
        channel.setVibrationPattern(new long[]{100, 200, 100, 200});
        channel.enableLights(true);
        channel.enableVibration(true);

        if (manager != null)
            manager.createNotificationChannel(channel);

        FirebaseMessaging.getInstance().subscribeToTopic("ALL");
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        wakeLock(this);

        int     id      = 0;
        String  title   = "SmartBell";
        String  content = "";
        Bitmap  icon    = null;

        RemoteMessage.Notification  notification    = remoteMessage.getNotification();
        Map<String, String>         data            = remoteMessage.getData();

        if (notification != null) {
            title   = notification.getTitle();
            content = notification.getBody();
        }

        if (data != null) {
            String _id = data.getOrDefault("id", "0");
            if (_id != null) id = Integer.parseInt(_id);

            String _icon = data.getOrDefault("icon", null);
            try {
                if (_icon != null)
                    icon = BitmapFactory.decodeStream(new URL(_icon).openConnection().getInputStream());
            } catch(Exception e) {
                Log.e("SmartBell", e.getLocalizedMessage());
            }
        }

        Intent intent = new Intent(this, MainActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(255, 500, 2000)
                .setAutoCancel(true);

        if (icon != null) builder.setLargeIcon(icon);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (manager != null)
            manager.notify(id, builder.build());
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean wakeLock(Context context) {
        try {
            PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (manager != null) {
                PowerManager.WakeLock wakeLock = manager.newWakeLock(
                        PowerManager.ACQUIRE_CAUSES_WAKEUP, "itamstore:wakelock"
                );

                wakeLock.acquire(3000);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
