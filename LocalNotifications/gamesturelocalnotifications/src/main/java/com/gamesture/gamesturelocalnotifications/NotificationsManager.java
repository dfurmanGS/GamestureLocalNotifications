/**
 * Created by dariuszfurman on 20.09.2017.
 * Usage from unity have to be initialized by providing UnityActivityContex
 * After initialization notificatiion can be sent by calling native function from this plugin
 */

package com.gamesture.gamesturelocalnotifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

public class NotificationsManager extends BroadcastReceiver
{
    private static final String TAG_FOR_DEBUG = "GAMESTURE_NOTIFICATIONS";
    private static final String TAG_UNIQUE_ID = "UNIQUE_ID";
    private static final String TAG_TITLE = "TITLE";
    private static final String TAG_CONTENT = "CONTENT";
    private static final String TAG_SOUND= "sound";
    private static final String TAG_VIBRATE = "vibrate";

    private static void SendNotificationInternal(Context currentActivity, int uniqueID, String title, String message, String largeIcon, NotificationManager manager)
    {
        try
        {
            Resources res = currentActivity.getResources();

            long currentTimestamp = System.currentTimeMillis();
            int smallIconId = res.getIdentifier("small", "drawable", currentActivity.getPackageName());
            int largeIconId = res.getIdentifier(largeIcon, "drawable", currentActivity.getPackageName());
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(res,largeIconId);

            Log(currentActivity.getPackageName() + " unity context package name");

            Log(smallIconId + " identifier of small icon");
            Log(res.getResourceEntryName(smallIconId) + " resource entry name of small icon");

            Log(largeIconId + " identifier of large icon");
            Log(res.getResourceEntryName(largeIconId) + " resource entry name of large icon");

            Notification.Builder builder = new Notification.Builder(currentActivity)
                                            .setContentTitle(title)
                                            .setContentText(message)
                                            .setWhen(currentTimestamp)
                                            .setSmallIcon(smallIconId)
                                            .setLargeIcon(largeIconBitmap)
                                            .setLights(Color.CYAN, 750,3000)
                                            .setAutoCancel(true);

            Notification notification = builder.getNotification();
            manager.notify(uniqueID,notification);
        }
        catch (Exception e)
        {
            LogException(e);
        }
    }

    public static void SendNotificationInternal(int uniqueID, String title, String message, long secondsToNotification)
    {
        SendNotificationInternal(UnityPlayer.currentActivity, uniqueID,title,message,"large", (NotificationManager) UnityPlayer.currentActivity.getSystemService(Context.NOTIFICATION_SERVICE));
    }

    public static void SendNotification(int uniqueID, String title, String message, long secondsToNotification)
    {
        try
        {
            Activity currentActivity = UnityPlayer.currentActivity;
            AlarmManager alarmManager = (AlarmManager) currentActivity.getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(currentActivity, NotificationsManager.class);
            notificationIntent.putExtra(TAG_UNIQUE_ID, uniqueID);
            notificationIntent.putExtra(TAG_TITLE, title);
            notificationIntent.putExtra(TAG_CONTENT, message);
            notificationIntent.putExtra(TAG_SOUND, true);
            notificationIntent.putExtra(TAG_VIBRATE, true);

            long timestamp = System.currentTimeMillis() + ( secondsToNotification * 1000);

            Log("SHEDULING NOTIFICATION TO THE USER + UNITQUE ID: " + uniqueID + " Seconds to notification: " + secondsToNotification);
            PendingIntent intent = PendingIntent.getBroadcast(UnityPlayer.currentActivity , uniqueID, notificationIntent, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, intent);
            }
            else
            {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, intent);
            }

        }
        catch (Exception e)
        {
            LogException(e);
        }
    }

    private static void Log(String msg){
        Log.d(TAG_FOR_DEBUG, msg);
    }

    private static void LogException(Exception e)
    {
        Log.e(TAG_FOR_DEBUG, e.toString());
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log("Called onReceive function");
        try
        {
            int id = intent.getIntExtra(TAG_UNIQUE_ID,0);
            String title = intent.getStringExtra(TAG_TITLE);
            String content = intent.getStringExtra(TAG_CONTENT);
            Log("SENDING NOTIFICATION TO THE USER");
            SendNotificationInternal(context, id,title,content,"large", manager);
        }
        catch (Exception e)
        {
            LogException(e);
        }
    }
}
