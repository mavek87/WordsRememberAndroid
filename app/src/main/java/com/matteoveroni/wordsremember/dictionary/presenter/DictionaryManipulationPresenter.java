package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManipulationView;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private WeakReference<DictionaryManipulationView> view;

    public DictionaryManipulationPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void onViewAttached(Object viewAttached) {
        view = new WeakReference<>((DictionaryManipulationView) viewAttached);
        eventBus.register(this);
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
        try {
            view.get().showVocableData(vocableToManipulate);
        } catch (NullPointerException ignored) {
        }
    }

    public void onSaveVocableRequest(Word currentVocableInView) {
        if (isVocableValid(currentVocableInView)) {
            storeVocableIntoModel(currentVocableInView);
            try {
                view.get().returnToPreviousView();
            } catch (NullPointerException ignored) {
            }
        } else {
            try {
                view.get().showMessage("You can\'t save an empty vocable. Compile all the data and retry");
            } catch (NullPointerException ignored) {
            }
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
            try {
                view.get().returnToPreviousView();
            } catch (NullPointerException ignored) {
            }
        }
    }
}
