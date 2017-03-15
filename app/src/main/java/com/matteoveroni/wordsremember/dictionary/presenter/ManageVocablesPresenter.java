package com.matteoveroni.wordsremember.dictionary.presenter;

import android.content.Context;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Range;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.WordsRemember;
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

    private final EventBus eventBus = EventBus.getDefault();
    private final DictionaryDAO dao;
    private ManageVocablesView view;

    public ManageVocablesPresenter(DictionaryDAO dao) {
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (ManageVocablesView) view;
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onCreateVocableRequest() {
        WordsRemember.getDictionaryModel().setSelectedVocable(new Word(""));
        view.goToEditVocableView();
    }

    @Subscribe(sticky = true)
    public void onEvent(EventVocableSelected event) {
        final Word selectedVocable = event.getSelectedVocable();
        eventBus.removeStickyEvent(event);
        WordsRemember.getDictionaryModel().setSelectedVocable(selectedVocable);
        view.goToEditVocableView();
    }

    @Subscribe(sticky = true)
    public void onEvent(EventVocableManipulationRequest event) {
        final Word vocableToManipulate = event.getVocableToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                dao.asyncDeleteVocable(vocableToManipulate.getId());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported vocable manipulation exception");
        }
        eventBus.removeStickyEvent(event);
    }

    @Subscribe(sticky = true)
    public void onEvent(EventAsyncDeleteVocableCompleted event) {
        eventBus.removeStickyEvent(event);
        view.showMessage("Vocable removed");
    }
}
