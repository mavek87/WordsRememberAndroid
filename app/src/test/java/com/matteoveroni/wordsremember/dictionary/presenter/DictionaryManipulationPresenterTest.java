package com.matteoveroni.wordsremember.dictionary.presenter;

import com.matteoveroni.wordsremember.PresenterFactory;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.view.DictionaryManipulationView;
import com.matteoveroni.wordsremember.pojos.Word;

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
        presenter = new DictionaryManipulationPresenterFactoryForTests(model).create();
        presenter.attachView(view);
    }

    @Test
    public void onVocableToManipulateRetrieved_View_showVocableData() {
        presenter.onVocableToManipulateRetrieved(VOCABLE);

        verify(view).showVocableData(VOCABLE);
    }

    private class DictionaryManipulationPresenterFactoryForTests implements PresenterFactory {
        private DictionaryDAO model;

        DictionaryManipulationPresenterFactoryForTests(DictionaryDAO model) {
            this.model = model;
        }

        @Override
        public DictionaryManipulationPresenter create() {
            return new DictionaryManipulationPresenter(model);
        }
    }
}
