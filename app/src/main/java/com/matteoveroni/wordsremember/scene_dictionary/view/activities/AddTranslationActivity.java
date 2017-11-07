package com.matteoveroni.wordsremember.scene_dictionary.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.Json;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.AddTranslationPresenter;
import com.matteoveroni.wordsremember.scene_dictionary.presenter.factories.AddTranslationPresenterFactory;
import com.matteoveroni.wordsremember.scene_dictionary.view.AddTranslationView;
import com.matteoveroni.wordsremember.scene_dictionary.view.fragments.TranslationsListFragment;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;
import com.matteoveroni.wordsremember.interfaces.view.BaseActivityPresentedView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Matteo Veroni
 */

public class AddTranslationActivity extends BaseActivityPresentedView implements AddTranslationView {

    public static final String TAG = TagGenerator.tag(AddTranslationActivity.class);

    private TranslationsListFragment notVocableTranslationsFragment;
    private AddTranslationPresenter presenter;

    @Override
    public Word getPojoUsed() {
        return notVocableTranslationsFragment.getPojoUsed();
    }

    @Override
    public void setPojoUsed(Word vocable) {
        notVocableTranslationsFragment.setPojoUsed(vocable);
    }

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
        buildAndShowInnerFragment();
        setupAndShowToolbar(getString(R.string.add_translation));
    }

    private void buildAndShowInnerFragment() {
        notVocableTranslationsFragment = new TranslationsListFragment();
        final Bundle fragmentArgs = new Bundle();
        final String json_fragmentType = Json.getInstance().toJson(TranslationsListFragment.TranslationsType.TRANSLATIONS_NOT_FOR_VOCABLE);
        fragmentArgs.putString(TranslationsListFragment.FRAGMENT_TYPE_KEY, json_fragmentType);
        notVocableTranslationsFragment.setArguments(fragmentArgs);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dictionary_translations_list_framelayout, notVocableTranslationsFragment).commit();
        fragmentManager.executePendingTransactions();
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
