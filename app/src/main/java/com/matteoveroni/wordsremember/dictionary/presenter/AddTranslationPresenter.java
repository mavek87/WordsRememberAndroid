package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.AddTranslationView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AddTranslationPresenter implements Presenter {

//    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private AddTranslationView view;

    public AddTranslationPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void attachView(Object view) {
        this.view = (AddTranslationView) view;
//        eventBus.register(this);
    }

    @Override
    public void destroy() {
//        eventBus.unregister(this);
        view = null;
    }

    public void onVocableToTranslateRetrieved(Word vocable) {
        Word newEmptyTranslation = new Word("");
//        view.setPojoUsedInView(new VocableTranslation(vocable, newEmptyTranslation));
    }
}
