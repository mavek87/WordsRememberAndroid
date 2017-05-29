package com.matteoveroni.wordsremember.settings.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.localization.LocaleKey;
import com.matteoveroni.wordsremember.quizgame.model.QuizGameDifficulty;
import com.matteoveroni.wordsremember.settings.model.Settings;
import com.matteoveroni.wordsremember.settings.view.SettingsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class SettingsPresenter implements Presenter<SettingsView> {

    public static final String TAG = TagGenerator.tag(SettingsPresenter.class);

//    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final Settings settings;
    private final DictionaryDAO dao;
    private SettingsView view;

    public SettingsPresenter(Settings settings, DictionaryDAO dao) {
        this.settings = settings;
        this.dao = dao;
    }

    @Override
    public void attachView(SettingsView view) {
        this.view = view;
//        EVENT_BUS.register(this);
        Log.d(TAG, "View Attached");
    }

    @Override
    public void detachView() {
        this.view = null;
//        EVENT_BUS.unregister(this);
        Log.d(TAG, "View Destroyed");
    }

//    @Subscribe
//    public void eventAbc(){
//
//    }

    public void onGameDifficultySelected(QuizGameDifficulty difficulty) {
        settings.setDifficulty(difficulty);

        String difficultyTranslationKey = "";
        switch (difficulty) {
            case EASY:
                difficultyTranslationKey = LocaleKey.EASY;
                break;
            case MEDIUM:
                difficultyTranslationKey = LocaleKey.MEDIUM;
                break;
            case HARD:
                difficultyTranslationKey = LocaleKey.HARD;
                break;
        }

        FormattedString msg_gameDifficultyChanged = new FormattedString(
                "%s (%s)",
                LocaleKey.MSG_GAME_DIFFICULTY_CHANGED,
                difficultyTranslationKey
        );
        view.showMessage(msg_gameDifficultyChanged);
    }
}
