package com.matteoveroni.wordsremember.dictionary.presenter;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.matteoveroni.wordsremember.dictionary.events.vocable.EventVocableManipulationRequest.TypeOfManipulation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
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
    private DictionaryDAO dao;
    @Mock
    private DictionaryModel model;

    private static final EventBus eventBus = EventBus.getDefault();

    private final Word EMPTY_VOCABLE = new Word("");
    private final Word VOCABLE = new Word(1, "VocableTest");
    private final ArgumentCaptor<Word> vocablePassedToModelCaptor = ArgumentCaptor.forClass(Word.class);

    @Before
    public void setUp() {
        presenter = new DictionaryVocablesManagerPresenterFactoryForTests(model, dao).create();
        presenter.attachView(view);
        assertTrue("Presenter should be registered to event bus before each test", eventBus.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.destroy();
        assertFalse("Presenter should be unregistered to event bus after each test", eventBus.isRegistered(presenter));
    }

    @Test
    public void onCreateVocableRequest_View_goToEditVocableView() {
        presenter.onCreateVocableRequest();

        verify(view).goToEditVocableView();
    }

    @Test
    public void onCreateVocableRequest_EmptyVocable_Is_setAsLastValidVocableSelected_InTheModel() {
        presenter.onCreateVocableRequest();

        verify(model).setLastValidVocableSelected(vocablePassedToModelCaptor.capture());
        assertTrue(
                "last valid vocable selected set in the model should be an empty vocable",
                vocablePassedToModelCaptor.getValue().equals(EMPTY_VOCABLE)
        );
    }

    @Test
    public void onEventVocableSelected_Model_saveVocableSelected() {
        EventVocableSelected eventVocableSelected = new EventVocableSelected(VOCABLE);

        presenter.onEvent(eventVocableSelected);

        verify(model).setLastValidVocableSelected(vocablePassedToModelCaptor.capture());
        assertEquals(eventVocableSelected.getSelectedVocable(), vocablePassedToModelCaptor.getValue());
    }

    @Test
    public void onEventVocableSelected_View_goToEditVocableView() {
        EventVocableSelected eventVocableSelected = new EventVocableSelected(VOCABLE);

        presenter.onEvent(eventVocableSelected);

        verify(view).goToEditVocableView();
    }

    @Test
    public void onEventVocableDeleteRequest_DAO_Starts_AsyncDeleteVocable() {
        EventVocableManipulationRequest vocableDeleteRequest = new EventVocableManipulationRequest(VOCABLE, TypeOfManipulation.REMOVE);

        presenter.onEvent(vocableDeleteRequest);

        verify(dao).asyncDeleteVocable(VOCABLE.getId());
    }

    @Test
    public void onEventAsyncDeleteVocableComplete_View_showMessageDeleteComplete() {
        EventAsyncDeleteVocableCompleted vocableDeletionComplete = new EventAsyncDeleteVocableCompleted(1);

        presenter.onEvent(vocableDeletionComplete);

        verify(view).showMessage(anyString());
    }

    private class DictionaryVocablesManagerPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO dao;
        private DictionaryModel model;

        DictionaryVocablesManagerPresenterFactoryForTests(DictionaryModel model, DictionaryDAO dao) {
            this.model = model;
            this.dao = dao;
        }

        @Override
        public ManageVocablesPresenter create() {
            return new ManageVocablesPresenter(model, dao);
        }
    }
}


