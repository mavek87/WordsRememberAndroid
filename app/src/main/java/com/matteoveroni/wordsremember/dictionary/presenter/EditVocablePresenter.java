package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocablesByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.EditVocableView;
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

public class EditVocablePresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryModel model;
    private final DictionaryDAO dao;
    private EditVocableView view;

    private Word vocableInViewToValidateForSaveRequest = null;

    public EditVocablePresenter(DictionaryDAO dao) {
        this.model = WordsRemember.getDictionaryModel();
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (EditVocableView) view;
        this.view.setPojoUsedInView(model.getSelectedVocable());
        eventBus.register(this);
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        view = null;
    }

    public void onAddTranslationRequest() {
        view.goToAddTranslationView();
    }

    public void onSaveVocableRequest() {
        vocableInViewToValidateForSaveRequest = view.getPojoUsedByView();
        if (isVocableValid(vocableInViewToValidateForSaveRequest)) {
            dao.asyncSearchVocablesByName(vocableInViewToValidateForSaveRequest.getName());
        } else {
            view.showMessage("Invalid vocable. Cannot save. Compile all the data and retry");
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(EventAsyncSearchVocablesByNameCompleted event) {
        final List<Word> vocablesWithSameName = event.getVocablesWithSearchedName();

        if (isVocableSaved(vocableInViewToValidateForSaveRequest)) {
            updateVocableIfHasUniqueNameOrShowError(vocableInViewToValidateForSaveRequest, vocablesWithSameName);
        } else {
            saveVocableIfHasUniqueNameOrShowError(vocableInViewToValidateForSaveRequest, vocablesWithSameName);
        }
    }

    private boolean isVocableSaved(Word vocable) {
        return vocable.getId() > 0;
    }

    private void saveVocableIfHasUniqueNameOrShowError(Word vocableToSave, List<Word> vocablesWithSameName) {
        if (vocablesWithSameName.isEmpty()) {
            dao.asyncSaveVocable(vocableToSave);
        } else {
            view.showMessage("Cannot save the vocable using this vocable name, it is already in use.");
        }
    }

    private void updateVocableIfHasUniqueNameOrShowError(Word vocableToUpdate, List<Word> vocablesWithSameName) {
        int numberOfVocablesWithSameName = vocablesWithSameName.size();

        if (numberOfVocablesWithSameName > 1) {
            throw new RuntimeException("There are duplicated vocables names");
        }

        if ((numberOfVocablesWithSameName == 1) && (vocableToUpdate.getId() == vocablesWithSameName.get(0).getId())) {
            dao.asyncUpdateVocable(vocableToUpdate.getId(), vocableToUpdate);
        } else {
            view.showMessage("Cannot update the vocable using this vocable name, it is already in use.");
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
        eventBus.removeStickyEvent(event);
        model.setSelectedVocable(vocableInViewToValidateForSaveRequest);
        view.returnToPreviousView();
    }

    private boolean isVocableValid(Word vocable) {
        return (vocable != null) && !vocable.getName().trim().isEmpty();
    }
}
