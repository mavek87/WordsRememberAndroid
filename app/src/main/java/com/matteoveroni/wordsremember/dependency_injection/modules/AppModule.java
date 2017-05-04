package com.matteoveroni.wordsremember.dependency_injection.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Matteo Veroni
 */

@Module
public class AppModule {
    private WordsRemember app;

    public AppModule(WordsRemember app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        final int PREFERENCES_ID = 1;
        return app.getApplicationContext().getSharedPreferences(app.getString(R.string.preference_file_key), PREFERENCES_ID);
    }
}