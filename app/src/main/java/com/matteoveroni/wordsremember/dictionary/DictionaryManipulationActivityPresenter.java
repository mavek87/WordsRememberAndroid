package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocable;
import com.matteoveroni.wordsremember.dictionary.events.EventResetDictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManipulationView;
import com.matteoveroni.wordsremember.dictionary.models.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.events.EventSaveVocableRequest;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

public class DictionaryManipulationActivityPresenter implements Presenter {

    public static final String TAG = "DictManipulPresenter";

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private DictionaryManipulationView view;

    public DictionaryManipulationActivityPresenter(DictionaryDAO model) {
        this.model = model;
    }

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

    public void onVocableToManipulateRetrieved(Word vocableToManipulate) {
        view.populateViewForVocable(vocableToManipulate);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventSaveVocableRequest(EventSaveVocableRequest event) {
        eventBus.removeStickyEvent(event);
        Word vocableToSave = event.getVocable();
        if (vocableToSave != null)
            model.asyncSaveVocable(vocableToSave);
        else
            view.showMessage("Error occurred during the saving process. Compile all the data and retry");
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncSaveVocableCompleted(EventAsyncSaveVocable event) {
        eventBus.removeStickyEvent(event);
        eventBus.postSticky(new EventResetDictionaryManagementView());
        view.returnToPreviousView();
    }
}
