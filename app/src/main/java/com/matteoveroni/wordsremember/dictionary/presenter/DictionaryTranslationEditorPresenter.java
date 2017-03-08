package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryTranslationEditorView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.pojos.TranslationForVocable;
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

    private TranslationForVocable persistedTranslationForVocableUsedByView;

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

    public void onSaveTranslationForVocableRequest() {
        TranslationForVocable translationForVocableInView = view.getPojoUsedByView();
        model.asyncSaveTranslationForVocable(
                translationForVocableInView.getTranslation(),
                translationForVocableInView.getVocable()
        );
    }

    public void onVocableForTranslationRetrieved(Word vocable){
        Word translation = new Word("");
        TranslationForVocable translationForVocable = new TranslationForVocable(vocable, translation);
        view.setPojoUsedInView(translationForVocable);
    }

    @Subscribe
    public void Event(EventAsyncSaveTranslationCompleted event) {
        persistedTranslationForVocableUsedByView = view.getPojoUsedByView();
        eventBus.removeStickyEvent(event);
        view.returnToPreviousView();
    }
}
