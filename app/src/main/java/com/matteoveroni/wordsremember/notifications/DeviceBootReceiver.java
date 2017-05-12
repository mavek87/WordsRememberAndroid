package com.matteoveroni.wordsremember.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.Settings;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.main_menu.MainMenuActivity;

import org.joda.time.DateTime;

/**
 * @author Matteo Veroni
 */

public class DeviceBootReceiver extends BroadcastReceiver {

    public static final String TAG = TagGenerator.tag(DeviceBootReceiver.class);

    private static final int OCCURRED_TIME_FOR_NOTIFICATION_IN_DAYS = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i(TAG, "DeviceBootReceiver woke up after system boot completed.");

            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            if (preferences.contains(Settings.LAST_GAME_DATE_KEY)) {

                DateTime currentDate = new DateTime();
                Log.i(TAG, "Current date: " + currentDate);

                DateTime lastGameDate = DateTime.parse(preferences.getString(Settings.LAST_GAME_DATE_KEY, ""));
                Log.i(TAG, "Last played game\'s date: " + lastGameDate);

                if (currentDate.isAfter(lastGameDate.plusDays(OCCURRED_TIME_FOR_NOTIFICATION_IN_DAYS))) {
                    new AndroidSimpleNotifier(
                            new MainMenuActivity(),
                            context,
                            "Play in " + WordsRemember.APP_NAME,
                            "Start a new quiz game now!",
                            R.drawable.ic_notification
                    ).showNotification();
                }
            }
        }
    }
}