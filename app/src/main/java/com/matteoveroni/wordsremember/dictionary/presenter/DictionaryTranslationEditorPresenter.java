package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSaveVocableTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryTranslationEditorView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class DictionaryTranslationEditorPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private DictionaryTranslationEditorView view;

    public DictionaryTranslationEditorPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void attachView(Object view) {
        this.view = (DictionaryTranslationEditorView) view;
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onVocableToTranslateRetrieved(Word vocable) {
        Word newEmptyTranslation = new Word("");
        view.setPojoUsedInView(new VocableTranslation(vocable, newEmptyTranslation));
    }

    public void onSaveTranslationForVocableRequest() {
        VocableTranslation vocableTranslation = view.getPojoUsedByView();
        if (vocableTranslation.getTranslation().getName().trim().isEmpty()) {
            view.showMessage("Is not possible to save empty translations for vocables.");
            return;
        }
        model.asyncSaveVocableTranslation(vocableTranslation);
    }

    @Subscribe
    public void onEvent(EventAsyncSaveVocableTranslationCompleted event) {
        eventBus.removeStickyEvent(event);
        view.returnToPreviousView();
    }
}
