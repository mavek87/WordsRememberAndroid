package com.matteoveroni.wordsremember.dictionary.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.AddTranslationPresenter;
import com.matteoveroni.wordsremember.dictionary.presenter.factories.AddTranslationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.view.AddTranslationView;
import com.matteoveroni.wordsremember.dictionary.view.fragments.TranslationsListFragment;
import com.matteoveroni.wordsremember.dictionary.pojos.Word;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class AddTranslationActivity extends BaseActivityPresentedView implements AddTranslationView {

    public static final String TAG = TagGenerator.tag(AddTranslationActivity.class);

    private TranslationsListFragment translationsListFragment;
    private AddTranslationPresenter presenter;

    @Override
    protected PresenterFactory getPresenterFactory() {
        return new AddTranslationPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(Presenter presenter) {
        this.presenter = (AddTranslationPresenter) presenter;
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

        setupAndShowToolbar(getString(R.string.add_translation));
    }

    private TranslationsListFragment createTranslationListFragmentNotForVocable() {
        final TranslationsListFragment fragmentToBuild = new TranslationsListFragment();
        fragmentToBuild.type = TranslationsListFragment.Type.TRANSLATIONS_NOT_FOR_VOCABLE;
        return fragmentToBuild;
    }

    @Override
    public Word getPojoUsed() {
        throw new UnsupportedOperationException(AddTranslationActivity.class.getSimpleName() + " doesn't store any pojo");
    }

    @Override
    public void setPojoUsed(Word vocable) {
        translationsListFragment.setPojoUsed(vocable);
    }

    @OnClick(R.id.add_translation_floating_action_button)
    @Override
    public void createTranslationAction() {
        presenter.onCreateTranslationRequest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        returnToPreviousView();
    }

    @Override
    public void returnToPreviousView() {
        finish();
    }
}
