package com.matteoveroni.wordsremember.scene_dictionary.view.activities;

import android.os.Bundle;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BasePresentedActivityView;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.ManageVocablesPresenter;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.ManageVocablesPresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.view.ManageVocablesView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dictionary Management Activity
 *
 * @author Matteo Veroni
 */

public class ManageVocablesActivity extends BasePresentedActivityView implements ManageVocablesView {

    private ManageVocablesPresenter presenter;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new ManageVocablesPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (ManageVocablesPresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manage_vocables);
        ButterKnife.bind(this);
        setupAndShowToolbar(getString(R.string.vocables_manager));
    }

    @OnClick(R.id.create_vocable_floating_action_button)
    @Override
    public void createVocableAction() {
        presenter.onCreateVocableRequest();
    }
}
