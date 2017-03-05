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
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryTranslationsManipulationPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryTranslationsManipulationPresenterFactory;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;

import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class DictionaryTranslationsActivity extends AppCompatActivity
        implements DictionaryTranslationsManipulationView, LoaderManager.LoaderCallbacks<DictionaryTranslationsManipulationPresenter> {

    public static final String TAG = TagGenerator.tag(DictionaryTranslationsActivity.class);

    private DictionaryTranslationsManipulationPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    private TranslationsManipulationFragment translationsManipulationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_translations);
        ButterKnife.bind(this);

        translationsManipulationFragment = (TranslationsManipulationFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translations_manipulation_fragment);

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
//                presenter.onSaveVocableRequest(translationsManipulationFragment.getPojoUsedByView());
                return true;
        }
        return false;
    }

    @Override
    public Loader<DictionaryTranslationsManipulationPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new DictionaryTranslationsManipulationPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<DictionaryTranslationsManipulationPresenter> loader, DictionaryTranslationsManipulationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<DictionaryTranslationsManipulationPresenter> loader) {
        presenter = null;
    }
}
