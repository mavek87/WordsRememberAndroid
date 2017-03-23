package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.EditVocableView;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
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

    public static final String MSG_VOCABLE_SAVED = "Vocable saved";
    public static final String MSG_ERROR_TRYING_TO_STORE_INVALID_VOCABLE = "Invalid vocable. Cannot save it. Compile all the data and retry";
    public static final String MSG_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME = "Cannot save the vocable using this vocable name. Name already used";

    public EditVocablePresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        Word lastValidVocableSelected = model.getLastValidVocableSelected();
        Word lastValidTranslationSelected = model.getLastValidTranslationSelected();

        this.view = (EditVocableView) view;
        this.view.setPojoUsedByView(lastValidVocableSelected);

        if (lastValidTranslationSelected != null) {
            dao.asyncSaveVocableTranslation(new VocableTranslation(lastValidVocableSelected, lastValidTranslationSelected));
            model.setLastValidTranslationSelected(null);
        }

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
        Word vocableInViewToPersist = view.getPojoUsedByView();
        model.setVocableInView(vocableInViewToPersist);
        if (isVocableValid(vocableInViewToPersist)) {
            dao.asyncSearchVocableByName(vocableInViewToPersist.getName());
        } else {
            view.showMessage(MSG_ERROR_TRYING_TO_STORE_INVALID_VOCABLE);
        }
    }

    @Subscribe
    public void onEvent(EventAsyncSearchVocableByNameCompleted event) {
        final Word persistentVocableWithSameName = event.getVocableWithSearchedName();
        if (storeVocableInViewIfHasUniqueName(persistentVocableWithSameName)) {
            view.showMessage(MSG_VOCABLE_SAVED);
        } else {
            view.showMessage(MSG_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME);
        }
    }

    private boolean storeVocableInViewIfHasUniqueName(Word persistentVocableWithSameName) {
        if (model.getEditedVocableInView().getId() <= 0) {
            return saveVocableInViewIfHasUniqueName(persistentVocableWithSameName);
        } else {
            return updateVocableInViewIfHasUniqueName(persistentVocableWithSameName);
        }
    }

    private boolean saveVocableInViewIfHasUniqueName(Word persistentVocableWithSameName) {
        if (persistentVocableWithSameName == null) {
            dao.asyncSaveVocable(model.getEditedVocableInView());
            return true;
        }
        return false;
    }

    private boolean updateVocableInViewIfHasUniqueName(Word persistentVocableWithSameName) {
        Word vocableInViewToPersist = model.getEditedVocableInView();
        if (persistentVocableWithSameName == null || vocableInViewToPersist.getId() == persistentVocableWithSameName.getId()) {
            dao.asyncUpdateVocable(vocableInViewToPersist.getId(), vocableInViewToPersist);
            return true;
        }
        return false;
    }

    @Subscribe
    public void onEvent(EventAsyncSaveVocableCompleted event) {
        handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView();
    }

    @Subscribe
    public void onEvent(EventAsyncUpdateVocableCompleted event) {
        handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView();
    }

    private void handleEventAsyncVocableStoreSuccessfulAndGoToPreviousView() {
        model.setLastValidVocableSelected(model.getEditedVocableInView());
        view.returnToPreviousView();
    }

    private boolean isVocableValid(Word vocable) {
        return (vocable != null) && !vocable.getName().trim().isEmpty();
    }
}
