package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchTranslationByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.EditTranslationView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class EditTranslationPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryModel model;
    private final DictionaryDAO dao;
    private EditTranslationView view;

    public static final String MSG_TRANSLATION_SAVED = "Translation saved";
    public static final String MSG_ERROR_TRYING_TO_STORE_INVALID_TRANSLATION = "Invalid translation. Cannot save it. Compile all the data and retry";
    public static final String MSG_ERROR_TRYING_TO_STORE_DUPLICATE_TRANSLATION_NAME = "Cannot save the translation using this translation name. Name already used";

    protected Word editedTranslationInView = null;

    public EditTranslationPresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (EditTranslationView) view;

        final Word newEmptyTranslation = new Word("");
        this.view.setPojoUsedByView(new VocableTranslation(model.getLastValidVocableSelected(), newEmptyTranslation));

        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onSaveTranslationRequest() {
        final VocableTranslation vocableTranslationInView = view.getPojoUsedByView();
        editedTranslationInView = vocableTranslationInView.getTranslation();

        if (isTranslationValid(editedTranslationInView)) {
            dao.asyncSearchTranslationByName(editedTranslationInView.getName());
        } else {
            view.showMessage(MSG_ERROR_TRYING_TO_STORE_INVALID_TRANSLATION);
        }
    }

    @Subscribe
    public void onEvent(EventAsyncSearchTranslationByNameCompleted event) {
        final Word persistentTranslationWithSameName = event.getTranslationWithSearchedName();
        if (persistentTranslationWithSameName == null) {
            dao.asyncSaveTranslation(editedTranslationInView);
        } else {
            view.showMessage(MSG_ERROR_TRYING_TO_STORE_DUPLICATE_TRANSLATION_NAME);
        }
    }

    @Subscribe
    public void onEvent(EventAsyncSaveTranslationCompleted event) {
        view.showMessage(MSG_TRANSLATION_SAVED);

        editedTranslationInView.setId(event.getSavedTranslationId());
        model.setLastValidTranslationSelected(editedTranslationInView);

        view.returnToPreviousView();
    }

    private boolean isTranslationValid(Word translation) {
        return (translation != null) && !translation.getName().trim().isEmpty();
    }
}
