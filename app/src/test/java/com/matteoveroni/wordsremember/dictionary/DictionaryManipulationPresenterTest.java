package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.pojo.Word;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;

/**
 * Created by Matteo Veroni
 */

public class DictionaryManipulationPresenterTest {

    private DictionaryManipulationPresenter presenter;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private DictionaryManipulationView view;
    @Mock
    private DictionaryDAO model;

    private Word VOCABLE = new Word(1, "VocableTest");

    @Before
    public void setUp() {
        presenter = new DictionaryManipulationtPresenterFactoryForTests(model).create();
        presenter.onViewAttached(view);
    }

    @Test
    public void on_Null_VocableToManipulateRetrieved_View_populateViewFor_Null_Vocable() {
        presenter.onVocableToManipulateRetrieved(null);

        verify(view).showVocableData(null);
    }

    @Test
    public void on_NotNull_VocableToManipulateRetrieved_View_populateViewFor_NotNull_Vocable() {
        presenter.onVocableToManipulateRetrieved(VOCABLE);

        verify(view).showVocableData(VOCABLE);
    }

    private class DictionaryManipulationtPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO model;

        DictionaryManipulationtPresenterFactoryForTests(DictionaryDAO model) {
            this.model = model;
        }

        @Override
        public DictionaryManipulationPresenter create() {
            return new DictionaryManipulationPresenter(model);
        }
    }
}
