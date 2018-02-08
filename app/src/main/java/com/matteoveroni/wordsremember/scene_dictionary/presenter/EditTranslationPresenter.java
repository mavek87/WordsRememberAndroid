package com.matteoveroni.wordsremember.scene_dictionary.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.localization.LocaleKey;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSearchTranslationByNameCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.scene_dictionary.view.activities.EditTranslationView;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class EditTranslationPresenter extends BasePresenter<EditTranslationView> {

    private final DictionaryModel model;
    private final DictionaryDAO dao;

    protected Word editedTranslationInView = null;

    public EditTranslationPresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(EditTranslationView view) {
        super.attachView(view);
        final Word newEmptyTranslation = new Word("");
        this.view.setPojoUsed(new VocableTranslation(model.getVocableSelected(), newEmptyTranslation));
    }

    public void onSaveTranslationRequest() {
        final VocableTranslation vocableTranslationInView = view.getPojoUsed();
        editedTranslationInView = vocableTranslationInView.getTranslation();

        if (Word.isNullOrEmpty(editedTranslationInView)) {
            view.showMessage(LocaleKey.MSG_ERROR_TRYING_TO_STORE_INVALID_TRANSLATION);
        } else {
            dao.asyncSearchTranslationByName(editedTranslationInView.getName());
        }
    }

    @Subscribe
    public void onEvent(EventAsyncSearchTranslationByNameCompleted event) {
        final Word persistentTranslationWithSameName = event.getTranslationWithSearchedName();
        if (persistentTranslationWithSameName == null) {
            dao.asyncSaveTranslation(editedTranslationInView);
        } else {
            view.showMessage(LocaleKey.MSG_ERROR_TRYING_TO_STORE_DUPLICATE_TRANSLATION_NAME);
        }
    }

    @Subscribe
    public void onEvent(EventAsyncSaveTranslationCompleted event) {
        view.showMessage(LocaleKey.TRANSLATION_SAVED);

        editedTranslationInView.setId(event.getSavedTranslationId());
        model.setTranslationSelected(editedTranslationInView);

        view.returnToPreviousView();
    }
}
