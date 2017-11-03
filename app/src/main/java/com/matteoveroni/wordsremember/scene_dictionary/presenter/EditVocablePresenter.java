package com.matteoveroni.wordsremember.scene_dictionary.presenter;

import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventAsyncDeleteVocableTranslationCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.view.EditVocableView;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.interfaces.view.View;
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
    protected static final int ADD_TRANSLATION_REQUEST_CODE = 0;

    public EditVocablePresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(Object view) {
        this.view = (EditVocableView) view;

        Word lastVocableSelected = model.getVocableSelected();
        Word lastTranslationSelected = model.getTranslationSelected();

        this.view.setPojoUsed(lastVocableSelected);
        EVENT_BUS.register(this);

        // When a translation for a vocable is selected this code is executed
        if (lastTranslationSelected != null) {
            dao.asyncSaveVocableTranslation(new VocableTranslation(lastVocableSelected, lastTranslationSelected));
            model.setTranslationSelected(null);
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

    @Subscribe
    public void onEvent(EventAsyncDeleteVocableTranslationCompleted event) {
        view.refresh();
    }

    public void onAddTranslationRequest() {
        final Word lastVocableSelected = model.getVocableSelected();
        if (Word.isValid(lastVocableSelected)) {
            view.switchToView(View.Name.ADD_TRANSLATION, ADD_TRANSLATION_REQUEST_CODE);
        } else {
            view.showDialogCannotAddTranslationIfVocableNotSaved();
        }
    }

    public void onSaveVocableRequest() {
        editedVocableInView = view.getPojoUsed();
        if (Word.isValid(editedVocableInView)) {
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
        model.setVocableSelected(editedVocableInView);
        view.returnToPreviousView();
    }
}