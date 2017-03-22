package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.Extras;
import com.matteoveroni.wordsremember.dictionary.presenter.AddTranslationPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.AddTranslationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.view.AddTranslationView;
import com.matteoveroni.wordsremember.dictionary.view.fragments.TranslationsListFragment;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class AddTranslationActivity extends AppCompatActivity implements AddTranslationView, LoaderManager.LoaderCallbacks<AddTranslationPresenter> {

    public static final String TAG = TagGenerator.tag(AddTranslationActivity.class);

    private TranslationsListFragment translationsListFragment;

    private AddTranslationPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_add_translation);
        ButterKnife.bind(this);

        translationsListFragment = (TranslationsListFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translations_list_fragment);

        setupAndShowToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            final String title = Str.concat(WordsRemember.ABBREVIATED_NAME, " - ", getString(R.string.title_dictionary_add_translation_activity));
            toolbar.setTitle(title);
        }
        setSupportActionBar(toolbar);
    }

    @Override
    public VocableTranslation getPojoUsedByView() {
//        return translationsListFragment.getPojoUsedByView();
        return null;
    }

    @Override
    public void setPojoUsedInView(VocableTranslation vocableTranslation) {
        translationsListFragment.setPojoUsedInView(vocableTranslation.getVocable());
    }

    @Override
    public void selectTranslationAction() {
    }

    @Override
    public void createTranslationAction() {
    }

    @Override
    public void goToEditTranslationView(Word vocable) {
        Intent intent_goToEditTranslationView = new Intent(getApplicationContext(), EditTranslationActivity.class);
        intent_goToEditTranslationView.putExtra(Extras.VOCABLE, vocable.toJson());
        startActivity(intent_goToEditTranslationView);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_edit_vocable, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_action_done:
//                saveTranslationAction();
//                return true;
//        }
//        return false;
//    }

    @Override
    public Loader<AddTranslationPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new AddTranslationPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<AddTranslationPresenter> loader, AddTranslationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<AddTranslationPresenter> loader) {
        presenter = null;
    }
}
