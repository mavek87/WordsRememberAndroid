package com.matteoveroni.wordsremember.scene_dictionary.presenter;

import com.matteoveroni.wordsremember.scene_dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventVocableManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventVocableSelected;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.scene_dictionary.view.ManageVocablesView;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.LocaleKey;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * @author Matteo Veroni
 */

public class ManageVocablesPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private ManageVocablesView view;
    @Mock
    private DictionaryDAO dao;
    @Mock
    private DictionaryModel model;

    private ManageVocablesPresenter presenter;

    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private static final Word EMPTY_VOCABLE = new Word("");
    private static final Word VOCABLE = new Word(1, "VocableTest");
    private static final ArgumentCaptor<Word> vocablePassedToModelCaptor = ArgumentCaptor.forClass(Word.class);

    @Before
    public void setUp() {
        presenter = new DictionaryVocablesPresenterFactoryForTests(model, dao).create();
        presenter.attachView(view);
        assertTrue("Presenter should be registered to event bus before each test", EVENT_BUS.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.detachView();
        assertFalse("Presenter should be unregistered to event bus after each test", EVENT_BUS.isRegistered(presenter));
    }

    @Test
    public void onCreateVocableRequest_View_goToEditVocableView() {
        presenter.onCreateVocableRequest();

        verify(view).switchToView(View.Name.EDIT_VOCABLE);
    }

    @Test
    public void onCreateVocableRequest_EmptyVocable_Is_setAsLastValidVocableSelected_InModel() {
        presenter.onCreateVocableRequest();

        verify(model).setVocableSelected(vocablePassedToModelCaptor.capture());
        assertTrue(
                "last valid vocable selected set in the model should be an empty vocable",
                vocablePassedToModelCaptor.getValue().equals(EMPTY_VOCABLE)
        );
    }

    @Test
    public void onEventVocableSelected_Model_saveIt() {
        EventVocableSelected eventVocableSelected = new EventVocableSelected(VOCABLE);

        presenter.onEvent(eventVocableSelected);

        verify(model).setVocableSelected(vocablePassedToModelCaptor.capture());
        assertEquals(eventVocableSelected.getSelectedVocable(), vocablePassedToModelCaptor.getValue());
    }

    @Test
    public void onEventVocableSelected_View_goToEditVocableView() {
        presenter.onEvent(new EventVocableSelected(VOCABLE));

        verify(view).switchToView(View.Name.EDIT_VOCABLE);
    }

    @Test
    public void onEventVocableDeleteRequest_DAO_Starts_AsyncDeleteVocable() {
        presenter.onEvent(new EventVocableManipulationRequest(VOCABLE, TypeOfManipulationRequest.REMOVE));

        verify(dao).asyncDeleteVocable(VOCABLE.getId());
    }

    @Test
    public void onEventAsyncDeleteVocableComplete_View_showMessageDeleteComplete() {
        final int FAKE_NUMBER_OF_ROWS_DELETED = 1;

        presenter.onEvent(new EventAsyncDeleteVocableCompleted(FAKE_NUMBER_OF_ROWS_DELETED));

        verify(view).showMessage(LocaleKey.VOCABLE_REMOVED);
    }

    private class DictionaryVocablesPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO dao;
        private DictionaryModel model;

        DictionaryVocablesPresenterFactoryForTests(DictionaryModel model, DictionaryDAO dao) {
            this.model = model;
            this.dao = dao;
        }

        @Override
        public ManageVocablesPresenter create() {
            return new ManageVocablesPresenter(model, dao);
        }
    }
}


