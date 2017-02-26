package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManipulationView;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    public void attachView(Object view) {
        this.view = (DictionaryManipulationView) view;
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onVocableToManipulateRetrieved(Word vocableToManipulate) {
        view.showVocableData(vocableToManipulate);
    }

    public void onSaveVocableRequest(Word currentVocableInView) {
        if (isVocableValid(currentVocableInView)) {
            storeVocableIntoModel(currentVocableInView);
        } else {
            view.showMessage("You can\'t save an empty vocable. Compile all the data and retry");
        }
    }

    private boolean isVocableValid(Word vocable) {
        return (vocable != null) && !vocable.getName().trim().isEmpty();
    }

    private void storeVocableIntoModel(Word vocable) {
        if (vocable.getId() <= 0) {
            model.asyncSaveVocable(vocable);
        } else {
            model.asyncUpdateVocable(vocable.getId(), vocable);
        }
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncSaveVocableCompleted event) {
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
}
