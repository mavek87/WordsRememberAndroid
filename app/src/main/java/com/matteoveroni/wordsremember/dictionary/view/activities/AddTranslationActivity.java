package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import butterknife.OnClick;

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

        final FragmentManager fragmentManager = getSupportFragmentManager();
        translationsListFragment = createTranslationListFragmentNotForVocable();
        fragmentManager.beginTransaction().replace(R.id.dictionary_translations_list_framelayout, translationsListFragment).commit();
        fragmentManager.executePendingTransactions();

        setupAndShowToolbar();
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private TranslationsListFragment createTranslationListFragmentNotForVocable() {
        final TranslationsListFragment fragmentToBuild = new TranslationsListFragment();
        fragmentToBuild.type = TranslationsListFragment.Type.TRANSLATIONS_NOT_FOR_VOCABLE;
        return fragmentToBuild;
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
    public void setPojoUsedByView(VocableTranslation vocableTranslation) {
        translationsListFragment.setPojoUsedByView(vocableTranslation.getVocable());
    }

    @OnClick(R.id.add_translation_floating_action_button)
    @Override
    public void createTranslationAction() {
        presenter.onCreateTranslationRequest();
    }

    @Override
    public void goToEditTranslationView() {
        Intent intent_goToEditTranslationView = new Intent(getApplicationContext(), EditTranslationActivity.class);
        startActivity(intent_goToEditTranslationView);
    }

    @Override
    public void returnToPreviousView() {
        onBackPressed();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

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
