package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.EditVocableView;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.localization.LocaleKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class EditVocablePresenter implements Presenter {

    private final EventBus EVENT_BUS = EventBus.getDefault();
    private final DictionaryDAO dao;
    private final DictionaryModel model;
    private EditVocableView view;

    protected Word editedVocableInView = null;

    public EditVocablePresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (EditVocableView) view;

        Word lastValidVocableSelected = model.getLastValidVocableSelected();
        Word lastValidTranslationSelected = model.getLastValidTranslationSelected();

        this.view.setPojoUsed(lastValidVocableSelected);
        EVENT_BUS.register(this);

        if (lastValidTranslationSelected != null) {
            dao.asyncSaveVocableTranslation(new VocableTranslation(lastValidVocableSelected, lastValidTranslationSelected));
            model.setLastValidTranslationSelected(null);
        }
    }

    @Override
    public void detachView() {
        EVENT_BUS.unregister(this);
        view = null;
    }

    @Subscribe
    public void onEvent(EventVocableTranslationManipulationRequest event) {
        long vocableId = event.getVocableIdToManipulate();
        long translationId = event.getTranslationIdToManipulate();
        switch (event.getTypeOfManipulation()) {
            case REMOVE:
                dao.asyncDeleteVocableTranslationsByVocableAndTranslationIds(vocableId, translationId);
                break;
        }
    }

    public void onAddTranslationRequest() {
        final Word lastVocableSelected = model.getLastValidVocableSelected();
        if (lastVocableSelected == null || Str.isNullOrEmpty(lastVocableSelected.getName())) {
            view.showDialogCannotAddTranslationIfVocableNotSaved();
        } else {
            view.goToAddTranslationView();
        }
    }

    public void onSaveVocableRequest() {
        editedVocableInView = view.getPojoUsed();
        if (isVocableValid(editedVocableInView)) {
            dao.asyncSearchVocableByName(editedVocableInView.getName());
        } else {
            view.showMessage(LocaleKey.MSG_ERROR_TRYING_TO_STORE_INVALID_VOCABLE);
        }
    }

    @Subscribe
    public void onEvent(EventAsyncSearchVocableCompleted event) {
        final Word persistentVocableWithSameName = event.getVocable();
        if (storeViewVocableIfHasUniqueName(persistentVocableWithSameName)) {
            view.showMessage(LocaleKey.VOCABLE_SAVED);
        } else {
            view.showMessage(LocaleKey.MSG_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME);
        }
    }

    private boolean storeViewVocableIfHasUniqueName(Word persistentVocableWithSameName) {
        if (editedVocableInView.getId() <= 0) {
            return saveViewVocableIfHasUniqueName(persistentVocableWithSameName);
        } else {
            return updateViewVocableIfHasUniqueName(persistentVocableWithSameName);
        }
    }

    private boolean saveViewVocableIfHasUniqueName(Word persistentVocableWithSameName) {
        if (persistentVocableWithSameName == null) {
            dao.asyncSaveVocable(editedVocableInView);
            return true;
        }
        return false;
    }

    private boolean updateViewVocableIfHasUniqueName(Word persistentVocableWithSameName) {
        if (persistentVocableWithSameName == null || editedVocableInView.getId() == persistentVocableWithSameName.getId()) {
            dao.asyncUpdateVocable(editedVocableInView.getId(), editedVocableInView);
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
        model.setLastValidVocableSelected(editedVocableInView);
        view.returnToPreviousView();
    }

    private boolean isVocableValid(Word vocable) {
        return (vocable != null) && !vocable.getName().trim().isEmpty();
    }
}
