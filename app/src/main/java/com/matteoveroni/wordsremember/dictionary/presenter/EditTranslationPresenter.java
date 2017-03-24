package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSaveVocableTranslationCompleted;
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

    private Word editedTranslationInView = null;

    public static final String MSG_TRANSLATION_SAVED = "Translation saved";

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

        if (Str.isNullOrEmpty(editedTranslationInView.getName())) {
            view.showMessage("Is not possible to save empty translations for a vocable.");
            return;
        }

        dao.asyncSaveTranslation(editedTranslationInView);
    }

    @Subscribe
    public void onEvent(EventAsyncSaveTranslationCompleted event) {
        view.showMessage(MSG_TRANSLATION_SAVED);

        final Word translationSaved = editedTranslationInView;
        translationSaved.setId(event.getSavedTranslationId());

        model.setLastValidTranslationSelected(translationSaved);
        view.returnToEditVocableView();
    }
}
