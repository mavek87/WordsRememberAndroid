package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryTranslationEditorView;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.pojos.TranslationForVocable;
import com.matteoveroni.wordsremember.pojos.Word;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Matteo Veroni
 */
public class DictionaryTranslationEditorViewPresenterTest {

    private DictionaryTranslationEditorPresenter presenter;
    private static final EventBus EVENT_BUS = EventBus.getDefault();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryTranslationEditorView view;
    @Mock
    private DictionaryDAO model;

    private final Word VOCABLE = new Word(1, "vocable");
    private final Word VOCABLE_TRANSLATION = new Word("translationForVocable");
    private final TranslationForVocable TRANSLATION_FOR_VOCABLE = new TranslationForVocable(VOCABLE, VOCABLE_TRANSLATION);

    @Before
    public void setUp() {
        presenter = new DictionaryTranslationEditorPresenterFactoryForTests(model).create();
        presenter.attachView(view);
        assertTrue("Presenter should be registered to eventbus before each test", EVENT_BUS.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        presenter.destroy();

        assertFalse("Presenter should be unregistered to eventbus after each test", EVENT_BUS.isRegistered(presenter));
    }

    @Test
    public void onSaveTranslationForVocableRequest_WithValidTranslationForExistingVocable_Call_asyncSaveTranslationForVocable() {
        when(view.getPojoUsedByView()).thenReturn(TRANSLATION_FOR_VOCABLE);

        presenter.onSaveTranslationForVocableRequest();

        final ArgumentCaptor<Word> argTranslation = ArgumentCaptor.forClass(Word.class);
        final ArgumentCaptor<Word> argVocable = ArgumentCaptor.forClass(Word.class);

        verify(model).asyncSaveTranslationForVocable(argTranslation.capture(), argVocable.capture());

        assertEquals(
                "Model should save translation taken from view",
                TRANSLATION_FOR_VOCABLE.getTranslation(), argTranslation.getValue()
        );
        assertEquals(
                "Model should save translation for vocable used by view",
                TRANSLATION_FOR_VOCABLE.getVocable(),
                argVocable.getValue()
        );
    }

    private class DictionaryTranslationEditorPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO model;

        DictionaryTranslationEditorPresenterFactoryForTests(DictionaryDAO model) {
            this.model = model;
        }

        @Override
        public DictionaryTranslationEditorPresenter create() {
            return new DictionaryTranslationEditorPresenter(model);
        }
    }
}