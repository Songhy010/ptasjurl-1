package khay.dy.ptasjurl.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.activity.ActivityMain;

public class FCMMessagingService extends FirebaseMessagingService {

    private Notification notification;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Received From","Test");
        //Todo broadcast intent

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("Received From",remoteMessage.getFrom());
        Log.e("Received Data",remoteMessage.getData()+"");

        Log.e("Received Data",remoteMessage.getNotification().getBody()+"");
        Log.e("Received Data",remoteMessage.getNotification().getTitle()+"");
        super.onMessageReceived(remoteMessage);
        //Log.e("Received", remoteMessage.getFrom());

        sendNotification(FCMMessagingService.this,remoteMessage.getNotification().getBody()+"",remoteMessage.getNotification().getTitle()+"");
//        Log.e("Received", ""+remoteMessage.getNotification().getBody());
    }

    @Override
    public void onNewToken(String s) {
        Log.e("Token", "Refreshed token: " + s);
    }

    private void sendNotification(Context context, String message,String title) {
        try {
            // NotificationCompat.Builder notificationBuilder;
            Intent intent;
            intent = new Intent(context, ActivityMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0  /*Request code*/ , intent, PendingIntent.FLAG_ONE_SHOT);
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String CHANNEL_ID = "channel-01";
                String channelName = "Aircon Repairer";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);

               mBuilder  = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(title)
                        .setContentText(message);
                notification = mBuilder.build();
                mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyManager.createNotificationChannel(mChannel);
                mNotifyManager.notify(1, notification);
            } else {
                mBuilder = new NotificationCompat.Builder(this, "notify_001")
                        .setSmallIcon(R.drawable.ic_about)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyManager.notify(1, mBuilder.build());
            }



           /* notificationBuilder = new NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(R.drawable.img_kim_logo)
                    .setContentText(messageBody)
                    .setLargeIcon(largeIcon)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH));
            }
            notificationManager.notify(Global.pushNumOpen, notificationBuilder.build());*/

        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
    }
}
