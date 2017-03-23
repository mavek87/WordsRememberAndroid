package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.matteoveroni.wordsremember.dictionary.presenter.factories.ManageVocablesPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.view.ManageVocablesView;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.ManageVocablesPresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dictionary Management Activity
 *
 * @author Matteo Veroni
 */

public class ManageVocablesActivity extends AppCompatActivity implements ManageVocablesView, LoaderManager.LoaderCallbacks<ManageVocablesPresenter> {

    private ManageVocablesPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manage_vocables);
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        presenter.destroy();
        super.onStop();
    }

    @OnClick(R.id.add_vocable_floating_action_button)
    @Override
    public void createVocableAction() {
        presenter.onCreateVocableRequest();
    }

    @Override
    public void goToEditVocableView() {
        final Intent intent_goToEditVocableView = new Intent(getApplicationContext(), EditVocableActivity.class);
        startActivity(intent_goToEditVocableView);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<ManageVocablesPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new ManageVocablesPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<ManageVocablesPresenter> loader, ManageVocablesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<ManageVocablesPresenter> loader) {
        presenter = null;
    }
}
