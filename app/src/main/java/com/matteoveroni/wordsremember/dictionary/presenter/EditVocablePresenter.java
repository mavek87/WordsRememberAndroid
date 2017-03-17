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

    private static final String MSG_VOCABLE_SAVED = "Vocable saved";
    private static final String MSG_ERROR_STORE_INVALID_VOCABLE = "Invalid vocable. Cannot save it. Compile all the data and retry";
    private static final String MSG_ERROR_STORE_DUPLICATE_VOCABLE_NAME = "Cannot save the vocable using this vocable name. Name already used";

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
        Word vocableInViewToPersist = view.getPojoUsedByView();
        if (isVocableValid(vocableInViewToPersist)) {
            view.blockUIWithMessage("Saving vocable");
            dao.asyncSearchVocableByName(vocableInViewToPersist.getName());
        } else {
            view.showMessage(MSG_ERROR_STORE_INVALID_VOCABLE);
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(EventAsyncSearchVocableByNameCompleted event) {
        final Word persistentVocableWithSameName = event.getVocableWithSearchedName();
        if (storeVocableInViewIfHasUniqueName(persistentVocableWithSameName)) {
            view.showMessage(MSG_VOCABLE_SAVED);
        } else {
            view.showMessage(MSG_ERROR_STORE_DUPLICATE_VOCABLE_NAME);
            view.unblockUI();
        }
    }

    private boolean storeVocableInViewIfHasUniqueName(Word persistentVocableWithSameName) {
        if (view.getPojoUsedByView().getId() <= 0) {
            return saveVocableInViewIfHasUniqueName(persistentVocableWithSameName);
        } else {
            return updateVocableInViewIfHasUniqueName(persistentVocableWithSameName);
        }
    }

    private boolean saveVocableInViewIfHasUniqueName(Word persistentVocableWithSameName) {
        if (persistentVocableWithSameName == null) {
            dao.asyncSaveVocable(view.getPojoUsedByView());
            return true;
        }
        return false;
    }

    private boolean updateVocableInViewIfHasUniqueName(Word persistentVocableWithSameName) {
        Word vocableInViewToPersist = view.getPojoUsedByView();
        if (vocableInViewToPersist.getId() == persistentVocableWithSameName.getId()) {
            dao.asyncUpdateVocable(persistentVocableWithSameName.getId(), vocableInViewToPersist);
            return true;
        }
        return false;
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
        model.setSelectedVocable(view.getPojoUsedByView());
        view.unblockUI();
        view.returnToPreviousView();
    }

    private boolean isVocableValid(Word vocable) {
        return (vocable != null) && !vocable.getName().trim().isEmpty();
    }
}
