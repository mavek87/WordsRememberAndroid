package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncFindVocablesByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.view.VocableEditView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * @author Matteo Veroni
 */

public class VocableEditPresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryDAO model;
    private VocableEditView view;

    private Word persistedVocableUsedByView;

    public VocableEditPresenter(DictionaryDAO model) {
        this.model = model;
    }

    @Override
    public void attachView(Object view) {
        this.view = (VocableEditView) view;
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onVocableToEditRetrieved(Word vocable) {
        persistedVocableUsedByView = vocable;
        view.setPojoUsedInView(vocable);
    }

    public void onCreateTranslationRequest() {
        view.goToTranslationSelectorView(persistedVocableUsedByView);
    }

    public void onSaveVocableRequest() {
        Word vocableInView = view.getPojoUsedByView();
        if (isVocableValid(vocableInView)) {
            model.asyncFindVocablesByName(vocableInView.getName());
        } else {
            view.showMessage("You cannot save a vocable with an empty name. Compile all the data and retry");
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(EventAsyncFindVocablesByNameCompleted event) {
        List<Word> vocablesWithSearchedName = event.getVocablesWithSearchedName();
        int numberOfVocablesWithSearchedName = vocablesWithSearchedName.size();

        Word vocableInViewToStore = view.getPojoUsedByView();
        long idOfVocableToStore = vocableInViewToStore.getId();

        if (idOfVocableToStore <= 0) {
            saveVocableIfHasUniqueName(vocableInViewToStore, numberOfVocablesWithSearchedName);
        } else {
            updateVocableIfHasUniqueName(vocableInViewToStore, vocablesWithSearchedName);
        }
    }

    private void updateVocableIfHasUniqueName(Word vocableToUpdate, List<Word> storedVocablesWithSameName) {
        int numberOfVocablesWithSameName = storedVocablesWithSameName.size();

        if (numberOfVocablesWithSameName > 1) {
            throw new RuntimeException("There are duplicated vocables names");
        }

        if ((numberOfVocablesWithSameName == 1) && (vocableToUpdate.getId() != storedVocablesWithSameName.get(0).getId())) {
            view.showMessage("Cannot update the vocable using this vocable name, it is already in use.");
        } else {
            model.asyncUpdateVocable(vocableToUpdate.getId(), vocableToUpdate);
        }
    }

    private void saveVocableIfHasUniqueName(Word vocableToSave, int numberOfStoredVocablesWithSameName) {
        if (numberOfStoredVocablesWithSameName == 0) {
            model.asyncSaveVocable(vocableToSave);
        } else {
            view.showMessage("Cannot save the vocable using this vocable name, it is already in use.");
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(EventAsyncSaveVocableCompleted event) {
        handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView(event);
    }

    @Subscribe(sticky = true)
    public void onEvent(EventAsyncUpdateVocableCompleted event) {
        handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView(event);
    }

    private void handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView(Object event) {
        persistedVocableUsedByView = view.getPojoUsedByView();
        eventBus.removeStickyEvent(event);
        view.returnToPreviousView();
    }

    private boolean isVocableValid(Word vocable) {
        return (vocable != null) && !vocable.getName().trim().isEmpty();
    }
}
