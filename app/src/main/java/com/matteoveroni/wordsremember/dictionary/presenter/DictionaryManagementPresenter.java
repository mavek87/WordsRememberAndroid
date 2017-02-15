package com.matteoveroni.wordsremember.dictionary.presenter;

import android.content.Context;

import com.matteoveroni.wordsremember.NullWeakReferenceProxy;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.DictionaryManagementView;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.utilities.Str;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Proxy;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();
    private static boolean isPresenterCreatedForTheFirstTime = true;
    private final DictionaryDAO model;
    private DictionaryManagementView view;

    public DictionaryManagementPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void onViewAttached(Object viewAttached) {
        view = (DictionaryManagementView) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{DictionaryManagementView.class},
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

    public void onViewCreated(Context context) {
        if (isPresenterCreatedForTheFirstTime) {
            populateDatabaseForTestPurposes(context);
            exportDatabaseOnSd(context);
            isPresenterCreatedForTheFirstTime = false;
        }
    }

    public void onCreateVocableRequest() {
        view.goToManipulationView(new Word(""));
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventVocableSelected event) {
        Word selectedVocable = event.getSelectedVocable();
        eventBus.removeStickyEvent(event);
        view.goToManipulationView(selectedVocable);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventVocableManipulationRequest event) {
        Word vocableToManipulate = event.getVocableToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                model.asyncDeleteVocable(vocableToManipulate.getId());
                break;
        }
        eventBus.removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncDeleteVocableCompleted event) {
        eventBus.removeStickyEvent(event);
        view.showMessage("Vocable removed");
    }

    private void populateDatabaseForTestPurposes(Context context) {
        for (int i = 0; i < 5; i++) {
            Word vocableToSave = new Word(Str.generateRandomUniqueString());
            model.asyncSaveVocable(vocableToSave);
        }
    }

    private void exportDatabaseOnSd(Context context) {
        if (context != null) {
            DatabaseManager.getInstance(context).exportDatabaseOnSD();
        }
    }
}
