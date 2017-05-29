package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.interfaces.base.BaseActivityMVP;
import com.matteoveroni.wordsremember.interfaces.presenters.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.ActivityView;
import com.matteoveroni.wordsremember.R;
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

public class EditTranslationActivity extends BaseActivityMVP implements EditTranslation {

    public static final String TAG = TagGenerator.tag(EditTranslationActivity.class);

    private TranslationEditorFragment translationEditorFragment;
    private EditTranslationPresenter presenter;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new EditTranslationPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (EditTranslationPresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_edit_translation);
        ButterKnife.bind(this);
        setupAndShowToolbar(getString(R.string.edit_translation));

        translationEditorFragment = (TranslationEditorFragment) getSupportFragmentManager().findFragmentById(R.id.dictionary_translation_editor_fragment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
}
