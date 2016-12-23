package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventVisualizeVocable;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManipulationView;
import com.matteoveroni.wordsremember.pojo.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Proxy;

public class DictionaryManipulationFragmentPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();
    private DictionaryManipulationView view;

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
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void onViewDestroyed() {
        onViewDetached();
    }

    @SuppressWarnings("unused")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventNotifiedVocableToVisualize(EventVisualizeVocable event) {
        view.populateViewForVocable(event.getVocable());
        eventBus.removeStickyEvent(event);
    }

    public void onSaveVocable(Word vocableToSave) {
        if (vocableToSave != null) {
        }
    }
}
