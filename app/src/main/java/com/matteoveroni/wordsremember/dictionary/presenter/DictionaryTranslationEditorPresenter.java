package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryTranslationEditorView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;

import org.greenrobot.eventbus.EventBus;

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
//        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
//        view = null;
    }
}
