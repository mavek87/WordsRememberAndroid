package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.ViewDictionaryTranslationsManipulation;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class DictionaryTranslationsManipulationPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private ViewDictionaryTranslationsManipulation view;

    public DictionaryTranslationsManipulationPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void attachView(Object view) {
        this.view = (ViewDictionaryTranslationsManipulation) view;
//        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
//        view = null;
    }
}
