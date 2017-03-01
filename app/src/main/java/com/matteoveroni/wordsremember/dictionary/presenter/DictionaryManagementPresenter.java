package com.matteoveroni.wordsremember.dictionary.presenter;

import android.content.Context;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Range;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManagementView;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private DictionaryManagementView view;

    public DictionaryManagementPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void attachView(Object view) {
        this.view = (DictionaryManagementView) view;
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
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
