package com.matteoveroni.wordsremember.scene_dictionary.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.BasePresenter;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.AndroidLocaleKey;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncSearchVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventAsyncDeleteVocableTranslationCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.scene_dictionary.view.activities.EditVocableView;

import org.greenrobot.eventbus.Subscribe;

/**
 * @author Matteo Veroni
 */

public class EditVocablePresenter extends BasePresenter<EditVocableView> implements AbstractPresentedActivityView.ErrorDialogListener {

    private final DictionaryDAO dao;
    private final DictionaryModel model;

    protected static final int ADD_TRANSLATION_REQUEST_CODE = 0;

    protected Word editedVocableInView = null;

    public EditVocablePresenter(DictionaryModel model, DictionaryDAO dao) {
        this.model = model;
        this.dao = dao;
    }

    @Override
    public void attachView(EditVocableView view) {
        super.attachView(view);

        Word lastVocableSelected = model.getVocableSelected();
        Word lastTranslationSelected = model.getTranslationSelected();

        if (Word.isNotPersisted(lastVocableSelected)) {
            this.view.setHeader(AndroidLocaleKey.CREATE_VOCABLE);
        } else {
            this.view.setHeader(AndroidLocaleKey.EDIT_VOCABLE);
        }

        this.view.setPojoUsed(lastVocableSelected);

        // When a translation for a vocable is selected, attach view is called again so
        // here I save the translation for the vocable and clear the dictionary model
        if (lastTranslationSelected != null) {
            dao.asyncSaveVocableTranslation(new VocableTranslation(lastVocableSelected, lastTranslationSelected));
            model.setTranslationSelected(null);
        }
    }

    public void onAddTranslationRequest() {
        if (Word.isNotPersisted(model.getVocableSelected())) {
            view.showErrorDialogVocableNotSaved();
        } else {
            view.switchToView(View.Name.ADD_TRANSLATION, ADD_TRANSLATION_REQUEST_CODE);
        }
    }

    @Override
    public void onErrorDialogPositiveButtonPressed() {
        // Error dialog: vocable not saved. do u want to save? if you response yes...
        onSaveVocableRequest();
    }

    @Override
    public void onErrorDialogNegativeButtonPressed() {
        // Error dialog: vocable not saved. do u want to save? if you response no...
        view.dismissErrorDialog();
    }

    public void onSaveVocableRequest() {
        editedVocableInView = view.getPojoUsed();
        if (Word.isNullOrEmpty(editedVocableInView)) {
            view.showMessage(AndroidLocaleKey.MSG_ERROR_TRYING_TO_STORE_INVALID_VOCABLE);
        } else {
            dao.asyncSearchVocableByName(editedVocableInView.getName());
        }
    }

    @Subscribe
    public void onEvent(EventAsyncSearchVocableCompleted event) {
        Word otherStoredVocableWithSameName = event.getVocable();
        if (otherStoredVocableWithSameName == null) {
            if (Word.isNotPersisted(editedVocableInView)) {
                dao.asyncSaveVocable(editedVocableInView);
            } else {
                dao.asyncUpdateVocable(editedVocableInView.getId(), editedVocableInView);
            }
        } else {
            view.showMessage(AndroidLocaleKey.MSG_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME);
        }
    }

    @Subscribe
    public void onEvent(EventAsyncSaveVocableCompleted event) {
        updateModelAndViewForVocable(editedVocableInView);
    }

    @Subscribe
    public void onEvent(EventAsyncUpdateVocableCompleted event) {
        updateModelAndViewForVocable(editedVocableInView);
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

    private void updateModelAndViewForVocable(Word vocable) {
        model.setVocableSelected(vocable);
        view.showMessage(AndroidLocaleKey.VOCABLE_SAVED);
        view.returnToPreviousView();
    }
}
