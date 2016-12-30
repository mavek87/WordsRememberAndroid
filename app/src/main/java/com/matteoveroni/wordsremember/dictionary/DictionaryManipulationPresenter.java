package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.models.DictionaryDAO;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

/**
 * @author Matteo Veroni
 */
public class DictionaryManipulationPresenter implements Presenter {

    public static final String TAG = "DictManipulPresenter";

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private DictionaryManipulationView view;

    public DictionaryManipulationPresenter(DictionaryDAO model) {
        this.model = model;
    }

    /**********************************************************************************************/

    // Presenter interface

    /**********************************************************************************************/

    @Override
    public void onViewAttached(Object viewAttached) {
        view = (DictionaryManipulationView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{DictionaryManipulationView.class},
                new NullWeakReferenceProxy(viewAttached)
        );
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

    /**********************************************************************************************/

    // Activity's callbacks

    /**********************************************************************************************/

    void onVocableToManipulateRetrieved(Word vocableToManipulate) {
        view.populateViewForVocable(vocableToManipulate);
    }

    void onSaveRequest(Word currentVocableInView) {
        if (currentVocableInView != null && !currentVocableInView.getName().trim().isEmpty()) {
            if (currentVocableInView.getId() < 0) {
                model.asyncSaveVocable(currentVocableInView);
                view.returnToPreviousView();
                return;
            } else if (currentVocableInView.getId() > 0) {
                model.asyncUpdateVocable(currentVocableInView.getId(), currentVocableInView);
                view.returnToPreviousView();
                return;
            }
        }
        view.showMessage("Error occurred during the saving process. Compile all the data and retry");
    }

    /**********************************************************************************************/

    // System Events

    /**********************************************************************************************/

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

    /**********************************************************************************************/

    // Helper methods

    /**********************************************************************************************/

    private void handleAsyncEventAndGoToPreviousView(Object event) {
        try {
            eventBus.removeStickyEvent(event);
        } finally {
            view.returnToPreviousView();
        }
    }
}
