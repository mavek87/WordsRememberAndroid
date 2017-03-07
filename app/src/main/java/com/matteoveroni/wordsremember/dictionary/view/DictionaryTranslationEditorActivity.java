package com.matteoveroni.wordsremember.dictionary.view;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryTranslationEditorPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryTranslationEditorPresenterFactory;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.pojos.TranslationForVocable;

import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class DictionaryTranslationEditorActivity extends AppCompatActivity implements DictionaryTranslationEditor, LoaderManager.LoaderCallbacks<DictionaryTranslationEditorPresenter> {

    public static final String TAG = TagGenerator.tag(DictionaryTranslationEditorActivity.class);

    private DictionaryTranslationEditorPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    private TranslationEditorFragment translationEditorFragment;

    @Override
    public void saveTranslationAction() {
        presenter.onSaveTranslationForVocableRequest();
    }

    @Override
    public TranslationForVocable getPojoUsedByView() {
        return translationEditorFragment.getPojoUsedByView();
    }

    @Override
    public void setPojoUsedInView(TranslationForVocable translationForVocable) {
        this.translationEditorFragment.setPojoUsedInView(translationForVocable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_translation_editor);
        ButterKnife.bind(this);

        TranslationEditorFragment translationEditorFragment = (TranslationEditorFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translation_editor_fragment);

        setupAndShowToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.title_activity_dictionary_translations);
        }
        setSupportActionBar(toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_management_editing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
//                presenter.onSaveVocableRequest(TranslationEditorFragment.getPojoUsedByView());
                return true;
        }
        return false;
    }

    @Override
    public Loader<DictionaryTranslationEditorPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new DictionaryTranslationEditorPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<DictionaryTranslationEditorPresenter> loader, DictionaryTranslationEditorPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<DictionaryTranslationEditorPresenter> loader) {
        presenter = null;
    }
}
