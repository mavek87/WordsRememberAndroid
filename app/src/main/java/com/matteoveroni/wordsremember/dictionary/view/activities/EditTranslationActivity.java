package com.matteoveroni.wordsremember.dictionary.view.activities;

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
import com.matteoveroni.wordsremember.dictionary.Extras;
import com.matteoveroni.wordsremember.dictionary.presenter.EditTranslationPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.EditTranslationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.view.EditTranslationView;
import com.matteoveroni.wordsremember.dictionary.view.fragments.TranslationEditorFragment;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;

import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class EditTranslationActivity extends AppCompatActivity implements EditTranslationView, LoaderManager.LoaderCallbacks<EditTranslationPresenter> {

    public static final String TAG = TagGenerator.tag(EditTranslationActivity.class);

    private TranslationEditorFragment translationEditorFragment;
    private EditTranslationPresenter presenter;

    @Override
    public void saveTranslationAction() {
        presenter.onSaveTranslationRequest();
    }

    @Override
    public void returnToEditVocableView() {
        Intent intent_goToEditVocableActivity = new Intent(getApplicationContext(), EditVocableActivity.class);
        startActivity(intent_goToEditVocableActivity);
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
    public void setPojoUsedByView(VocableTranslation vocableTranslation) {
        this.translationEditorFragment.setPojoUsedByView(vocableTranslation);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_edit_translation);
        ButterKnife.bind(this);
        setupAndShowToolbar();

        translationEditorFragment = (TranslationEditorFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translation_editor_fragment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        final int PRESENTER_LOADER_ID = 1;
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
        getMenuInflater().inflate(R.menu.menu_dictionary_top_bar, menu);
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
    public Loader<EditTranslationPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new EditTranslationPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<EditTranslationPresenter> loader, EditTranslationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<EditTranslationPresenter> loader) {
        presenter = null;
    }
}
