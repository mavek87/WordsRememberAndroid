package com.matteoveroni.wordsremember.dictionary.presenter;

import android.content.Context;

import com.matteoveroni.myutils.Range;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncVocableDeletionComplete;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManagementView;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 * https://community.oracle.com/blogs/enicholas/2006/05/04/understanding-weak-references
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementPresenter implements Presenter {

    @SuppressWarnings("unused")
    public static final String TAG = TagGenerator.tag(DictionaryManagementPresenter.class);

    private final EventBus eventBus = EventBus.getDefault();
    private static boolean isPresenterCreatedForTheFirstTime = true;
    private final DictionaryDAO model;
    private WeakReference<DictionaryManagementView> view;

    public DictionaryManagementPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void onViewAttached(Object viewAttached) {
        view = new WeakReference<>((DictionaryManagementView) viewAttached);
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

    //TODO: remove this method in production code
    public void onViewCreated(Context context) {
        if (isPresenterCreatedForTheFirstTime) {
            populateDatabaseForTestPurposes(context);
            exportDatabaseOnSd(context);
            isPresenterCreatedForTheFirstTime = false;
        }
    }

    public void onCreateVocableRequest() {
        try {
            view.get().goToManipulationView(new Word(""));
        } catch (NullPointerException ex) {
        }
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventVocableSelected event) {
        Word selectedVocable = event.getSelectedVocable();
        eventBus.removeStickyEvent(event);
        try {
            view.get().goToManipulationView(selectedVocable);
        } catch (NullPointerException ex) {
        }
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
    public void onEvent(EventAsyncVocableDeletionComplete event) {
        eventBus.removeStickyEvent(event);
        try {
            view.get().showMessage("Vocable removed");
        } catch (NullPointerException ex) {
        }
    }

    private void populateDatabaseForTestPurposes(Context context) {
        for (int i = 0; i < 5; i++) {
            Word vocableToSave = new Word(Str.generateUniqueRndLowercaseString(new Range(3, 20)));
//            model.asyncSaveVocable(vocableToSave);
        }
    }

    private void exportDatabaseOnSd(Context context) {
        if (context != null) {
            DatabaseManager.getInstance(context).exportDatabaseOnSD();
        }
    }
}
