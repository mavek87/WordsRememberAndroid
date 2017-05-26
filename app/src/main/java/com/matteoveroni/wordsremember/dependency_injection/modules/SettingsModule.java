package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.SharedPreferences;

import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;

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
    public Settings providesSettings(SharedPreferences preferences) {
        Settings settings = new Settings(
                preferences,
                QuizGameDifficulty.EASY
        );
        return settings;
    }
}
