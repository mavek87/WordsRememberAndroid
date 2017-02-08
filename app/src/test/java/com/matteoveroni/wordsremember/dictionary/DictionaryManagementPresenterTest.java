package com.matteoveroni.wordsremember.dictionary;

import com.matteoveroni.wordsremember.dictionary.factories.DictionaryManagementPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.pojo.Word;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertNotNull;

/**
 * @author Matteo Veroni
 */

public class DictionaryManagementPresenterTest {

    public MockitoRule mockitoRule = MockitoJUnit.rule();

    DictionaryManagementPresenter presenter = new DictionaryManagementPresenterFactory().create();
    @Mock
    DictionaryManagementView view;
    @Mock
    DictionaryDAO model;

    @Test
    public void onCreateVocableRequestPresenterOrderToViewToGoToManipulationView() {
        presenter.onCreateVocableRequest();
//        Mockito.verify(view).goToManipulationView(Matchers.any(Word.class));
    }

}
