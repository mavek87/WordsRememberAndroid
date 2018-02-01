package com.matteoveroni.wordsremember.scene_report;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.persistency.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.persistency.dao.StatisticsDAO;
import com.matteoveroni.wordsremember.scene_settings.model.Settings;

import javax.inject.Inject;

/**
 * @author Matteo Veroni
 */

public class QuizGameReportPresenterFactory implements PresenterFactory<QuizGameReportPresenter> {

    @Inject
    StatisticsDAO statisticsDAO;

    @Override
    public QuizGameReportPresenter create() {
        WordsRemember.getAppComponent().inject(this);
        return new QuizGameReportPresenter(statisticsDAO);
    }
}
