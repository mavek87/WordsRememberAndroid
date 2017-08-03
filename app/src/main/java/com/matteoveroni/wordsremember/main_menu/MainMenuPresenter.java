package com.matteoveroni.wordsremember.main_menu;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.web.WebTranslator;
import com.matteoveroni.wordsremember.web.WebTranslatorListener;

import java.util.List;
import java.util.Locale;

/**
 *
 * @author Matteo Veroni
 *
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 * <p>
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.y0b4pwra1
 */

public class MainMenuPresenter implements Presenter, WebTranslatorListener {

    private MainMenuView view;

    @Override
    public void attachView(Object view) {
        this.view = (MainMenuView) view;
        // TODO: remove this REST test from production code
        WebTranslator.getInstance().translate(new Word("atto"), new Locale("it"), new Locale("en"), this);
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    void onButtonManageDictionaryClicked() {
        view.switchToView(View.Name.MANAGE_VOCABLES);
    }

    void onButtonStartClicked() {
        view.switchToView(View.Name.QUIZ_GAME);
    }

    void onButtonSettingsClicked() {
        view.switchToView(View.Name.SETTINGS);
    }

    @Override
    public void onTranslationCompletedSuccessfully(List<Word> translationsFound) {
        Log.i(TagGenerator.tag(MainMenuPresenter.class), "Translations found from the web: \n" + Json.getInstance().toJson(translationsFound));
    }

    @Override
    public void onTranslationCompletedWithError(Throwable t) {
        Log.i(TagGenerator.tag(MainMenuPresenter.class), t.getMessage());
    }
}
