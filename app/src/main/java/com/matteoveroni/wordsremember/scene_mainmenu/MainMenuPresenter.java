package com.matteoveroni.wordsremember.scene_mainmenu;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.web.WebTranslatorListener;

import java.util.List;

/**
 * @author Matteo Veroni
 *         <p>
 *         https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 *         <p>
 *         https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.y0b4pwra1
 */

public class MainMenuPresenter extends BasePresenter<MainMenuView> implements WebTranslatorListener {

    // TODO: remove this REST test from production code
    //       WebTranslator.getInstance().translate(new Word("atto"), new Locale("it"), new Locale("en"), this);

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
