package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSaveVocableTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.TranslationCreateView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class TranslationEditPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private TranslationCreateView view;

    public TranslationEditPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void attachView(Object view) {
        this.view = (TranslationCreateView) view;
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

    public void onSaveTranslationRequest() {
        VocableTranslation vocableTranslation = view.getPojoUsedByView();
        if (vocableTranslation.getTranslation().getName().trim().isEmpty()) {
            view.showMessage("Is not possible to save empty translations for vocables.");
            return;
        }
        model.asyncSaveTranslation(vocableTranslation.getTranslation());
    }

    @Subscribe
    public void onEvent(EventAsyncSaveTranslationCompleted event) {
        VocableTranslation vocableTranslation = view.getPojoUsedByView();
        vocableTranslation.getTranslation().setId(event.getSavedTranslationId());
        model.asyncSaveVocableTranslation(vocableTranslation);
    }

    @Subscribe
    public void onEvent(EventAsyncSaveVocableTranslationCompleted event) {
        eventBus.removeStickyEvent(event);
        view.returnToPreviousView();
    }
}
