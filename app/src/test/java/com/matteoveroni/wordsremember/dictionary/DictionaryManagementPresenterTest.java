package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.PresenterFactory;
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

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private DictionaryManagementPresenter presenter;
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

    @Test
    public void test_onCreateVocableRequest_View_goToManipulationView() {
        Word nullVocable = null;

        presenter.onCreateVocableRequest();

        verify(view).goToManipulationView(nullVocable);
    }

    @Test
    public void test_onEventVocableSelected_View_goToManipulationView_Using_Selected_Vocable() {
        EventVocableSelected eventVocableSelected = new EventVocableSelected(VOCABLE);

        presenter.onEvent(eventVocableSelected);

        verify(view).goToManipulationView(VOCABLE);
    }

    @Test
    public void test_onEventVocableManipulationDeleteRequest_Model_Starts_asyncDeleteVocable() {
        EventVocableManipulationRequest eventVocableDeleteRequest = new EventVocableManipulationRequest(
                VOCABLE, TypeOfManipulation.REMOVE
        );

        presenter.onEvent(eventVocableDeleteRequest);

        verify(model).asyncDeleteVocable(VOCABLE.getId());
    }
//
//    @Test
//    public void test_onEventVocableSelected_View_goToManipulationView_Using_Selected_Vocable() {
//        final Word vocableSelected = new Word("VocableTest");
//        EventVocableSelected eventVocableSelected = new EventVocableSelected(vocableSelected);
//        presenter.onEvent(eventVocableSelected);
//
//        verify(view).goToManipulationView(vocableSelected);
//    }

}

class DictionaryManagementPresenterFactoryForTests implements PresenterFactory {
    private DictionaryDAO dao;

    public DictionaryManagementPresenterFactoryForTests(DictionaryDAO dao) {
        this.dao = dao;
    }

    @Override
    public DictionaryManagementPresenter create() {
        return new DictionaryManagementPresenter(dao);
    }
}
