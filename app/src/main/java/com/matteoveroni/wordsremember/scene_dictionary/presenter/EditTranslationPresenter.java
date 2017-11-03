package com.matteoveroni.wordsremember.scene_dictionary.presenter;

import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventAsyncSearchTranslationByNameCompleted;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.view.EditTranslationView;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.localization.LocaleKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class EditTranslationPresenter implements Presenter {

    private final EventBus EVENT_BUS = EventBus.getDefault();

    private final DictionaryModel model;
    private final DictionaryDAO dao;
    private EditTranslationView view;

    protected Word editedTranslationInView = null;

    public EditTranslationPresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (EditTranslationView) view;

        final Word newEmptyTranslation = new Word("");
        this.view.setPojoUsed(new VocableTranslation(model.getVocableSelected(), newEmptyTranslation));

        EVENT_BUS.register(this);
    }

    @Override
    public void detachView() {
        EVENT_BUS.unregister(this);
        view = null;
    }

    public void onSaveTranslationRequest() {
        final VocableTranslation vocableTranslationInView = view.getPojoUsed();
        editedTranslationInView = vocableTranslationInView.getTranslation();

        if (Word.isValid(editedTranslationInView)) {
            dao.asyncSearchTranslationByName(editedTranslationInView.getName());
        } else {
            view.showMessage(LocaleKey.MSG_ERROR_TRYING_TO_STORE_INVALID_TRANSLATION);
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
