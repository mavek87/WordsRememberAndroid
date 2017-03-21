package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.AddTranslationView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class AddTranslationPresenter implements Presenter {

//    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO dao;
    private final DictionaryModel model;
    private AddTranslationView view;

    public AddTranslationPresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (AddTranslationView) view;
        Word newEmptyTranslation = new Word("");
        this.view.setPojoUsedInView(new VocableTranslation(model.getLastValidVocableSelected(), newEmptyTranslation));
//        eventBus.register(this);
    }

    @Override
    public void destroy() {
//        eventBus.unregister(this);
        view = null;
    }

    public void onVocableToTranslateRetrieved(Word vocable) {
        Word newEmptyTranslation = new Word("");
        view.setPojoUsedInView(new VocableTranslation(vocable, newEmptyTranslation));
    }
}
