package com.matteoveroni.wordsremember.scene_dictionary.presenter;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.events.TypeOfManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.events.translation.EventTranslationSelected;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventVocableTranslationManipulationRequest;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.scene_dictionary.view.activities.AddTranslationView;

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
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */

public class AddTranslationPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryDAO dao;
    @Mock
    private AddTranslationView view;
    @Mock
    private DictionaryModel model;

    private AddTranslationPresenter presenter;

    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private static final Word TRANSLATION = new Word(1, "translation");
    private static final Word FAKE_TRANSLATION_SELECTED = new Word(1, "fake_selected_translation");

    @Before
    public void setUp() {
        presenter = new DictionaryAddTranslationPresenterFactoryForTests(model, dao).create();

        when(model.getTranslationSelected()).thenReturn(TRANSLATION);
        presenter.attachView(view);

        assertTrue("Presenter should be registered to eventbus before each test", EVENT_BUS.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.detachView();

        assertFalse("Presenter should be unregistered to eventbus after each test", EVENT_BUS.isRegistered(presenter));
    }

    @Test
    public void onCreateTranslationRequest_View_goToEditTranslationView() {
        presenter.onCreateTranslationRequest();
        verify(view).switchToView(View.Name.EDIT_TRANSLATION, AddTranslationPresenter.EDIT_TRANSLATION_REQUEST_CODE);
    }

    @Test
    public void onEventTranslationSelected_View_showMessage_And_returnToPreviousView() {
        presenter.onEvent(new EventTranslationSelected(FAKE_TRANSLATION_SELECTED));

        verify(view).showMessage(any(FormattedString.class));
        verify(view).returnToPreviousView();
    }

    @Test
    public void onEventTranslationSelected_Model_setLastValidTranslationSelected() {
        presenter.onEvent(new EventTranslationSelected(FAKE_TRANSLATION_SELECTED));

        verify(model).setTranslationSelected(FAKE_TRANSLATION_SELECTED);
    }

    @Test
    public void onEventVocableTranslationDeleteRequest_Dao_asyncDeleteVocableTranslationsByTranslationId() {
        final long fakeVocablenId = 1;
        final long fakeTranslationId = 1;

        presenter.onEvent(new EventVocableTranslationManipulationRequest(fakeVocablenId, fakeTranslationId, TypeOfManipulationRequest.REMOVE));

        verify(dao).asyncDeleteVocableTranslationsByTranslationId(fakeTranslationId);
    }

    private class DictionaryAddTranslationPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO dao;
        private DictionaryModel model;

        DictionaryAddTranslationPresenterFactoryForTests(DictionaryModel model, DictionaryDAO dao) {
            this.dao = dao;
            this.model = model;
        }

        @Override
        public AddTranslationPresenter create() {
            return new AddTranslationPresenter(model, dao);
        }
    }
}
