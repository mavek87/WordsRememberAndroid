package com.matteoveroni.wordsremember.scene_report;

import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BasePresentedActivityView;

/**
 * @author Matteo Veroni
 */

public class QuizGameReportActivity extends BasePresentedActivityView implements QuizGameReportView {

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PresenterFactories.getFactory(PresenterFactoryName.QUIZ_GAME_REPORT_PRESENTER_FACTORY);
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {

    }
}
