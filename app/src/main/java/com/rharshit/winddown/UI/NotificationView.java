package com.rharshit.winddown.UI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rharshit.winddown.R;
import com.rharshit.winddown.Util.Blur;
import com.rharshit.winddown.Util.Notification;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class NotificationView extends LinearLayout {

    private Notification notification;
    private final String appName;
    private final Drawable icon;

    private HashMap<String, ArrayList<String>> groupNotifications;

    public NotificationView(Context context, Notification notification, Drawable icon,
                            String appName, String ticker){
        this(context, notification, icon, appName);
        groupNotifications = new HashMap<>();
        addToHashMap(notification.getKey(), ticker, notification.isOngoing());
    }

    public NotificationView(Context context, Notification notification, Drawable icon,
                            String appName) {
        super(context);

        this.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setOrientation(VERTICAL);
        this.notification = notification;

        this.appName = appName;
        this.icon = icon;

        float dimen = getResources().getDimension(R.dimen.notification_icon_dimen);
        float raduis = getResources().getInteger(R.integer.notification_icon_blur_radius);
        int elevation = getResources().getInteger(R.integer.notification_icon_blur_elevation);
        float pad = getResources().getDimension(R.dimen.notification_icon_padding);

        RelativeLayout rvIcon = new RelativeLayout(context);
        rvIcon.setLayoutParams(new LayoutParams((int) (dimen + 2*raduis),
                ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageView ivIcon = new ImageView(context);
        ImageView ivIconBlur = new ImageView(context);

        ivIcon.setLayoutParams(new LayoutParams((int) (dimen + 2*raduis), (int) dimen));
        ivIconBlur.setLayoutParams(new LayoutParams((int) (dimen + 2*raduis),
                (int) (dimen + 2*raduis)));

        if(icon!=null){
            ivIcon.setImageDrawable(icon);
            ivIconBlur.setImageBitmap(Blur.transform(context, icon, raduis));
        } else {
            Log.e(TAG, "NotificationView: NULL Icon");
        }

        ivIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivIconBlur.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivIconBlur.setPadding(0, elevation, 0, 0);
        ivIconBlur.setAlpha(0.75f);

        rvIcon.setPadding((int) pad, 0, (int) pad, 0);
        rvIcon.addView(ivIconBlur);
        rvIcon.addView(ivIcon);

        this.addView(rvIcon);

        TextView packageName = new TextView(context);
        packageName.setText(appName);
        packageName.setGravity(Gravity.CENTER_HORIZONTAL);
        packageName.setPadding(0, 0, 0, (int) pad);
        packageName.setTextSize(12.0f);
        packageName.setBackgroundColor(Color.argb(0,0,0,0));
        this.addView(packageName);
        packageName.setTranslationY(-raduis);
    }

    private void addToHashMap(String key, String ticker, boolean ongoing) {
        ArrayList<String> notifs = groupNotifications.get(key);
        if(notifs == null){
            notifs = new ArrayList<>();
        }
        if(ongoing && notifs.size()>0){
            notifs.set(0, ticker);
        } else {
            notifs.add(ticker);
        }
        groupNotifications.put(key, notifs);
    }

    private boolean removeFromHashMap(String key, String ticker, boolean ongoing) {
        ArrayList<String> notifs = groupNotifications.get(key);
        groupNotifications.remove(key);
        return groupNotifications.isEmpty();
    }

    public String getGroupKey(){
        return notification.getGroupKey();
    }

    public String getKey(){
        return notification.getKey();
    }

    public String getPackageName(){
        return notification.getPackageName();
    }

    public String getAppName(){
        return appName;
    }

    public Drawable getIcon(){
        return icon;
    }

    public void updateNotification(Notification n, String ticker) {
        addToHashMap(n.getKey(), ticker, n.isOngoing());
    }

    public boolean removeNotification(Notification n, String ticker) {
        return removeFromHashMap(n.getKey(), ticker, n.isOngoing());
    }

    public ArrayList<String> getNotifications() {
        ArrayList<String> notifications = new ArrayList<>();
        for(String key : groupNotifications.keySet()){
            for(String ticker : groupNotifications.get(key)){
                Log.d(TAG, "getNotifications: Key: " + key + " Ticker: " + ticker);
                notifications.add(ticker);
            }
        }
        return notifications;
    }
}
