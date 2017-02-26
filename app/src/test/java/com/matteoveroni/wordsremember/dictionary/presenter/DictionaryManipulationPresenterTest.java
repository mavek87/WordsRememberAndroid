package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncUpdateVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManipulationView;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by Matteo Veroni
 */

public class DictionaryManipulationPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryManipulationView view;
    @Mock
    private DictionaryDAO model;

    private DictionaryManipulationPresenter presenter;

    private static final EventBus eventBus = EventBus.getDefault();

    private final Word VOCABLE = new Word(1, "VocableTest");

    @Before
    public void setUp() {
        presenter = new DictionaryManipulationPresenterFactoryForTests(model).create();

        presenter.attachView(view);

        assertTrue(eventBus.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.destroy();

        assertFalse(eventBus.isRegistered(presenter));
    }

    @Test
    public void onVocableToManipulateRetrieved_OrderToViewTo_showVocableData() {
        presenter.onVocableToManipulateRetrieved(VOCABLE);

        verify(view).showVocableData(VOCABLE);
    }

    @Test
    public void onSaveVocableRequest_ForNewVocableToCreate_OrderToModelToCall_asyncSaveVocableRequest() {
        final Word NEW_VOCABLE_TO_CREATE = new Word(-1, "newVocable");

        presenter.onSaveVocableRequest(NEW_VOCABLE_TO_CREATE);

        verify(model).asyncSaveVocable(NEW_VOCABLE_TO_CREATE);
    }

    @Test
    public void onSaveVocableRequest_ForExistingVocableUpdatedInView_OrderToModelToCall_asyncUpdateVocableRequest() {
        final Word INITIAL_VOCABLE_IN_VIEW = VOCABLE;
        final Word UPDATED_VOCABLE_IN_VIEW = new Word(INITIAL_VOCABLE_IN_VIEW.getId(), "modifiedText");

        presenter.onSaveVocableRequest(UPDATED_VOCABLE_IN_VIEW);

        verify(model).asyncUpdateVocable(INITIAL_VOCABLE_IN_VIEW.getId(), UPDATED_VOCABLE_IN_VIEW);
    }

    @Test
    public void onSaveVocableRequest_UsingNullVocable_OrderToViewTo_showErrorMessage() {
        presenter.onSaveVocableRequest(null);

        verify(view).showMessage(String.valueOf(any()));
    }

    @Test
    public void onSaveVocableRequest_UsingVocableWithEmptyName_OrderToViewTo_showErrorMessage() {
        final Word VOCABLE_WITH_EMPTY_NAME = new Word(1, " ");

        presenter.onSaveVocableRequest(VOCABLE_WITH_EMPTY_NAME);

        verify(view).showMessage(String.valueOf(any()));
    }

    @Test
    public void onEventAsyncSaveVocableCompleted_returnToPreviousView() {
        EventAsyncSaveVocableCompleted asyncSaveVocableCompleted = new EventAsyncSaveVocableCompleted(VOCABLE.getId());

        presenter.onEvent(asyncSaveVocableCompleted);

        verify(view).returnToPreviousView();
    }

    @Test
    public void onEventAsyncUpdateVocableCompleted_returnToPreviousView() {
        final int FAKE_NUMBER_OF_UPDATED_VOCABLES = 1;
        EventAsyncUpdateVocableCompleted asyncUpdateVocableCompleted = new EventAsyncUpdateVocableCompleted(FAKE_NUMBER_OF_UPDATED_VOCABLES);

        presenter.onEvent(asyncUpdateVocableCompleted);

        verify(view).returnToPreviousView();
    }

    private class DictionaryManipulationPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO model;

        DictionaryManipulationPresenterFactoryForTests(DictionaryDAO model) {
            this.model = model;
        }

        @Override
        public DictionaryManipulationPresenter create() {
            return new DictionaryManipulationPresenter(model);
        }
    }
}
