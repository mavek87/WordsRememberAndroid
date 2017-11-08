package com.matteoveroni.wordsremember.scene_dictionary.presenter;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.view.ManageVocablesView;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventVocableSelected;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.LocaleKey;

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
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private final DictionaryDAO dao;
    private final DictionaryModel dictionaryModel;
    private ManageVocablesView view;

    public ManageVocablesPresenter(DictionaryModel dictionaryModel, DictionaryDAO dao) {
        this.dictionaryModel = dictionaryModel;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (ManageVocablesView) view;
        dictionaryModel.reset();
        EVENT_BUS.register(this);
    }

    @Override
    public void detachView() {
        EVENT_BUS.unregister(this);
        view = null;
    }

    public void onCreateVocableRequest() {
        dictionaryModel.setVocableSelected(new Word(""));
        view.switchToView(View.Name.EDIT_VOCABLE);
    }

    @Subscribe
    public void onEvent(EventVocableSelected event) {
        dictionaryModel.setVocableSelected(event.getSelectedVocable());
        view.switchToView(View.Name.EDIT_VOCABLE);
    }

    @Subscribe
    public void onEvent(EventVocableManipulationRequest event) {
        Word vocableToManipulate = event.getVocableToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                dao.asyncDeleteVocable(vocableToManipulate.getId());
                break;
        }
    }

    @Subscribe
    public void onEvent(EventAsyncDeleteVocableCompleted event) {
        view.showMessage(LocaleKey.VOCABLE_REMOVED);
    }
}
