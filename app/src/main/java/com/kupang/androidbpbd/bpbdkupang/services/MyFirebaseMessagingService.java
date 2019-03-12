package com.kupang.androidbpbd.bpbdkupang.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.ui.MainActivity;


import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String ACTION_DESTINATION = "action_destination";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        Log.e(TAG, "From: " + remoteMessage.getData().size());

        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Map<String, String> data = remoteMessage.getData();
        handleData(data);
    }


    private void sendMyNotification(String title, String message) {


        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "id_product";
            // The user-visible name of the channel.
            CharSequence name = "Product";
            // The user-visible description of the channel.
            String description = "Notifications regarding our products";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(mChannel);

            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "id_product")
                    .setSmallIcon(R.drawable.announce_30) //your app icon
                    .setBadgeIconType(R.drawable.announce_30) //your app icon
                    .setChannelId(id)
                    .setContentTitle(title)
                    .setAutoCancel(true).setContentIntent(pendingIntent)
                    .setNumber(1)
                    .setColor(255)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setWhen(System.currentTimeMillis());
            notificationManager.notify(1, notificationBuilder.build());

        } else {

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.announce_30)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent2);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }

    }

    public void cancelNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(ns);
        notificationManager.cancel(1);


    }

    private void handleData(Map<String, String> data) {
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);

        sendMyNotification(title, message);

    }
}
