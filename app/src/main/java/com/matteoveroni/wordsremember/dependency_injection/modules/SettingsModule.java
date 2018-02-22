package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.SharedPreferences;

import com.matteoveroni.wordsremember.persistency.DBManager;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;
import com.matteoveroni.wordsremember.scene_userprofile.Profile;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class SettingsModule {

    public SettingsModule() {
    }

    @Provides
    @Singleton
    public Settings provideSettings(SharedPreferences sharedPrefs) {
        Settings settings;
        if (sharedPrefs.contains(Settings.Key.GAME_DIFFICULTY)) {
            settings = new Settings(sharedPrefs);
        } else {
            settings = new Settings(sharedPrefs, Settings.DEFAULT_DIFFICULTY);
        }
        return settings;
    }
}
