package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryVocablesManagerView;
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
import static junit.framework.Assert.assertNotNull;
import static com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableManipulationRequest.TypeOfManipulation;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author Matteo Veroni
 */

public class DictionaryVocablesManagerPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryVocablesManagerView view;
    @Mock
    private DictionaryDAO dictionaryModel;

    private DictionaryVocablesManagerPresenter presenter;

    private final EventBus eventBus = EventBus.getDefault();

    private Word VOCABLE = new Word(1, "VocableTest");

    @Before
    public void setUp() {
        presenter = new DictionaryManagementPresenterFactoryForTests(dictionaryModel).create();

        presenter.attachView(view);

        assertTrue(eventBus.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.destroy();

        assertFalse(eventBus.isRegistered(presenter));
    }

    @Test
    public void onEvent_VocableSelected_View_goToManipulationView_Using_Selected_Vocable() {
        EventVocableSelected vocableSelected = new EventVocableSelected(VOCABLE);

        presenter.onEvent(vocableSelected);

        verify(view).goToManipulationView(VOCABLE);
    }

    @Test
    public void onEvent_VocableManipulationDeleteRequest_DictionaryModel_Starts_asyncDeleteVocable() {
        EventVocableManipulationRequest vocableDeleteRequest
                = new EventVocableManipulationRequest(VOCABLE, TypeOfManipulation.REMOVE);

        presenter.onEvent(vocableDeleteRequest);

        verify(dictionaryModel).asyncDeleteVocable(VOCABLE.getId());
    }

    @Test
    public void onEvent_AsyncVocableDeletionComplete_View_showMessage_for_Deletion_Complete() {
        EventAsyncDeleteVocableCompleted asyncVocableDeletionComplete = new EventAsyncDeleteVocableCompleted(1);

        presenter.onEvent(asyncVocableDeletionComplete);

        verify(view).showMessage(any(String.class));
    }

    private class DictionaryManagementPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO model;

        DictionaryManagementPresenterFactoryForTests(DictionaryDAO model) {
            this.model = model;
        }

        @Override
        public DictionaryVocablesManagerPresenter create() {
            return new DictionaryVocablesManagerPresenter(model);
        }
    }
}


