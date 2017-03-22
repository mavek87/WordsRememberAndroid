package com.matteoveroni.wordsremember.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.matteoveroni.wordsremember.dictionary.view.activities.ManageVocablesActivity;
import com.matteoveroni.wordsremember.interfaces.presenters.PresenterLoader;
import com.matteoveroni.wordsremember.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main Menu Activity
 */
public class MainMenuActivity extends AppCompatActivity implements MainMenuView, LoaderManager.LoaderCallbacks<MainMenuPresenter> {

    private MainMenuPresenter presenter;
    private static final int PRESENTER_LOADER_ID = 1;

    @BindView(R.id.main_menu_btn_start)
    Button btn_start;

    @BindView(R.id.main_menu_btn_manage_dictionary)
    Button btn_manage_dictionary;

    @BindView(R.id.main_menu_btn_settings)
    Button btn_settings;

    @OnClick(R.id.main_menu_btn_start)
    @SuppressWarnings("unused")
    public void onButtonStartClicked() {
    }

    @OnClick(R.id.main_menu_btn_manage_dictionary)
    @SuppressWarnings("unused")
    public void onButtonManageDictionaryClicked() {
        presenter.onButtonManageDictionaryClicked();
    }

    @OnClick(R.id.main_menu_btn_settings)
    @SuppressWarnings("unused")
    public void onButtonSettingsClicked() {
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

        setContentView(R.layout.activity_main_menu);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);

        // Todo: remove this lines in production code
        // This create a bug, if a vocable is inserted here EventAsyncSaveVocable is erroneously propagated
        // to VocableEditorFragment and causes "goBackActivity for the firstTime bug"

        // Use this line to reset the database if changes in some contract class schema has occurred
//        DatabaseManager.getInstance(getApplicationContext()).deleteDatabase();
//
//        Word vocable = new Word("vocable1");
//        Word translation = new Word("translation1");
//
//        DictionaryDAO dao = new DictionaryDAO(getApplicationContext());
//        dao.asyncSaveVocable(vocable);
//        dao.asyncSaveVocableTranslation(translation, vocable);
    }

    @Override
    public Loader<MainMenuPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new MainMenuPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<MainMenuPresenter> loader, MainMenuPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<MainMenuPresenter> loader) {
        presenter = null;
    }

    @Override
    public void startDictionaryManagement() {
        Intent intentStartDictionary = new Intent(getBaseContext(), ManageVocablesActivity.class);
        startActivity(intentStartDictionary);
    }
}

