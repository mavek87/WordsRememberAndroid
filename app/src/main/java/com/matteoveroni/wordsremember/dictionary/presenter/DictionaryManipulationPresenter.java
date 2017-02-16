package com.matteoveroni.wordsremember.dictionary.presenter;

import android.util.Log;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.DictionaryManipulationView;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private DictionaryManipulationView view;

    public DictionaryManipulationPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void onViewAttached(Object viewAttached) {
        view = (DictionaryManipulationView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{DictionaryManipulationView.class},
                new NullWeakReferenceProxy(viewAttached)
        );
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewDetached() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void onViewDestroyed() {
        onViewDetached();
    }

    public void onVocableToManipulateRetrieved(Word vocableToManipulate) {
        view.showVocableData(vocableToManipulate);
    }

    public void onSaveRequest(Word currentVocableInView) {
        if (isVocableInvalid(currentVocableInView)) {
            view.showMessage("You can\'t save an empty vocable. Compile all the data and retry");
        } else {
            saveVocable(currentVocableInView);
            view.returnToPreviousView();
        }
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncSaveVocableCompleted event) {
//        Word translation = new Word("TranslationTest");
//        Word vocableWithSearchedId = new Word("");
//        vocableWithSearchedId.setId(event.getIdOfSavedVocable());
//        model.asyncSaveTranslationForVocable(translation, vocableWithSearchedId);
        handleAsyncEventAndGoToPreviousView(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncUpdateVocableCompleted event) {
        handleAsyncEventAndGoToPreviousView(event);
    }

    private void handleAsyncEventAndGoToPreviousView(Object event) {
        try {
            eventBus.removeStickyEvent(event);
        } finally {
            view.returnToPreviousView();
        }
    }

    private boolean isVocableInvalid(Word currentVocableInView) {
        return currentVocableInView == null || currentVocableInView.getName().trim().isEmpty();
    }

    private void saveVocable(Word currentVocableInView) {
        if (currentVocableInView.getId() <= 0) {
            model.asyncSaveVocable(currentVocableInView);
        } else {
            model.asyncUpdateVocable(currentVocableInView.getId(), currentVocableInView);
        }
    }
}
