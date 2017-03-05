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
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManipulationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class DictionaryManipulationActivity extends AppCompatActivity implements DictionaryManipulationView, LoaderManager.LoaderCallbacks<DictionaryManipulationPresenter> {

    public static final String TAG = TagGenerator.tag(DictionaryManipulationActivity.class);

    private DictionaryManipulationPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    private DictionaryManipulationFragment manipulationFragment;
    private TranslationsManagementFragment translationsManagementFragment;

    @Override
    public void showVocableData(Word vocable) {
        manipulationFragment.setPojoUsedInView(vocable);
        translationsManagementFragment.setPojoUsedInView(vocable);
    }

    @Override
    @OnClick(R.id.dictionary_manipulation_floating_action_button)
    @SuppressWarnings("unused")
    public void createTranslationAction() {
        presenter.onCreateTranslationRequest();
    }

    @Override
    public void goToTranslationsManipulationView(Word vocable) {
        Intent intent_goToTranslationsManipulationView = new Intent(getApplicationContext(), DictionaryTranslationsActivity.class);
        intent_goToTranslationsManipulationView.putExtra(
                Extras.VOCABLE,
                vocable.toJson()
        );
        startActivity(intent_goToTranslationsManipulationView);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manipulation);
        ButterKnife.bind(this);

        manipulationFragment = (DictionaryManipulationFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_manipulation_fragment);
        translationsManagementFragment = (TranslationsManagementFragment) getSupportFragmentManager().findFragmentById(R.id.translations_management_fragment);

        setupAndShowToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.title_activity_dictionary_manipulation);
        }
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        Word vocableToManipulate = findVocableToManipulate();
        presenter.onVocableToManipulateRetrieved(vocableToManipulate);
    }

    private Word findVocableToManipulate() {
        Intent activityStarterIntent = getIntent();
        if (activityStarterIntent.hasExtra(Extras.VOCABLE)) {
            String json_vocableToManipulate = activityStarterIntent.getStringExtra(Extras.VOCABLE);
            return Word.fromJson(json_vocableToManipulate);
        } else {
            final String errorMessage = "Unexpected Error: Any vocable to manipulate retrieved.";
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
        getMenuInflater().inflate(R.menu.menu_dictionary_management_editing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                presenter.onSaveVocableRequest(manipulationFragment.getPojoUsedByView());
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
}
