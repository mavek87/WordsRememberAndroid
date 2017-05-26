package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.interfaces.view.ActivityView;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.EditVocablePresenterFactory;
import com.matteoveroni.wordsremember.dictionary.view.EditVocable;
import com.matteoveroni.wordsremember.dictionary.view.fragments.TranslationsListFragment;
import com.matteoveroni.wordsremember.dictionary.view.fragments.VocableEditorFragment;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.EditVocablePresenter;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class EditVocableActivity extends ActivityView implements EditVocable, LoaderManager.LoaderCallbacks<EditVocablePresenter> {

    public static final String TAG = TagGenerator.tag(EditVocableActivity.class);

    private FragmentManager fragmentManager;
    private VocableEditorFragment vocableEditorFragment;
    private TranslationsListFragment translationsListFragment;

    private EditVocablePresenter presenter;
    private static final int PRESENTER_LOADER_ID = 1;

    private AlertDialog dialogCannotAddTranslation;

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

        setupAndShowToolbar(getString(R.string.vocable_editor));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    private TranslationsListFragment createTranslationListFragmentForVocable() {
        final TranslationsListFragment fragmentToBuild = new TranslationsListFragment();
        fragmentToBuild.type = TranslationsListFragment.Type.TRANSLATIONS_FOR_VOCABLE;
        return fragmentToBuild;
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
    public Word getPojoUsed() {
        return vocableEditorFragment.getPojoUsed();
    }

    @Override
    public void setPojoUsed(Word vocable) {
        vocableEditorFragment.setPojoUsed(vocable);
        translationsListFragment.setPojoUsed(vocable);
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
    public void showDialogCannotAddTranslationIfVocableNotSaved() {
        if (dialogCannotAddTranslation == null) {
            buildDialogCannotAddTranslationIfVocableNotSaved();
        }
        dialogCannotAddTranslation.show();
    }

    private void buildDialogCannotAddTranslationIfVocableNotSaved() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(R.string.vocable_not_saved)
                .setMessage(R.string.msg_vocable_not_saved)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.onSaveVocableRequest();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        dialogCannotAddTranslation = alertDialogBuilder.create();
    }

    @Override
    public void goToAddTranslationView() {
        Intent intent_goToAddTranslationActivity = new Intent(getApplicationContext(), AddTranslationActivity.class);
        startActivityForResult(intent_goToAddTranslationActivity, 0);
    }

    @Override
    public void returnToPreviousView() {
        onBackPressed();
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
