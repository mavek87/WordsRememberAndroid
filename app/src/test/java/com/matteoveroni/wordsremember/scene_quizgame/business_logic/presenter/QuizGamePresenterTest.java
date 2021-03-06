package com.matteoveroni.wordsremember.scene_quizgame.business_logic.presenter;

import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.scene_dictionary.model.DictionaryModel;
import com.matteoveroni.wordsremember.scene_quizgame.view.QuizGameView;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Created by Matteo Veroni
 */

public class QuizGamePresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    QuizGameView view;
    @Mock
    DictionaryDAO dao;
    @Mock
    Settings settings;
    @Mock
    DictionaryModel model;

    QuizGamePresenter presenter = new QuizGamePresenterFactoryForTest(dao).create();

    @Test
    public void onAttachView_PresenterSubscribeToEventBus(){
//        presenter.attachView(view);
//        assertTrue(EventBus.getDefault().isRegistered(presenter));
    }

    private final class QuizGamePresenterFactoryForTest implements PresenterFactory {
        private final DictionaryDAO dao;

        QuizGamePresenterFactoryForTest(DictionaryDAO dao) {
            this.dao = dao;
        }

        @Override
        public QuizGamePresenter create() {
            return new QuizGamePresenter(settings, dao);
        }
    }

}
