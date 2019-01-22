package com.rharshit.winddown.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    private NotificationListenerReceiver notificationListenerReciver;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationListenerReciver = new NotificationListenerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.rharshit.winddown.NOTIFICATION_LISTENER_SERVICE");
        registerReceiver(notificationListenerReciver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationListenerReciver);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.i(TAG, "onNotificationPosted");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new Intent("com.rharshit.winddown.NOTIFICATION_LISTENER");
        Notification n = new Notification(sbn);
        Bundle bundle = new Bundle();
        bundle.putParcelable("NOTIFICATION", n);
        i.putExtras(bundle);
        sendBroadcast(i);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "onNOtificationRemoved");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new Intent("com.rharshit.winddown.NOTIFICATION_LISTENER");
        Notification n = new Notification(sbn);
        Bundle bundle = new Bundle();
        bundle.putParcelable("NOTIFICATION", n);
        i.putExtras(bundle);
        sendBroadcast(i);
    }

    class NotificationListenerReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("EXTRA_ACTION").equals("getNotificaitons")) {
                for (StatusBarNotification sbn : NotificationListener.this.getActiveNotifications()) {
                    Intent i = new Intent("com.rharshit.winddown.NOTIFICATION_LISTENER");
                    Notification n = new Notification(sbn);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("NOTIFICATION", n);
                    i.putExtras(bundle);
                    sendBroadcast(i);
                }
            }
        }
    }
}