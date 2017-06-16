package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.content.Intent;
import android.os.Bundle;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.ManageVocablesPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.ManageVocablesPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.view.ManageVocablesView;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dictionary Management Activity
 *
 * @author Matteo Veroni
 */

public class ManageVocablesActivityView extends BaseActivityPresentedView implements ManageVocablesView {

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

    @OnClick(R.id.add_vocable_floating_action_button)
    @Override
    public void createVocableAction() {
        presenter.onCreateVocableRequest();
    }

    @Override
    public void goToEditVocableView() {
        final Intent intent_goToEditVocableView = new Intent(getApplicationContext(), EditVocableActivityView.class);
        startActivity(intent_goToEditVocableView);
    }
}
