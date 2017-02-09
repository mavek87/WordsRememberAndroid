package com.matteoveroni.wordsremember.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.factories.DictionaryManipulationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.utilities.Json;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationActivity
        extends AppCompatActivity
        implements DictionaryManipulationView, LoaderManager.LoaderCallbacks<DictionaryManipulationPresenter> {

    private DictionaryManipulationPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    private Toolbar toolbar;

    private DictionaryManipulationFragment manipulationFragment;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // DictionaryManipulationView interface methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void returnToPreviousView() {
        onBackPressed();
    }

    @Override
    public void populateViewForVocable(Word vocable) {
        manipulationFragment.populateViewForVocable(vocable);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Android Lifecycle Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manipulation);

        manipulationFragment = (DictionaryManipulationFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dictionary_manipulation_fragment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(WordsRemember.TAG + " - Details");
        }
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
        presenter.onVocableToManipulateRetrieved(retrieveVocableToManipulate());
    }

    @Override
    protected void onStop() {
        presenter.onViewDetached();
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
                presenter.onSaveRequest(manipulationFragment.getCurrentVocableInView());
                return true;
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Presenter Loader methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Loader<DictionaryManipulationPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new DictionaryManipulationPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<DictionaryManipulationPresenter> loader, DictionaryManipulationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<DictionaryManipulationPresenter> loader) {
        presenter = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private Word retrieveVocableToManipulate() {
        Intent starterIntent = getIntent();
        if (starterIntent.hasExtra(Extras.VOCABLE_TO_MANIPULATE)) {
            String str_vocableToManipulate = starterIntent.getStringExtra(Extras.VOCABLE_TO_MANIPULATE);
            if (!str_vocableToManipulate.trim().isEmpty())
                return Json.getInstance().fromJson(str_vocableToManipulate, Word.class);
        }
        return null;
    }
}