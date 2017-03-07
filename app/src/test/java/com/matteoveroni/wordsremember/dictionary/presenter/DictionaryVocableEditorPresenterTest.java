package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSearchVocablesByNameCompleted;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryVocableEditor;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Matteo Veroni
 */

public class DictionaryVocableEditorPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryVocableEditor view;
    @Mock
    private DictionaryDAO model;

    private DictionaryVocableEditorPresenter presenter;

    private static final EventBus eventBus = EventBus.getDefault();

    private final Word VOCABLE = new Word(1, "VocableTest");
    private final Word VOCABLE_WITH_UPDATED_NAME = new Word(1, "VocableNameUpdated");
    private final Word NOT_PERSISTED_VOCABLE_IN_VIEW = new Word(-1, "name");
    private final Word PERSISTED_VOCABLE_IN_VIEW = new Word(1, "name");
    private final Word ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME = new Word(2, "name");
    private final Word PERSISTED_VOCABLE_WITH_SAME_NAME_BUT_DIFFERENT_ID = new Word(1, "name");

    private final List<Word> PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME = new ArrayList<>();

    @Before
    public void setUp() {
        presenter = new DictionaryManipulationPresenterFactoryForTests(model).create();

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
    public void onVocableToManipulateRetrieved_OrderToViewTo_showVocableData() {
        presenter.onVocableToManipulateRetrieved(VOCABLE);

        verify(view).setPojoUsedInView(VOCABLE);
    }

    @Test
    public void onSaveVocableRequest_ForNewVocableToCreate_OrderToModelToCall_asyncFindVocablesWithName() {
        when(view.getPojoUsedByView()).thenReturn(VOCABLE);

        presenter.onSaveVocableRequest();

        verify(model).asyncFindVocablesWithName(VOCABLE.getName());
    }

    @Test
    public void onSaveVocableRequest_UsingNullVocable_OrderToViewTo_showErrorMessage() {
        when(view.getPojoUsedByView()).thenReturn(null);

        presenter.onSaveVocableRequest();

        verify(view).showMessage(String.valueOf(any()));
        verify(model, never()).asyncFindVocablesWithName(any(String.class));
    }

    @Test
    public void onSaveVocableRequest_UsingVocableWithEmptyName_OrderToViewTo_showErrorMessage() {
        final Word VOCABLE_WITH_EMPTY_NAME = new Word(1, " ");
        when(view.getPojoUsedByView()).thenReturn(VOCABLE_WITH_EMPTY_NAME);

        presenter.onSaveVocableRequest();

        verify(view).showMessage(any(String.class));
        verify(model, never()).asyncFindVocablesWithName(any(String.class));
    }

    @Test
    public void onCreateTranslationsRequest_OrderToViewTo_goToTranslationsManipulationView() {
        presenter.onVocableToManipulateRetrieved(VOCABLE);

        presenter.onCreateTranslationRequest();

        verify(view).goToTranslationEditView(VOCABLE);
    }

    @Test
    public void onEventAsyncSaveVocableCompleted_returnToPreviousView() {
        EventAsyncSaveVocableCompleted asyncSaveVocableCompleted = new EventAsyncSaveVocableCompleted(VOCABLE.getId());

        presenter.onEvent(asyncSaveVocableCompleted);

        verify(view).returnToPreviousView();
    }

    @Test
    public void onEventAsyncUpdateVocableCompleted_returnToPreviousView() {
        final int FAKE_NUMBER_OF_VOCABLES_UPDATED = 1;

        EventAsyncUpdateVocableCompleted asyncUpdateVocableCompleted =
                new EventAsyncUpdateVocableCompleted(FAKE_NUMBER_OF_VOCABLES_UPDATED);

        presenter.onEvent(asyncUpdateVocableCompleted);

        verify(view).returnToPreviousView();
    }

    @Test
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewNotPersisted_SaveItIfHisNameIsUnique() {
        when(view.getPojoUsedByView()).thenReturn(NOT_PERSISTED_VOCABLE_IN_VIEW);

        EventAsyncSearchVocablesByNameCompleted eventAsyncSearchVocablesByNameCompleted =
                new EventAsyncSearchVocablesByNameCompleted(Collections.emptyList());

        presenter.onEvent(eventAsyncSearchVocablesByNameCompleted);

        verify(model).asyncSaveVocable(NOT_PERSISTED_VOCABLE_IN_VIEW);
    }

    @Test
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted_IfVocableInViewNotPersisted_DontSaveItIfHisNameIsDuplicated() {
        when(view.getPojoUsedByView()).thenReturn(NOT_PERSISTED_VOCABLE_IN_VIEW);

        populatePersistentListOfVocablesWithSameName(PERSISTED_VOCABLE_WITH_SAME_NAME_BUT_DIFFERENT_ID);

        EventAsyncSearchVocablesByNameCompleted eventAsyncSearchVocableByNameCompleted =
                new EventAsyncSearchVocablesByNameCompleted(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME);

        presenter.onEvent(eventAsyncSearchVocableByNameCompleted);

        verify(view).showMessage(any(String.class));
        verify(model, never()).asyncSaveVocable(any(Word.class));
    }

    @Test(expected = RuntimeException.class)
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted_IfCurrentVocableInViewStillPersistent_ButVocablesWithSameNameAreMoreThanOne_ThrowRuntimeException() {
        when(view.getPojoUsedByView()).thenReturn(PERSISTED_VOCABLE_IN_VIEW);

        populatePersistentListOfVocablesWithSameName(
                PERSISTED_VOCABLE_IN_VIEW,
                ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME
        );

        EventAsyncSearchVocablesByNameCompleted eventAsyncSearchVocablesByNameCompleted =
                new EventAsyncSearchVocablesByNameCompleted(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME);

        presenter.onEvent(eventAsyncSearchVocablesByNameCompleted);
    }

    @Test
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted__IfCurrentVocableInViewStillPersistent_ButHisNewNameToUpdateIsDuplicatedForOtherVocable_DontUpdateAndViewShowError() {
        when(view.getPojoUsedByView()).thenReturn(PERSISTED_VOCABLE_IN_VIEW);

        populatePersistentListOfVocablesWithSameName(ANOTHER_PERSISTENT_VOCABLE_WITH_SAME_NAME);

        EventAsyncSearchVocablesByNameCompleted event =
                new EventAsyncSearchVocablesByNameCompleted(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME);

        presenter.onEvent(event);

        verify(view).showMessage(any(String.class));
        verify(model, never()).asyncUpdateVocable(any(Long.class), any(Word.class));
    }

    @Test
    public void after_onSaveVocableRequest_onEventAsyncSearchVocablesByNameCompleted_IfCurrentVocableInViewStillPersistent_AndNewNameToUpdateNotDuplicated_UpdateVocable() {
        populatePersistentListOfVocablesWithSameName(VOCABLE);

        when(view.getPojoUsedByView()).thenReturn(VOCABLE_WITH_UPDATED_NAME);

        EventAsyncSearchVocablesByNameCompleted eventAsyncSearchVocablesByNameCompleted =
                new EventAsyncSearchVocablesByNameCompleted(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME);

        presenter.onEvent(eventAsyncSearchVocablesByNameCompleted);

        verify(model).asyncUpdateVocable(VOCABLE_WITH_UPDATED_NAME.getId(), VOCABLE_WITH_UPDATED_NAME);
    }

    private void populatePersistentListOfVocablesWithSameName(Word... words) {
        Collections.addAll(PERSISTENT_LIST_OF_VOCABLES_WITH_SAME_NAME, words);
    }

    private class DictionaryManipulationPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO model;

        DictionaryManipulationPresenterFactoryForTests(DictionaryDAO model) {
            this.model = model;
        }

        @Override
        public DictionaryVocableEditorPresenter create() {
            return new DictionaryVocableEditorPresenter(model);
        }
    }
}
