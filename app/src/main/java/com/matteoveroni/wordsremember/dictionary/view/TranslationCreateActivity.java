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
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.presenter.TranslationEditPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.TranslationEditPresenterFactory;
import com.matteoveroni.wordsremember.fragments.TranslationEditorFragment;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class TranslationCreateActivity extends AppCompatActivity implements TranslationCreateView, LoaderManager.LoaderCallbacks<TranslationEditPresenter> {

    public static final String TAG = TagGenerator.tag(TranslationCreateActivity.class);

    private TranslationEditorFragment translationEditorFragment;

    private TranslationEditPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    @Override
    public void saveTranslationAction() {
        presenter.onSaveTranslationRequest();
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
    public VocableTranslation getPojoUsedByView() {
        return translationEditorFragment.getPojoUsedByView();
    }

    @Override
    public void setPojoUsedInView(VocableTranslation vocableTranslation) {
        this.translationEditorFragment.setPojoUsedInView(vocableTranslation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        presenter.onVocableToTranslateRetrieved(findVocableToTranslate());
    }

    private Word findVocableToTranslate() {
        Intent starterIntent = getIntent();
        if (starterIntent.hasExtra(Extras.VOCABLE)) {
            String json_vocableToTranslate = starterIntent.getStringExtra(Extras.VOCABLE);
            return Word.fromJson(json_vocableToTranslate);
        } else {
            final String errorMessage = "Unexpected Error: No vocable to translate retrieved.";
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_translation_editor);
        ButterKnife.bind(this);
        setupAndShowToolbar();

        translationEditorFragment = (TranslationEditorFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translation_editor_fragment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            final String title = Str.concat(WordsRemember.ABBREVIATED_NAME, " - ", getString(R.string.title_activity_dictionary_translation_editor));
            toolbar.setTitle(title);
        }
        setSupportActionBar(toolbar);
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
                saveTranslationAction();
                return true;
        }
        return false;
    }

    @Override
    public Loader<TranslationEditPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new TranslationEditPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<TranslationEditPresenter> loader, TranslationEditPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<TranslationEditPresenter> loader) {
        presenter = null;
    }
}
