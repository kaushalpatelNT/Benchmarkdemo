package com.nichetech.smartonsite.benchmark.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.RemoteMessage;
import com.nichetech.smartonsite.benchmark.Activities.AssignedComplaintDetailsActivity;
import com.nichetech.smartonsite.benchmark.Common.Constant;
import com.nichetech.smartonsite.benchmark.Common.Validate;
import com.nichetech.smartonsite.benchmark.R;

/**
 * Created by kaushal on 9/12/16.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private String TAG = "Notification Service";

    // PUSH NOTIFICATION WEB SERIVCE
    public static final String PUSH_KEY = "push_key";
    protected static final String PUSH_NOT_SUBJECT = "notSubject";
    protected static final String PUSH_NOT_MSG = "notMsg";
    protected static final String PUSH_NOT_URL = "notUrl";
    protected static final String PUSH_NOT_READ = "notRead";

    public static void setSessionPushKey(Context context, String push_key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(PUSH_KEY, push_key);
        editor.apply();
    }

    public static String getSessionPushKey(Context context) {
        SharedPreferences savedSession = PreferenceManager.getDefaultSharedPreferences(context);
        return savedSession.getString(PUSH_KEY, null);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "Message Body: " + remoteMessage);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {

            //  Log.d("Firebase Message",remoteMessage.getNotification().getBody());
            Log.d("Firebase Message", remoteMessage.getData() + "");
            Log.d("Firebase Message", remoteMessage.getData().get("complaint_id"));
            displayNotification(getApplicationContext(), remoteMessage.getData().get("message"), remoteMessage.getData().get("complaint_id"));

        }

    }


    private static void displayNotification(Context context, String msg, String comp_id) {


        Intent noteIntent;
        if (Validate.isNotNull(comp_id)) {

            noteIntent = new Intent(context, AssignedComplaintDetailsActivity.class);
            //noteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
            noteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            String TAG = "GCM";
            noteIntent.putExtra("ActivityName", "Notification");
            noteIntent.putExtra(Constant.Complaint.ComplainId, comp_id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, noteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID);

            mBuilder.setContentTitle(context.getString(R.string.app_name));
            mBuilder.setTicker(context.getString(R.string.app_name));
            mBuilder.setWhen(System.currentTimeMillis());
            mBuilder.setContentText(msg);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setLargeIcon(icon);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
            //mBuilder.setColor(context.getResources().getColor(R.color.accent));
            mBuilder.setColor(Color.parseColor("#F8C303"));

            int id = (int) System.currentTimeMillis();

            /* NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
             */

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channel_id= "com.nichetech.saas.complaint";
                CharSequence name = "Smart On Site";
                String description = "Smart On Site";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
                channel.setDescription(description);
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                channel.setShowBadge(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            } else {


                NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
                mNotificationManager.notify(id, mBuilder.build());
            }
        }

       /* Intent noteIntent = new Intent(context, AssignedComplaintDetailsActivity.class);
        //noteIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
        noteIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);
        String TAG = "GCM";
        noteIntent.putExtra("ActivityName","Notification");
        noteIntent.putExtra("complaintId",comp_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, noteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setTicker(context.getString(R.string.app_name));
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentText(msg);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(icon);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        //mBuilder.setColor(context.getResources().getColor(R.color.accent));
        mBuilder.setColor(Color.parseColor("#F8C303"));

        int id = (int) System.currentTimeMillis();

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        mNotificationManager.notify(id, mBuilder.build());*/
    }
}
