package com.matteoveroni.wordsremember.dictionary.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.ManageVocablesView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * https://medium.com/@trionkidnapper/android-mvp-an-end-to-if-view-null-42bb6262a5d1#.tt4usoych
 * https://community.oracle.com/blogs/enicholas/2006/05/04/understanding-weak-references
 *
 * @author Matteo Veroni
 */

public class ManageVocablesPresenter implements Presenter {

    public static final String TAG = TagGenerator.tag(ManageVocablesPresenter.class);

    private final EventBus eventBus;
    private final DictionaryDAO dao;
    private final DictionaryModel model;
    private ManageVocablesView view;

    private static final String MSG_VOCABLE_REMOVED = "Vocable removed";
    private static final String UNSUPPORTED_VOCABLE_MANIPULATION_EXCEPTION = "Unsupported vocable manipulation exception";

    public ManageVocablesPresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
        this.eventBus = EventBus.getDefault();
    }

    @Override
    public void attachView(Object view) {
        this.view = (ManageVocablesView) view;
        model.reset();
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onCreateVocableRequest() {
        model.setLastValidVocableSelected(new Word(""));
        view.goToEditVocableView();
    }

    @Subscribe
    public void onEvent(EventVocableSelected event) {
        Word vocableSelected = event.getSelectedVocable();
        model.setLastValidVocableSelected(vocableSelected);
        view.goToEditVocableView();
    }

    @Subscribe
    public void onEvent(EventVocableManipulationRequest event) {
        Word vocableToManipulate = event.getVocableToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                dao.asyncDeleteVocable(vocableToManipulate.getId());
                break;
            default:
                Log.e(TAG, UNSUPPORTED_VOCABLE_MANIPULATION_EXCEPTION);
                throw new UnsupportedOperationException(UNSUPPORTED_VOCABLE_MANIPULATION_EXCEPTION);
        }
    }

    @Subscribe
    public void onEvent(EventAsyncDeleteVocableCompleted event) {
        view.showMessage(MSG_VOCABLE_REMOVED);
    }
}