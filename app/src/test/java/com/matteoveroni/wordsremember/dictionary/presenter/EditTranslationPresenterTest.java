package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSearchTranslationByNameCompleted;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.dictionary.view.EditTranslationView;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.localization.LocaleKey;

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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */

public class EditTranslationPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private EditTranslationView view;
    @Mock
    private DictionaryDAO dao;
    @Mock
    private DictionaryModel model;

    private EditTranslationPresenter presenter;

    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private static final Word VOCABLE = new Word(1, "vocable");
    private static final Word TRANSLATION = new Word("translation");
    private static final Word TRANSLATION_WITH_EMPTY_NAME = new Word(" ");
    private static final VocableTranslation TRANSLATION_FOR_VOCABLE = new VocableTranslation(VOCABLE, TRANSLATION);
    private static final VocableTranslation TRANSLATION_FOR_VOCABLE_WITH_EMPTY_NAME = new VocableTranslation(null, TRANSLATION_WITH_EMPTY_NAME);
    private static final long FAKE_NEW_SAVED_TRANSLATION_ID = 1;

    @Before
    public void setUp() {
        presenter = new PresenterFactoryForTests(model, dao).create();
        presenter.attachView(view);
        assertTrue("Presenter should be registered to eventbus before each test", EVENT_BUS.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.detachView();
        assertFalse("Presenter should be unregistered to eventbus after each test", EVENT_BUS.isRegistered(presenter));
    }

    @Test
    public void onSaveTranslationRequest_WithEmptyTranslationName_View_showsError() {
        when(view.getPojoUsed()).thenReturn(TRANSLATION_FOR_VOCABLE_WITH_EMPTY_NAME);

        presenter.onSaveTranslationRequest();

        verify(view).showMessage(LocaleKey.MSG_ERROR_TRYING_TO_STORE_INVALID_TRANSLATION);
        verify(dao, never()).asyncSearchTranslationByName(anyString());
    }

    @Test
    public void onSaveTranslationValidRequest_Dao_asyncSearchTranslationByName() {
        when(view.getPojoUsed()).thenReturn(TRANSLATION_FOR_VOCABLE);

        presenter.onSaveTranslationRequest();

        verify(dao).asyncSearchTranslationByName(TRANSLATION_FOR_VOCABLE.getTranslation().getName());
    }

    @Test
    public void onEventAsyncSearchTranslationByNameCompleted_IfADuplicateExists_View_showsError() {
        presenter.editedTranslationInView = TRANSLATION;

        presenter.onEvent(new EventAsyncSearchTranslationByNameCompleted(TRANSLATION));

        verify(view).showMessage(LocaleKey.MSG_ERROR_TRYING_TO_STORE_DUPLICATE_TRANSLATION_NAME);
    }

    @Test
    public void onEventAsyncSearchTranslationByNameCompleted_IfAnyDuplicateExists_Dao_asyncSaveTranslation() {
        presenter.editedTranslationInView = TRANSLATION;

        presenter.onEvent(new EventAsyncSearchTranslationByNameCompleted(null));

        verify(dao).asyncSaveTranslation(presenter.editedTranslationInView);
    }

    @Test
    public void onEventAsyncSaveTranslationCompleted_Model_setLastValidTranslationSelected() {
        presenter.editedTranslationInView = TRANSLATION;

        presenter.onEvent(new EventAsyncSaveTranslationCompleted(FAKE_NEW_SAVED_TRANSLATION_ID));

        verify(model).setTranslationSelected(TRANSLATION);
    }

    @Test
    public void onEventAsyncSaveTranslationCompleted_View_returnToPreviousView() {
        presenter.editedTranslationInView = TRANSLATION;

        presenter.onEvent(new EventAsyncSaveTranslationCompleted(FAKE_NEW_SAVED_TRANSLATION_ID));

        verify(view).returnToPreviousView();
    }

    private class PresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO dao;
        private DictionaryModel model;

        PresenterFactoryForTests(DictionaryModel dictionaryModel, DictionaryDAO dictionaryDAO) {
            this.dao = dictionaryDAO;
            this.model = dictionaryModel;
        }

        @Override
        public EditTranslationPresenter create() {
            return new EditTranslationPresenter(model, dao);
        }
    }
}