package com.matteoveroni.wordsremember.dictionary.view;

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

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManipulationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.pojos.Word;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationActivity extends AppCompatActivity
        implements DictionaryManipulationView, LoaderManager.LoaderCallbacks<DictionaryManipulationPresenter> {

    @SuppressWarnings("unused")
    public static final String TAG = TagGenerator.tag(DictionaryManipulationActivity.class);

    private DictionaryManipulationPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    private DictionaryManipulationFragment manipulationFragment;

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void returnToPreviousView() {
        onBackPressed();
    }

    @Override
    public void showVocableData(Word vocable) {
        manipulationFragment.showVocableData(vocable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manipulation);

        manipulationFragment = (DictionaryManipulationFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dictionary_manipulation_fragment);

        setupAndShowToolbar();
        disableAutoShowInputKeyboardWhenActivityShowsUp();
        loadPresenter();
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(WordsRemember.NAME + " - Details");
        }
        setSupportActionBar(toolbar);
    }

    private void disableAutoShowInputKeyboardWhenActivityShowsUp() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void loadPresenter() {
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        Word vocableToManipulate = catchVocableToManipulate();
        if (vocableToManipulate != null) {
            presenter.onVocableToManipulateRetrieved(vocableToManipulate);
        } else {
            returnToPreviousView();
        }
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
                presenter.onSaveVocableRequest(manipulationFragment.getVocableDataInView());
                return true;
        }
        return false;
    }

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

    private Word catchVocableToManipulate() {
        Intent starterIntent = getIntent();
        if (starterIntent.hasExtra(Extras.VOCABLE_TO_MANIPULATE)) {
            String json_vocableToManipulate = starterIntent.getStringExtra(Extras.VOCABLE_TO_MANIPULATE);
            return Word.fromJson(json_vocableToManipulate);
        }
        return null;
    }
}
