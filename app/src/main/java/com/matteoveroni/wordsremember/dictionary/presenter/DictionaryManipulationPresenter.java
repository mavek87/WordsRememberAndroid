package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncFindVocablesWithSearchedNameCompleted;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManipulationView;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private DictionaryManipulationView view;

    private Word persistedVocableInView;

    public DictionaryManipulationPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void attachView(Object view) {
        this.view = (DictionaryManipulationView) view;
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onVocableToManipulateRetrieved(Word vocable) {
        persistedVocableInView = vocable;
        view.setPojoUsedInView(vocable);
    }

    public void onCreateTranslationRequest() {
        view.goToTranslationsManipulationView(persistedVocableInView);
    }

    public void onSaveVocableRequest() {
        if (isVocableValid(view.getPojoUsedByView())) {
            storeCurrentVocableInView();
        } else {
            view.showMessage("You cannot save a vocable with an empty name. Compile all the data and retry");
        }
    }

    public void storeCurrentVocableInView() {
        Word vocableInViewToStore = view.getPojoUsedByView();
        if (vocableInViewToStore.getId() > 0) {
            model.asyncUpdateVocable(vocableInViewToStore.getId(), vocableInViewToStore);
        } else {
            model.asyncFindVocablesWithName(vocableInViewToStore.getName());
        }
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncFindVocablesWithSearchedNameCompleted event) {
        List<Word> vocablesWithSearchedName = event.getVocablesWithSearchedName();
        int numberOfVocablesWithSearchedName = vocablesWithSearchedName.size();
        saveVocableIfHasUniqueName(numberOfVocablesWithSearchedName);
    }

    private void saveVocableIfHasUniqueName(int numberOfVocablesWithSameName) {
        if (numberOfVocablesWithSameName == 0) {
            model.asyncSaveVocable(view.getPojoUsedByView());
        } else {
            view.showMessage("You cannot save a vocable with this vocable name, it is already used.");
        }
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncSaveVocableCompleted event) {
        handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView(event);
    }

    @Subscribe(sticky = true)
    @SuppressWarnings("unused")
    public void onEvent(EventAsyncUpdateVocableCompleted event) {
        handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView(event);
    }

    private void handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView(Object event) {
        persistedVocableInView = view.getPojoUsedByView();
        try {
            eventBus.removeStickyEvent(event);
        } finally {
            view.returnToPreviousView();
        }
    }

    private boolean isVocableValid(Word vocable) {
        return (vocable != null) && !vocable.getName().trim().isEmpty();
    }
}
