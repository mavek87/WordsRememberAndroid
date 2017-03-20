package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocableByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.EditVocableView;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Matteo Veroni
 */

public class EditVocablePresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private EditVocableView view;
    @Mock
    private DictionaryModel model;
    @Mock
    private DictionaryDAO dao;

    private EditVocablePresenter presenter;

    private static final EventBus eventBus = EventBus.getDefault();

    private final Word VOCABLE = new Word(1, "VocableTest");
    private final Word VOCABLE_WITH_UPDATED_NAME = new Word(1, "VocableNameUpdated");
    private final Word VOCABLE_WITH_EMPTY_NAME = new Word(1, " ");
    private final Word NOT_PERSISTENT_VOCABLE_IN_VIEW = new Word(-1, "name");
    private final Word PERSISTENT_VOCABLE_IN_VIEW = new Word(1, "name");
    private final Word ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME = new Word(2, "name");
    private final Word PERSISTED_VOCABLE_WITH_SAME_NAME_BUT_DIFFERENT_ID = new Word(1, "name");

    private final List<Word> PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME = new ArrayList<>();

    @Before
    public void setUp() {
        presenter = new DictionaryEditVocablePresenterFactoryForTests(model, dao).create();

        presenter.attachView(view);

        assertTrue("Presenter should be registered to eventbus before each test", eventBus.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.destroy();

        assertFalse("Presenter should be unregistered to eventbus after each test", eventBus.isRegistered(presenter));

        PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME.clear();
    }

    @Test
    public void onViewAttached_populateViewUsingModelData() {
        presenter.destroy();

        when(model.getLastValidVocableSelected()).thenReturn(VOCABLE);

        presenter.attachView(view);

        verify(view, times(1)).setPojoUsedInView(VOCABLE);
    }

    @Test
    public void onSaveVocableRequest_ForNewVocableToCreate_DAOCalls_asyncFindVocablesWithName() {
        when(view.getPojoUsedByView()).thenReturn(VOCABLE);

        presenter.onSaveVocableRequest();

        verify(dao).asyncSearchVocableByName(VOCABLE.getName());
    }

    @Test
    public void onSaveVocableRequest_UsingNullVocable_View_showErrorMessage() {
        when(view.getPojoUsedByView()).thenReturn(null);

        presenter.onSaveVocableRequest();

        verify(view).showMessage(EditVocablePresenter.MSG_ERROR_TRYING_TO_STORE_INVALID_VOCABLE);
    }

    @Test
    public void onSaveVocableRequest_UsingVocableWithEmptyName_View_showErrorMessage() {
        when(view.getPojoUsedByView()).thenReturn(VOCABLE_WITH_EMPTY_NAME);

        presenter.onSaveVocableRequest();

        verify(view).showMessage(EditVocablePresenter.MSG_ERROR_TRYING_TO_STORE_INVALID_VOCABLE);
    }

    @Test
    public void onEventAsyncSaveVocableCompleted_returnToPreviousView() {
        EventAsyncSaveVocableCompleted eventAsyncSaveVocableCompleted = new EventAsyncSaveVocableCompleted(VOCABLE.getId());

        presenter.onEvent(eventAsyncSaveVocableCompleted);

        verify(view).returnToPreviousView();
    }

    @Test
    public void onEventAsyncUpdateVocableCompleted_returnToPreviousView() {
        int FAKE_NUMBER_OF_VOCABLES_UPDATED = 1;
        EventAsyncUpdateVocableCompleted updateVocableCompleted = new EventAsyncUpdateVocableCompleted(FAKE_NUMBER_OF_VOCABLES_UPDATED);
        presenter.onEvent(updateVocableCompleted);

        verify(view).returnToPreviousView();
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewNotPersisted_AndHasUniqueName_SaveIt() {
        when(model.getEditedVocableInView()).thenReturn(NOT_PERSISTENT_VOCABLE_IN_VIEW);

        EventAsyncSearchVocableByNameCompleted searchVocableByNameCompleted = new EventAsyncSearchVocableByNameCompleted(null);
        presenter.onEvent(searchVocableByNameCompleted);

        verify(dao).asyncSaveVocable(NOT_PERSISTENT_VOCABLE_IN_VIEW);
        verify(dao, never()).asyncUpdateVocable(any(Long.class), any(Word.class));
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewNotPersisted_AndHasDuplicateName_DontSaveItAndShowError() {
        when(model.getEditedVocableInView()).thenReturn(NOT_PERSISTENT_VOCABLE_IN_VIEW);

        EventAsyncSearchVocableByNameCompleted searchVocableByNameComplete = new EventAsyncSearchVocableByNameCompleted(PERSISTED_VOCABLE_WITH_SAME_NAME_BUT_DIFFERENT_ID);
        presenter.onEvent(searchVocableByNameComplete);

        verify(view).showMessage(EditVocablePresenter.MSG_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME);
        verify(dao, never()).asyncSaveVocable(any(Word.class));
        verify(dao, never()).asyncUpdateVocable(any(Long.class), any(Word.class));
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted__IfVocableInViewStillPersistent_AndNewNameDuplicated_DontUpdateItAndShowError() {
        when(model.getEditedVocableInView()).thenReturn(PERSISTENT_VOCABLE_IN_VIEW);

        EventAsyncSearchVocableByNameCompleted searchVocableByNameCompleted = new EventAsyncSearchVocableByNameCompleted(ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME);
        presenter.onEvent(searchVocableByNameCompleted);

        verify(view).showMessage(EditVocablePresenter.MSG_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME);
        verify(dao, never()).asyncUpdateVocable(any(Long.class), any(Word.class));
        verify(dao, never()).asyncSaveVocable(any(Word.class));
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewStillPersistent_AndHasUniqueName_UpdateIt() {
        when(model.getEditedVocableInView()).thenReturn(VOCABLE_WITH_UPDATED_NAME);

        EventAsyncSearchVocableByNameCompleted searchVocableByNameCompleted = new EventAsyncSearchVocableByNameCompleted(VOCABLE_WITH_UPDATED_NAME);
        presenter.onEvent(searchVocableByNameCompleted);

        verify(dao).asyncUpdateVocable(VOCABLE_WITH_UPDATED_NAME.getId(), VOCABLE_WITH_UPDATED_NAME);
    }

    @Test
    public void onCreateTranslationsRequest_View_goToTranslationEditorView() {
        presenter.onAddTranslationRequest();

        verify(view).goToAddTranslationView();
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
