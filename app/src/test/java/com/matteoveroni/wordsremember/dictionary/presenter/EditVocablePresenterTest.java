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

import edu.emory.mathcs.backport.java.util.Collections;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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
    private final Word NOT_PERSISTED_VOCABLE_IN_VIEW = new Word(-1, "name");
    private final Word PERSISTED_VOCABLE_IN_VIEW = new Word(1, "name");
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
        when(model.getSelectedVocable()).thenReturn(VOCABLE);

        presenter.destroy();
        presenter.attachView(view);

        verify(view, times(1)).setPojoUsedInView(VOCABLE);
    }

    @Test
    public void onSaveVocableRequest_ForNewVocableToCreate_Model_asyncFindVocablesWithName() {
        when(view.getPojoUsedByView()).thenReturn(VOCABLE);

        presenter.onSaveVocableRequest();

        verify(dao).asyncSearchVocableByName(VOCABLE.getName());
    }

    @Test
    public void onSaveVocableRequest_UsingNullVocable_View_showErrorMessage() {
        when(view.getPojoUsedByView()).thenReturn(null);

        presenter.onSaveVocableRequest();

        verify(view).showMessage(String.valueOf(any()));
        verify(dao, never()).asyncSearchVocableByName(anyString());
    }

    @Test
    public void onSaveVocableRequest_UsingVocableWithEmptyName_View_showErrorMessage() {
        when(view.getPojoUsedByView()).thenReturn(VOCABLE_WITH_EMPTY_NAME);

        presenter.onSaveVocableRequest();

        verify(view).showMessage(any(String.class));
        verify(dao, never()).asyncSearchVocableByName(anyString());
    }

    @Test
    public void onCreateTranslationsRequest_View_goToTranslationEditorView() {
        presenter.onAddTranslationRequest();

        verify(view).goToAddTranslationView();
    }

    @Test
    public void onEventAsyncSaveVocableCompleted_returnToPreviousView() {
        EventAsyncSaveVocableCompleted eventAsyncSaveVocableCompleted = new EventAsyncSaveVocableCompleted(VOCABLE.getId());

        presenter.onEvent(eventAsyncSaveVocableCompleted);

        verify(view).returnToPreviousView();
    }

    @Test
    public void onEventAsyncUpdateVocableCompleted_returnToPreviousView() {
        final int FAKE_NUMBER_OF_VOCABLES_UPDATED = 1;

        EventAsyncUpdateVocableCompleted eventAsyncUpdateVocableCompleted =
                new EventAsyncUpdateVocableCompleted(FAKE_NUMBER_OF_VOCABLES_UPDATED);

        presenter.onEvent(eventAsyncUpdateVocableCompleted);

        verify(view).returnToPreviousView();
    }

    @Test
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewNotPersisted_SaveItIfHisNameIsUnique() {
        when(view.getPojoUsedByView()).thenReturn(NOT_PERSISTED_VOCABLE_IN_VIEW);

        EventAsyncSearchVocableByNameCompleted eventAsyncSearchVocableByNameCompleted =
                new EventAsyncSearchVocableByNameCompleted(Collections.emptyList());

        presenter.onEvent(eventAsyncSearchVocableByNameCompleted);

        verify(dao).asyncSaveVocable(NOT_PERSISTED_VOCABLE_IN_VIEW);
    }

    @Test
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewNotPersisted_DontSaveItIfHisNameIsDuplicated() {
        when(view.getPojoUsedByView()).thenReturn(NOT_PERSISTED_VOCABLE_IN_VIEW);

        populatePersistentListOfVocablesWithSameName(PERSISTED_VOCABLE_WITH_SAME_NAME_BUT_DIFFERENT_ID);

        EventAsyncSearchVocableByNameCompleted eventAsyncSearchVocableByNameCompleted =
                new EventAsyncSearchVocableByNameCompleted(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME);

        presenter.onEvent(eventAsyncSearchVocableByNameCompleted);

        verify(view).showMessage(anyString());
        verify(dao, never()).asyncSaveVocable(any(Word.class));
    }

    @Test(expected = RuntimeException.class)
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted_IfCurrentVocableInViewStillPersistent_ButVocablesWithSameNameAreMoreThanOne_ThrowRuntimeException() {
        when(view.getPojoUsedByView()).thenReturn(PERSISTED_VOCABLE_IN_VIEW);

        populatePersistentListOfVocablesWithSameName(
                PERSISTED_VOCABLE_IN_VIEW,
                ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME
        );

        EventAsyncSearchVocableByNameCompleted eventAsyncSearchVocableByNameCompleted =
                new EventAsyncSearchVocableByNameCompleted(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME);

        presenter.onEvent(eventAsyncSearchVocableByNameCompleted);
    }

    @Test
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted__IfCurrentVocableInViewStillPersistent_ButHisNewNameToUpdateIsDuplicatedForOtherVocable_DontUpdateAndViewShowError() {
        when(view.getPojoUsedByView()).thenReturn(PERSISTED_VOCABLE_IN_VIEW);

        populatePersistentListOfVocablesWithSameName(ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME);

        EventAsyncSearchVocableByNameCompleted event =
                new EventAsyncSearchVocableByNameCompleted(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME);

        presenter.onEvent(event);

        verify(view).showMessage(anyString());
        verify(dao, never()).asyncUpdateVocable(any(Long.class), any(Word.class));
    }

    @Test
    public void onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewAlreadySavedAndHasUniqueName_Update() {
        populatePersistentListOfVocablesWithSameName(VOCABLE);

        when(view.getPojoUsedByView()).thenReturn(VOCABLE_WITH_UPDATED_NAME);

        EventAsyncSearchVocableByNameCompleted eventAsyncSearchVocableByNameCompleted =
                new EventAsyncSearchVocableByNameCompleted(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME);

        presenter.onEvent(eventAsyncSearchVocableByNameCompleted);

        verify(dao).asyncUpdateVocable(VOCABLE_WITH_UPDATED_NAME.getId(), VOCABLE_WITH_UPDATED_NAME);
    }

    private void populatePersistentListOfVocablesWithSameName(Word... words) {
        Collections.addAll(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME, words);
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
