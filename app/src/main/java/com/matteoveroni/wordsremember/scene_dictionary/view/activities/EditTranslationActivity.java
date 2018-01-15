package com.matteoveroni.wordsremember.scene_dictionary.view.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.factories.PresenterFactories;
import com.matteoveroni.wordsremember.factories.PresenterFactoryName;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.AbstractPresentedActivityView;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.EditTranslationPresenter;
import com.matteoveroni.wordsremember.scene_dictionary.view.EditTranslationView;
import com.matteoveroni.wordsremember.scene_dictionary.view.fragments.TranslationEditorFragment;

import butterknife.ButterKnife;

/**
 * @author Matteo Veroni
 */

public class EditTranslationActivity extends AbstractPresentedActivityView implements EditTranslationView {

    public static final String TAG = TagGenerator.tag(EditTranslationActivity.class);

    private TranslationEditorFragment translationEditorFragment;
    private EditTranslationPresenter presenter;

    @Override
    public VocableTranslation getPojoUsed() {
        return translationEditorFragment.getPojoUsed();
    }

    @Override
    public void setPojoUsed(VocableTranslation vocableTranslation) {
        this.translationEditorFragment.setPojoUsed(vocableTranslation);
    }

    @Override
    protected PresenterFactory getPresenterFactory() {
        return PresenterFactories.getFactory(PresenterFactoryName.EDIT_TRANSLATION_PRESENTER_FACTORY);
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
}
