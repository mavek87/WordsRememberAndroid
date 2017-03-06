package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryTranslationEditorView;
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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * @author Matteo Veroni
 */
public class DictionaryTranslationEditorPresenterTest {

    private static final EventBus eventBus = EventBus.getDefault();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryTranslationEditorView view;
    @Mock
    private DictionaryDAO model;

    private DictionaryTranslationEditorPresenter presenter;

    @Before
    public void setUp() {
        presenter = new DictionaryTranslationEditorPresenterFactoryForTests(model).create();
        presenter.attachView(view);
        assertTrue("Presenter should be registered to eventbus before each test", eventBus.isRegistered(presenter));
    }

    @After
    public void tearDown() {
        assertFalse("Presenter should be unregistered to eventbus after each test", eventBus.isRegistered(presenter));
    }

    @Test
    public void onSaveTranslationRequest_forVocable_SaveTranslationForVocable() {
        Word translation = new Word("translation");
        presenter.onSaveTranslationRequest();
        verify(model).asyncSaveTranslationForVocable(translation, new Word("vocable"));
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