package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncSaveVocable;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.events.EventStartVocableCreation;
import com.matteoveroni.wordsremember.events.EventVisualizeVocable;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

public class DictionaryManipulationActivityPresenter implements DictionaryManipulationPresenter {

    public static final String TAG = "DictManipulPresenter";

    private final DictionaryDAO model;
    private DictionaryManagementView view;
    private Word vocableToManipulate = null;

    public DictionaryManipulationActivityPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void onViewAttached(Object viewAttached) {
        view = (DictionaryManagementView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{DictionaryManagementView.class},
                new NullWeakReferenceProxy(viewAttached));

        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewDetached() {
        view = null;
        vocableToManipulate = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewDestroyed() {
        onViewDetached();
    }

    @Override
    public void setVocableToManipulate(Word vocableToManipulate) {
        this.vocableToManipulate = vocableToManipulate;
        if (vocableToManipulate != null) {
            EventBus.getDefault().postSticky(new EventVisualizeVocable(vocableToManipulate));
        } else {
            EventBus.getDefault().postSticky(new EventStartVocableCreation());
        }
    }

    @Override
    public void onSaveVocableRequest() {
        if (vocableToManipulate != null)
            model.asyncSaveVocable(vocableToManipulate);
        else
            view.showMessage("Error occurred during the save. Compile all the data and retry");
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEventAsyncSaveVocableCompleted(EventAsyncSaveVocable event) {
        long savedVocableId = event.getIdOfInsertedVocable();
        view.showMessage("saved with id " + savedVocableId);
        EventBus.getDefault().removeStickyEvent(event);
    }
}
