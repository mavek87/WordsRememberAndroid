package com.matteoveroni.wordsremember.scene_dictionary.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.EditVocablePresenter;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.EditVocablePresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.view.EditVocableView;
import com.matteoveroni.wordsremember.scene_dictionary.view.fragments.TranslationsListFragment;
import com.matteoveroni.wordsremember.scene_dictionary.view.fragments.VocableEditorFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class EditVocableActivity extends BaseActivityPresentedView implements EditVocableView {

    public static final String TAG = TagGenerator.tag(EditVocableActivity.class);

    private VocableEditorFragment vocableEditorFragment;
    private TranslationsListFragment vocableTranslationsFragment;
    private EditVocablePresenter presenter;
    private AlertDialog errorDialog;

    @Override
    public Word getPojoUsed() {
        return vocableEditorFragment.getPojoUsed();
    }

    @Override
    public void setPojoUsed(Word vocable) {
        vocableEditorFragment.setPojoUsed(vocable);
        vocableTranslationsFragment.setPojoUsed(vocable);
    }

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new EditVocablePresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (EditVocablePresenter) presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_edit_vocable);
        ButterKnife.bind(this);
        buildAndShowInnerFragments();
        setupAndShowToolbar(getString(R.string.vocable_editor));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void buildAndShowInnerFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        vocableEditorFragment = (VocableEditorFragment) fragmentManager.findFragmentById(R.id.dictionary_vocable_editor_fragment);
        vocableTranslationsFragment = buildVocableTranslationsFragment();
        fragmentManager.beginTransaction().replace(R.id.dictionary_translations_list_framelayout, vocableTranslationsFragment).commit();
        fragmentManager.executePendingTransactions();
    }

    private TranslationsListFragment buildVocableTranslationsFragment() {
        TranslationsListFragment fragment = new TranslationsListFragment();
        final Bundle fragmentArgs = new Bundle();
        final String json_fragmentType = Json.getInstance().toJson(TranslationsListFragment.TranslationsType.TRANSLATIONS_FOR_VOCABLE);
        fragmentArgs.putString(TranslationsListFragment.FRAGMENT_TYPE_KEY, json_fragmentType);
        fragment.setArguments(fragmentArgs);
        return fragment;
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
    public void refresh() {
        vocableTranslationsFragment.onResume();
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
    public void showErrorDialogVocableNotSaved() {
        if (errorDialog == null) {
            errorDialog = buildErrorDialog(getString(R.string.vocable_not_saved), getString(R.string.msg_vocable_not_saved), presenter);
        }
        errorDialog.show();
    }

    @Override
    public void dismissErrorDialog() {
        if (errorDialog == null) errorDialog.dismiss();
    }

    @Override
    public void returnToPreviousView() {
        onBackPressed();
    }
}
