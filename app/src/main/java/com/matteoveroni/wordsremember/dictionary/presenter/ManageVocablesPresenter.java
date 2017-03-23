package com.matteoveroni.wordsremember.dictionary.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
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

    private final EventBus eventBus = EventBus.getDefault();
    private final DictionaryDAO dao;
    private ManageVocablesView view;

    private static final String MSG_VOCABLE_REMOVED = "Vocable removed";
    private static final String UNSUPPORTED_VOCABLE_MANIPULATION_EXCEPTION = "Unsupported vocable manipulation exception";

    public ManageVocablesPresenter(DictionaryDAO dao) {
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (ManageVocablesView) view;
        WordsRemember.getDictionaryModel().reset();
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onCreateVocableRequest() {
        WordsRemember.getDictionaryModel().setLastValidVocableSelected(new Word(""));
        view.goToEditVocableView();
    }

    @Subscribe
    public void onEvent(EventVocableSelected event) {
        final Word selectedVocable = event.getSelectedVocable();
        eventBus.removeStickyEvent(event);
        WordsRemember.getDictionaryModel().setLastValidVocableSelected(selectedVocable);
        view.goToEditVocableView();
    }

    public void onEvent(EventVocableManipulationRequest event) {
        final Word vocableToManipulate = event.getVocableToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                dao.asyncDeleteVocable(vocableToManipulate.getId());
                break;
            default:
                Log.e(TAG, UNSUPPORTED_VOCABLE_MANIPULATION_EXCEPTION);
                throw new UnsupportedOperationException(UNSUPPORTED_VOCABLE_MANIPULATION_EXCEPTION);
        }
        eventBus.removeStickyEvent(event);
    }

    public void onEvent(EventVocableTranslationManipulationRequest event) {
        final Word vocableToManipulate = event.getVocableToManipulate();
        final Word translationToManipulate = event.getTranslationToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
//                dao.asyncDeleteVocableTranslation(vocableToManipulate.getId());
                break;
            default:
                Log.e(TAG, UNSUPPORTED_VOCABLE_MANIPULATION_EXCEPTION);
                throw new UnsupportedOperationException(UNSUPPORTED_VOCABLE_MANIPULATION_EXCEPTION);
        }
        eventBus.removeStickyEvent(event);
    }

    public void onEvent(EventAsyncDeleteVocableCompleted event) {
        view.showMessage(MSG_VOCABLE_REMOVED);
    }
}
