package com.matteoveroni.wordsremember.dictionary.view;

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
import com.matteoveroni.wordsremember.fragments.TranslationsListFragment;
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
    public void selectTranslationAction() {
    }

    @Override
    public void createTranslactionAction() {
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
    public VocableTranslation getPojoUsedByView() {
        return null;
//        return translationEditorFragment.getPojoUsedByView();
    }

    @Override
    public void setPojoUsedInView(VocableTranslation vocableTranslation) {
        translationsListFragment.setPojoUsedInView(vocableTranslation.getVocable());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
        presenter.onVocableToTranslateRetrieved(findVocableToTranslate());
    }

    private Word findVocableToTranslate() {
        Intent starterIntent = getIntent();
        String json_vocableToTranslate = starterIntent.getStringExtra(Extras.VOCABLE);
        return Word.fromJson(json_vocableToTranslate);
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
        setupAndShowToolbar();

        translationsListFragment = (TranslationsListFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translations_list_fragment);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_vocable_editor, menu);
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
