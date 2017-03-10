package com.matteoveroni.wordsremember.dictionary.view;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.matteoveroni.wordsremember.dictionary.presenter.factories.VocablesManagerPresenterFactory;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.VocablesManagerPresenter;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dictionary Management Activity
 *
 * @author Matteo Veroni
 */

public class VocablesManagerActivity extends AppCompatActivity implements VocablesManagerView, LoaderManager.LoaderCallbacks<VocablesManagerPresenter> {

    private VocablesManagerPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    public VocablesManagerActivity() {
    }

    @OnClick(R.id.dictionary_management_floating_action_button)
    @Override
    public void createVocableAction() {
        presenter.onCreateVocableRequest();
    }

    @Override
    public void goToVocableEditView(Word vocableToEdit) {
        Intent intent_goToVocableEditor = new Intent(getApplicationContext(), VocableEditActivity.class);
        intent_goToVocableEditor.putExtra(
                Extras.VOCABLE,
                vocableToEdit.toJson()
        );
        startActivity(intent_goToVocableEditor);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_vocables_manager);
        ButterKnife.bind(this);

        loadPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        presenter.onViewCreated(getApplicationContext());
    }

    @Override
    protected void onStop() {
        presenter.destroy();
        super.onStop();
    }

    @Override
    public Loader<VocablesManagerPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new VocablesManagerPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<VocablesManagerPresenter> loader, VocablesManagerPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<VocablesManagerPresenter> loader) {
        presenter = null;
    }

    private void loadPresenter() {
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }
}
