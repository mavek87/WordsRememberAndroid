package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.interfaces.view.ActivityView;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.presenter.EditTranslationPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.EditTranslationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.view.EditTranslation;
import com.matteoveroni.wordsremember.dictionary.view.fragments.TranslationEditorFragment;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.dictionary.pojos.VocableTranslation;

import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class EditTranslationActivity extends ActivityView implements EditTranslation, LoaderManager.LoaderCallbacks<EditTranslationPresenter> {

    public static final String TAG = TagGenerator.tag(EditTranslationActivity.class);

    private TranslationEditorFragment translationEditorFragment;
    private EditTranslationPresenter presenter;

    private static final int PRESENTER_LOADER_ID = 1;

    @Override
    public void saveTranslationAction() {
        presenter.onSaveTranslationRequest();
    }

    @Override
    public void returnToPreviousView() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public VocableTranslation getPojoUsed() {
        return translationEditorFragment.getPojoUsed();
    }

    @Override
    public void setPojoUsed(VocableTranslation vocableTranslation) {
        this.translationEditorFragment.setPojoUsed(vocableTranslation);
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
        setupAndShowToolbar(getString(R.string.edit_translation));

        translationEditorFragment = (TranslationEditorFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translation_editor_fragment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
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
