package com.matteoveroni.wordsremember.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.scene_mainmenu.MainMenuActivity;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import org.joda.time.DateTime;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Matteo Veroni
 */

public class DeviceBootReceiver extends BroadcastReceiver {

    public static final String TAG = TagGenerator.tag(DeviceBootReceiver.class);

    private static final int DAYS_TO_PASS_BEFORE_NOTIFICATION = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d(TAG, "DeviceBootReceiver woke up after system boot completed.");

            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), MODE_PRIVATE);
            if (preferences.contains(Settings.Key.LAST_GAME_DATE)) {

                DateTime currentDate = new DateTime();
                Log.d(TAG, "Current date: " + currentDate);

                DateTime lastGameDate = DateTime.parse(preferences.getString(Settings.Key.LAST_GAME_DATE, ""));
                Log.d(TAG, "Last played game\'s date: " + lastGameDate);

                // TODO: is localized string resources
                DateTime dateOfNotification = lastGameDate.plusDays(DAYS_TO_PASS_BEFORE_NOTIFICATION);
                if (currentDate.isAfter(dateOfNotification)) {
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