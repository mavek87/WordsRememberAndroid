package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchTranslationByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.EditTranslation;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
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
    private EditTranslation view;

    protected Word editedTranslationInView = null;

    public EditTranslationPresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (EditTranslation) view;

        final Word newEmptyTranslation = new Word("");
        this.view.setPojoUsed(new VocableTranslation(model.getLastValidVocableSelected(), newEmptyTranslation));

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

        if (isTranslationValid(editedTranslationInView)) {
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
        model.setLastValidTranslationSelected(editedTranslationInView);

        view.returnToPreviousView();
    }

    private boolean isTranslationValid(Word translation) {
        return (translation != null) && !translation.getName().trim().isEmpty();
    }
}
