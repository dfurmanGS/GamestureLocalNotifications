/**
 * Created by dariuszfurman on 20.09.2017.
 * Usage from unity have to be initialized by providing UnityActivityContex
 * After initialization notificatiion can be sent by calling native function from this plugin
 */

package com.gamesture.gamesturelocalnotifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;


public class NotificationsManager
{
    private static final String TAG_FOR_DEBUG = "GAMESTURE_NOTIFICATIONS";
    private static UnityPlayerActivity _unityContext;
    private static NotificationManager _notificationManager;

    public static void Initialize(Context context)
    {
        try
        {
            _unityContext = (UnityPlayerActivity) context;
        }
        catch(Exception e)
        {
            LogException(e);
        }
    }

    private static NotificationManager GetNotificationManager()
    {
        if (_notificationManager == null)
        {
            _notificationManager = (NotificationManager) _unityContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (_notificationManager == null)
            {
                Log("Can't get notification manager from unity context");
            }
        }

        return _notificationManager;
    }

    public static void SendNotification(int uniqueID, String title, String message, long secondsToNotification)
    {
        try
        {
            long currentTimestamp = System.currentTimeMillis();
            long millisecondsToWaitTillShow = secondsToNotification * 1000;
            long displayTimeStamp = currentTimestamp + millisecondsToWaitTillShow;
            NotificationManager manager = GetNotificationManager();

            Notification.Builder builder = new Notification.Builder(_unityContext)
                                            .setContentTitle(title)
                                            .setContentText(message)
                                            .setWhen(displayTimeStamp);

            Notification notification = builder.getNotification();
            manager.notify(uniqueID,notification);
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
}
