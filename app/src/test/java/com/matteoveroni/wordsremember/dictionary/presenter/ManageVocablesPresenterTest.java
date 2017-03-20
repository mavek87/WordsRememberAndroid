package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableSelected;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.view.ManageVocablesView;
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

import static com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableManipulationRequest.TypeOfManipulation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * @author Matteo Veroni
 */

public class ManageVocablesPresenterTest {

    private ManageVocablesPresenter presenter;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private ManageVocablesView view;
    @Mock
    private DictionaryDAO dictionaryDAO;

    private DictionaryModel model;

    private final EventBus eventBus = EventBus.getDefault();

    private Word VOCABLE = new Word(1, "VocableTest");

    @Before
    public void setUp() {
        presenter = new DictionaryVocablesManagerPresenterFactoryForTests(dictionaryDAO).create();
        presenter.attachView(view);
        assertTrue("Presenter should be registered to eventbus before each test", eventBus.isRegistered(presenter));

        model = WordsRemember.getDictionaryModel();
    }

    @After
    public void tearDown() {
        presenter.destroy();
        assertFalse("Presenter should be unregistered to eventbus after each test", eventBus.isRegistered(presenter));

        model = null;
    }

    @Test
    public void onEventVocableSelected_Model_saveVocableSelected() {
        EventVocableSelected eventVocableSelected = new EventVocableSelected(VOCABLE);

        presenter.onEvent(eventVocableSelected);

        assertEquals(eventVocableSelected.getSelectedVocable(), model.getLastValidVocableSelected());
    }

    @Test
    public void onEventVocableSelected_View_goToManipulationView() {
        EventVocableSelected eventVocableSelected = new EventVocableSelected(VOCABLE);

        presenter.onEvent(eventVocableSelected);

        verify(view).goToEditVocableView();
    }

    @Test
    public void onEventVocableDeleteRequest_DAO_Starts_asyncDeleteVocable() {
        EventVocableManipulationRequest eventVocableDeleteRequest
                = new EventVocableManipulationRequest(VOCABLE, TypeOfManipulation.REMOVE);

        presenter.onEvent(eventVocableDeleteRequest);

        verify(dictionaryDAO).asyncDeleteVocable(VOCABLE.getId());
    }

    @Test
    public void onEventAsyncVocableDeleteComplete_View_showMessageDeleteComplete() {
        EventAsyncDeleteVocableCompleted eventAsyncVocableDeletionComplete = new EventAsyncDeleteVocableCompleted(1);

        presenter.onEvent(eventAsyncVocableDeletionComplete);

        verify(view).showMessage(anyString());
    }

    private class DictionaryVocablesManagerPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO dao;

        DictionaryVocablesManagerPresenterFactoryForTests(DictionaryDAO dao) {
            this.dao = dao;
        }

        @Override
        public ManageVocablesPresenter create() {
            return new ManageVocablesPresenter(dao);
        }
    }
}


