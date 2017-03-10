package com.matteoveroni.wordsremember.dictionary.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.VocableEditPresenterFactory;
import com.matteoveroni.wordsremember.fragments.TranslationsListFragment;
import com.matteoveroni.wordsremember.fragments.VocableEditorFragment;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.VocableEditPresenter;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class VocableEditActivity extends AppCompatActivity implements VocableEditView, LoaderManager.LoaderCallbacks<VocableEditPresenter> {

    public static final String TAG = TagGenerator.tag(VocableEditActivity.class);

    private VocableEditorFragment vocableEditorFragment;
    private TranslationsListFragment translationsListFragment;

    private VocableEditPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    @Override
    public void saveVocableAction() {
        presenter.onSaveVocableRequest();
    }

    @OnClick(R.id.dictionary_editor_floating_action_button)
    @Override
    public void createTranslationAction() {
        presenter.onCreateTranslationRequest();
    }

    @Override
    public void goToTranslationEditView(Word vocable) {
        Intent intent_goToTranslationsEditView = new Intent(getApplicationContext(), TranslationEditActivity.class);
        intent_goToTranslationsEditView.putExtra(
                Extras.VOCABLE,
                vocable.toJson()
        );
        startActivity(intent_goToTranslationsEditView);
    }

    @Override
    public void returnToPreviousView() {
        onBackPressed();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Word getPojoUsedByView() {
        return vocableEditorFragment.getPojoUsedByView();
    }

    @Override
    public void setPojoUsedInView(Word vocable) {
        vocableEditorFragment.setPojoUsedInView(vocable);
        translationsListFragment.setPojoUsedInView(vocable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_vocable_editor);
        ButterKnife.bind(this);

        vocableEditorFragment = (VocableEditorFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_vocable_editor_fragment);
        translationsListFragment = (TranslationsListFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translations_list_fragment);

        setupAndShowToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.title_activity_dictionary_vocable_editor);
        }
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        Word vocableToEdit = findVocableToEdit();
        presenter.onVocableToEditRetrieved(vocableToEdit);
    }

    private Word findVocableToEdit() {
        Intent starterIntent = getIntent();
        if (starterIntent.hasExtra(Extras.VOCABLE)) {
            String json_vocableToEdit = starterIntent.getStringExtra(Extras.VOCABLE);
            return Word.fromJson(json_vocableToEdit);
        } else {
            final String errorMessage = "Unexpected Error: No vocable to edit retrieved.";
            Log.e(TAG, errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    @Override
    protected void onStop() {
        presenter.destroy();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_vocable_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_done:
                saveVocableAction();
                return true;
        }
        return false;
    }

    @Override
    public Loader<VocableEditPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new VocableEditPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<VocableEditPresenter> loader, VocableEditPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<VocableEditPresenter> loader) {
        presenter = null;
    }
}