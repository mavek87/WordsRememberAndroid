package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.EditVocable;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Matteo Veroni
 */

public class EditVocablePresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryDAO dao;
    @Mock
    private EditVocable view;
    @Mock
    private DictionaryModel model;

    private EditVocablePresenter presenter;

    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private static final Word VOCABLE = new Word(1, "Vocable");
    private static final Word TRANSLATION = new Word(1, "Translation");
    private static final Word VOCABLE_WITH_UPDATED_NAME = new Word(1, "VocableNameUpdated");
    private static final Word VOCABLE_WITH_EMPTY_NAME = new Word(1, " ");
    private static final Word NOT_PERSISTENT_VOCABLE_IN_VIEW = new Word(-1, "name");
    private static final Word PERSISTENT_VOCABLE_IN_VIEW = new Word(1, "name");
    private static final Word ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME = new Word(2, "name");
    private static final Word PERSISTED_VOCABLE_WITH_SAME_NAME_BUT_DIFFERENT_ID = new Word(1, "name");
    private static final ArgumentCaptor<VocableTranslation> VOCABLE_TRANSLATION_ARG_CAPTOR = ArgumentCaptor.forClass(VocableTranslation.class);
    private static final List<Word> PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME = new ArrayList<>();

    @Before
    public void setUp() {
        presenter = new DictionaryEditVocablePresenterFactoryForTests(model, dao).create();

        presenter.attachView(view);

        assertTrue("Presenter should be registered to eventbus before each test", EVENT_BUS.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.destroy();

        assertFalse("Presenter should be unregistered to eventbus after each test", EVENT_BUS.isRegistered(presenter));

        PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME.clear();
    }

    @Test
    public void onViewAttached_View_Populated_UsingModelData() {
        when(model.getLastValidVocableSelected()).thenReturn(VOCABLE);

        attachViewToPresenter(view);

        verify(view).setPojoUsed(model.getLastValidVocableSelected());
    }

    @Test
    public void onViewAttached_IfExistsLastValidTranslationSelected_SaveTranslationForVocable_And_RemoveLastTranslationSelected() {
        final VocableTranslation EXPECTED_VOCABLE_TRANSLATION = new VocableTranslation(VOCABLE, TRANSLATION);

        when(model.getLastValidVocableSelected()).thenReturn(VOCABLE);
        when(model.getLastValidTranslationSelected()).thenReturn(TRANSLATION);

        attachViewToPresenter(view);

        verify(dao).asyncSaveVocableTranslation(VOCABLE_TRANSLATION_ARG_CAPTOR.capture());
        assertTrue(
                "DAO save expected translation for vocable",
                VOCABLE_TRANSLATION_ARG_CAPTOR.getValue().equals(EXPECTED_VOCABLE_TRANSLATION)
        );
        verify(model).setLastValidTranslationSelected(null);
    }

    @Test
    public void onViewAttached_IfDoesntExistsLastValidTranslationSelected_DontSaveAnyNewTranslationForVocable() {
        when(model.getLastValidVocableSelected()).thenReturn(VOCABLE);
        when(model.getLastValidTranslationSelected()).thenReturn(null);

        attachViewToPresenter(view);

        verify(dao, never()).asyncSaveVocableTranslation(any(VocableTranslation.class));
    }

    @Test
    public void onSaveVocableRequest_UsingNullVocable_View_showsError() {
        when(view.getPojoUsed()).thenReturn(null);

        presenter.onSaveVocableRequest();

        verify(view).showMessage(EditVocablePresenter.MSG_KEY_ERROR_TRYING_TO_STORE_INVALID_VOCABLE);
    }

    @Test
    public void onSaveVocableRequest_UsingVocableWithEmptyName_View_showsError() {
        when(view.getPojoUsed()).thenReturn(VOCABLE_WITH_EMPTY_NAME);

        presenter.onSaveVocableRequest();

        verify(view).showMessage(EditVocablePresenter.MSG_KEY_ERROR_TRYING_TO_STORE_INVALID_VOCABLE);
    }

    @Test
    public void onSaveVocableRequest_ForNewVocableToCreate_DAO_Calls_asyncFindVocablesWithName() {
        when(view.getPojoUsed()).thenReturn(VOCABLE);

        presenter.onSaveVocableRequest();

        verify(dao).asyncSearchVocableByName(VOCABLE.getName());
    }

    @Test
    public void onEventAsyncSaveVocableCompleted_returnToPreviousView() {
        presenter.onEvent(new EventAsyncSaveVocableCompleted(VOCABLE.getId()));

        verify(view).returnToPreviousView();
    }

    @Test
    public void onEventAsyncUpdateVocableCompleted_returnToPreviousView() {
        int FAKE_NUMBER_OF_VOCABLES_UPDATED = 1;
        presenter.onEvent(new EventAsyncUpdateVocableCompleted(FAKE_NUMBER_OF_VOCABLES_UPDATED));

        verify(view).returnToPreviousView();
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewNotPersisted_AndHasUniqueName_SaveIt() {
        presenter.editedVocableInView = NOT_PERSISTENT_VOCABLE_IN_VIEW;

        presenter.onEvent(new EventAsyncSearchVocableByNameCompleted(null));

        verify(dao).asyncSaveVocable(NOT_PERSISTENT_VOCABLE_IN_VIEW);
        verify(dao, never()).asyncUpdateVocable(any(Long.class), any(Word.class));
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewNotPersisted_AndHasDuplicateName_DontSaveItAndShowError() {
        presenter.editedVocableInView = NOT_PERSISTENT_VOCABLE_IN_VIEW;

        presenter.onEvent(new EventAsyncSearchVocableByNameCompleted(PERSISTED_VOCABLE_WITH_SAME_NAME_BUT_DIFFERENT_ID));

        verify(view).showMessage(EditVocablePresenter.MSG_KEY_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME);
        verify(dao, never()).asyncSaveVocable(any(Word.class));
        verify(dao, never()).asyncUpdateVocable(any(Long.class), any(Word.class));
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted__IfVocableInViewAlreadyPersistent_AndNewNameDuplicated_DontUpdateItAndShowError() {
        presenter.editedVocableInView = PERSISTENT_VOCABLE_IN_VIEW;

        presenter.onEvent(new EventAsyncSearchVocableByNameCompleted(ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME));

        verify(view).showMessage(EditVocablePresenter.MSG_KEY_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME);
        verify(dao, never()).asyncUpdateVocable(any(Long.class), any(Word.class));
        verify(dao, never()).asyncSaveVocable(any(Word.class));
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewStillPersistent_AndHasUniqueName_UpdateIt() {
        presenter.editedVocableInView = VOCABLE_WITH_UPDATED_NAME;

        presenter.onEvent(new EventAsyncSearchVocableByNameCompleted(VOCABLE_WITH_UPDATED_NAME));

        verify(dao).asyncUpdateVocable(VOCABLE_WITH_UPDATED_NAME.getId(), VOCABLE_WITH_UPDATED_NAME);
    }

    @Test
    public void onEventVocableTranslationDeleteRequest_Dao_asyncDeleteVocableTranslationsByVocableAndTranslationIds() {
        EventVocableTranslationManipulationRequest vocabl_transl_deleteRequest = new EventVocableTranslationManipulationRequest(
                VOCABLE.getId(),
                TRANSLATION.getId(),
                TypeOfManipulationRequest.REMOVE
        );

        presenter.onEvent(vocabl_transl_deleteRequest);
    }

    @Test
    //Todo check and fix this test
    public void onCreateTranslationsRequest_View_goToTranslationEditorView() {
//        presenter.onAddTranslationRequest();
//
//        verify(view).goToAddTranslationView();
    }

    private void attachViewToPresenter(EditVocable view) {
        if (presenter != null) {
            presenter.destroy();
        }
        presenter.attachView(view);
    }

    private class DictionaryEditVocablePresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO dao;
        private DictionaryModel model;

        DictionaryEditVocablePresenterFactoryForTests(DictionaryModel model, DictionaryDAO dao) {
            this.dao = dao;
            this.model = model;
        }

        @Override
        public EditVocablePresenter create() {
            return new EditVocablePresenter(model, dao);
        }
    }
}
