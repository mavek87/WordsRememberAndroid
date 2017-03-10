package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSaveVocableTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.TranslationEditView;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */
public class TranslationEditViewPresenterTest {

    private static final EventBus EVENT_BUS = EventBus.getDefault();

    private TranslationEditPresenter presenter;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private TranslationEditView view;
    @Mock
    private DictionaryDAO dictionary;

    private final Word VOCABLE = new Word(1, "vocable");
    private final Word TRANSLATION = new Word("translation");
    private final Word TRANSLATION_WITH_EMPTY_NAME = new Word(" ");
    private final VocableTranslation TRANSLATION_FOR_VOCABLE = new VocableTranslation(VOCABLE, TRANSLATION);
    private final VocableTranslation EMPTY_TRANSLATION_NAME_FOR_VOCABLE = new VocableTranslation(null, TRANSLATION_WITH_EMPTY_NAME);

    @Before
    public void setUp() {
        presenter = new PresenterFactoryForTests(dictionary).create();
        presenter.attachView(view);
        assertTrue("Presenter should be registered to eventbus before each test", EVENT_BUS.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.destroy();
        assertFalse("Presenter should be unregistered to eventbus after each test", EVENT_BUS.isRegistered(presenter));
    }

    @Test
    public void onSaveTranslationForVocableRequest_Succeed_Call_asyncSaveTranslationForVocable() {
        when(view.getPojoUsedByView()).thenReturn(TRANSLATION_FOR_VOCABLE);

        presenter.onSaveTranslationForVocableRequest();

        verify(dictionary).asyncSaveVocableTranslation(TRANSLATION_FOR_VOCABLE);
    }

    @Test
    public void onSaveTranslationForVocableRequest_WithEmptyTranslationName_OrderToViewToShowErrorMessage() {
        when(view.getPojoUsedByView()).thenReturn(EMPTY_TRANSLATION_NAME_FOR_VOCABLE);

        presenter.onSaveTranslationForVocableRequest();

        verify(view).showMessage(anyString());
        verify(dictionary, never()).asyncSaveVocableTranslation(any(VocableTranslation.class));
    }

    @Test
    public void onEventAsyncSaveVocableTranslationCompletedSuccessfully_returnToPreviousView() {
        presenter.onEvent(new EventAsyncSaveVocableTranslationCompleted(1, 1));

        verify(view).returnToPreviousView();
    }

    private class PresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO dictionaryDAO;

        PresenterFactoryForTests(DictionaryDAO dictionaryDAO) {
            this.dictionaryDAO = dictionaryDAO;
        }

        @Override
        public TranslationEditPresenter create() {
            return new TranslationEditPresenter(dictionaryDAO);
        }
    }
}