package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.events.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManagementPresenter;
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
    private DictionaryDAO model;

    private Word VOCABLE = new Word(1, "VocableTest");

    @Before
    public void setUp() {
        presenter = new DictionaryManagementPresenterFactoryForTests(model).create();
        presenter.onViewAttached(view);
    }
//
//    @Test
//    public void onCreateVocableRequest_View_goToManipulationView() {
//        presenter.createVocableAction();
//
//        verify(view).goToManipulationView(null);
//    }

    @Test
    public void onEventVocableSelected_View_goToManipulationView_Using_Selected_Vocable() {
        EventVocableSelected eventVocableSelected = new EventVocableSelected(VOCABLE);

        presenter.onEvent(eventVocableSelected);

        verify(view).goToManipulationView(VOCABLE);
    }

    @Test
    public void onEventVocableManipulationDeleteRequest_Model_Starts_asyncDeleteVocable() {
        EventVocableManipulationRequest eventVocableDeleteRequest
                = new EventVocableManipulationRequest(VOCABLE, TypeOfManipulation.REMOVE);

        presenter.onEvent(eventVocableDeleteRequest);

        verify(model).asyncDeleteVocable(VOCABLE.getId());
    }

    @Test
    public void onEventAsyncDeleteVocableCompleted_View_Shows_A_Completion_Message() {
        EventAsyncDeleteVocableCompleted eventAsyncDeleteVocableCompleted = new EventAsyncDeleteVocableCompleted(1);

        presenter.onEvent(eventAsyncDeleteVocableCompleted);

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


