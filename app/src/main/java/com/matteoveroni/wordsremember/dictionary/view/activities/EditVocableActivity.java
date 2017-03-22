package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.EditVocablePresenterFactory;
import com.matteoveroni.wordsremember.dictionary.view.EditVocableView;
import com.matteoveroni.wordsremember.dictionary.view.fragments.TranslationsListFragment;
import com.matteoveroni.wordsremember.dictionary.view.fragments.VocableEditorFragment;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.EditVocablePresenter;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class EditVocableActivity extends AppCompatActivity implements EditVocableView, LoaderManager.LoaderCallbacks<EditVocablePresenter> {

    public static final String TAG = TagGenerator.tag(EditVocableActivity.class);

    private FragmentManager fragmentManager;
    private VocableEditorFragment vocableEditorFragment;
    private TranslationsListFragment translationsListFragment;

    private EditVocablePresenter presenter;
    private final int ID_PRESENTER_LOADER = 1;

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
        setContentView(R.layout.activity_dictionary_edit_vocable);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();
        vocableEditorFragment = (VocableEditorFragment) fragmentManager.findFragmentById(R.id.dictionary_vocable_editor_fragment);
        translationsListFragment = createTranslationListFragmentForVocable();
        fragmentManager.beginTransaction().replace(R.id.dictionary_translations_list_framelayout, translationsListFragment).commit();
        fragmentManager.executePendingTransactions();

        setupAndShowToolbar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportLoaderManager().initLoader(ID_PRESENTER_LOADER, null, this);
    }

    private TranslationsListFragment createTranslationListFragmentForVocable() {
        final TranslationsListFragment fragmentToBuild = new TranslationsListFragment();
        final Bundle typeOfFragment = new Bundle();
        typeOfFragment.putString(TranslationsListFragment.KEY_TRANSLATIONS_FOR_VOCABLE, null);
        fragmentToBuild.setArguments(typeOfFragment);
        return fragmentToBuild;
    }

    private void setupAndShowToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            final String title = Str.concat(WordsRemember.ABBREVIATED_NAME, " - ", getString(R.string.title_activity_dictionary_vocable_editor));
            toolbar.setTitle(title);
        }
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_top_bar, menu);
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
    public Word getPojoUsedByView() {
        return vocableEditorFragment.getPojoUsedByView();
    }

    @Override
    public void setPojoUsedInView(Word vocable) {
        vocableEditorFragment.setPojoUsedInView(vocable);
        translationsListFragment.setPojoUsedInView(vocable);
    }

    @Override
    public void saveVocableAction() {
        presenter.onSaveVocableRequest();
    }

    @OnClick(R.id.edit_vocable_view_add_translation_action_button)
    @Override
    public void addTranslationAction() {
        presenter.onAddTranslationRequest();
    }

    @Override
    public void goToAddTranslationView() {
        Intent intent_goToAddTranslationActivity = new Intent(getApplicationContext(), AddTranslationActivity.class);
        startActivity(intent_goToAddTranslationActivity);
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
    public Loader<EditVocablePresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new EditVocablePresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<EditVocablePresenter> loader, EditVocablePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<EditVocablePresenter> loader) {
        presenter = null;
    }
}
