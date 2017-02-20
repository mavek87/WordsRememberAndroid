package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncVocableDeletionComplete;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManagementView;
import com.matteoveroni.wordsremember.pojo.Word;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertNotNull;
import static com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest.TypeOfManipulation;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author Matteo Veroni
 */

public class DictionaryManagementPresenterTest {

    private DictionaryManagementPresenter presenter;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryManagementView view;
    @Mock
    private DictionaryDAO dictionaryModel;

    private Word VOCABLE = new Word(1, "VocableTest");

    @Before
    public void setUp() {
        presenter = new DictionaryManagementPresenterFactoryForTests(dictionaryModel).create();
        presenter.onViewAttached(view);
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
        EventAsyncVocableDeletionComplete asyncVocableDeletionComplete = new EventAsyncVocableDeletionComplete(1);

        presenter.onEvent(asyncVocableDeletionComplete);

        verify(view).showMessage(any(String.class));
    }

    private class DictionaryManagementPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO model;

        DictionaryManagementPresenterFactoryForTests(DictionaryDAO model) {
            this.model = model;
        }

        @Override
        public DictionaryManagementPresenter create() {
            return new DictionaryManagementPresenter(model);
        }
    }
}


