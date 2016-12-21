package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocable;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManipulationView;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.events.EventSaveVocableRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventStartVocableCreation;
import com.matteoveroni.wordsremember.dictionary.events.EventVisualizeVocable;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

public class DictionaryManipulationPresenter implements Presenter {

    public static final String TAG = "DictManipulPresenter";

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
                new NullWeakReferenceProxy(viewAttached));

        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewDetached() {
        view = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewDestroyed() {
        onViewDetached();
    }

    public void onVocableToManipulateRetrieved(Word vocableToManipulate) {
        if (vocableToManipulate != null)
            EventBus.getDefault().postSticky(new EventVisualizeVocable(vocableToManipulate));
        else
            EventBus.getDefault().postSticky(new EventStartVocableCreation());
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventSaveVocableRequest(EventSaveVocableRequest event) {
        Word vocableToSave = event.getVocable();
        if (vocableToSave != null)
            model.asyncSaveVocable(vocableToSave);
        else
            view.showMessage("Error occurred during the saving process. Compile all the data and retry");
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncSaveVocableCompleted(EventAsyncSaveVocable event) {
        long savedVocableId = event.getIdOfInsertedVocable();
        //TODO: send event to reload data in the list adapter
        view.returnToPreviousView();
        EventBus.getDefault().removeStickyEvent(event);
    }
}
