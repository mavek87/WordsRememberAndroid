package com.matteoveroni.wordsremember.notifications;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.matteoveroni.wordsremember.main_menu.MainMenuActivity;

/**
 * @author Matteo Veroni
 */

/**
 * Useful resources:
 * https://developer.android.com/training/scheduling/alarms.html
 * http://stackoverflow.com/questions/2179644/how-to-calculate-elapsed-time-from-now-with-joda-time
 */

public class AndroidSimpleNotifier {

    private final Activity activity;
    private final Context context;

    private final String notification_title;
    private final String notification_text;
    private final int notification_icon;

    private NotificationCompat.Builder notificationBuilder;
    private static final int NOTIFICATION_ID = 1;
    private Notification notification;

    public AndroidSimpleNotifier(Activity activity, Context context, String notification_title, String notification_text, int notification_icon) {
        this.activity = activity;
        this.context = context;

        this.notification_title = notification_title;
        this.notification_text = notification_text;
        this.notification_icon = notification_icon;
        buildNotification();
    }

    private void buildNotification() {
        notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(notification_title)
                .setContentText(notification_text)
                .setSmallIcon(notification_icon);

        // The stack builder object will contain an artificial back stack for the
        // started Activity. This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(activity.getClass());

        // Creates an explicit intent for an Activity in your app
        final Intent resultIntent = new Intent(context, activity.getClass());
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        final PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        notificationBuilder.setContentIntent(resultPendingIntent);
        notification = notificationBuilder.build();
    }

    public void showNotification() {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // NOTIFICATION_ID allows you to update the notification later on.
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
