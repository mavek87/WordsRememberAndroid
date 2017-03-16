package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.EditVocableView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class EditVocablePresenter implements Presenter {

    private final EventBus eventBus = EventBus.getDefault();

    private final DictionaryModel model;
    private final DictionaryDAO dao;
    private EditVocableView view;

    private Word vocableInViewToPersist = null;

    public EditVocablePresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
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
        vocableInViewToPersist = view.getPojoUsedByView();
        if (isVocableValid(vocableInViewToPersist)) {
            dao.asyncSearchVocableByName(vocableInViewToPersist.getName());
        } else {
            view.showMessage("Invalid vocable. Cannot save. Compile all the data and retry");
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(EventAsyncSearchVocableByNameCompleted event) {
        final Word vocableFoundByName = event.getVocableWithSearchedName();
        if (wasVocableSavedBefore(vocableInViewToPersist)) {
            updateVocableIfPossibleOrShowError(vocableFoundByName);
        } else {
            saveVocableIfPossibleOrShowError(vocableFoundByName);
        }
    }

    private boolean wasVocableSavedBefore(Word vocable) {
        return vocable.getId() > 0;
    }

    private void updateVocableIfPossibleOrShowError(Word uniquePersistentVocableWithSameName) {
        if ((uniquePersistentVocableWithSameName != null) && (vocableInViewToPersist.getId() == uniquePersistentVocableWithSameName.getId())) {
            dao.asyncUpdateVocable(uniquePersistentVocableWithSameName.getId(), vocableInViewToPersist);
        } else {
            view.showMessage("Cannot update the vocable using this vocable name, this name is already used.");
        }
    }

    private void saveVocableIfPossibleOrShowError(Word uniquePersistentVocabledWithSameName) {
        if (uniquePersistentVocabledWithSameName == null) {
            dao.asyncSaveVocable(vocableInViewToPersist);
        } else {
            view.showMessage("Cannot save the vocable using this vocable name, this name is already used.");
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
        model.setSelectedVocable(vocableInViewToPersist);
        view.returnToPreviousView();
    }

    private boolean isVocableValid(Word vocable) {
        return (vocable != null) && !vocable.getName().trim().isEmpty();
    }
}
