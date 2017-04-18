package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;

import com.matteoveroni.wordsremember.Settings;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
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
    public Settings providesSettings() {
        Settings settings = new Settings();
        settings.setDifficulty(QuizGameDifficulty.EASY);
        return settings;
    }
}
