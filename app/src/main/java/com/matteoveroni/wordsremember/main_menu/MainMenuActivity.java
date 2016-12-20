package com.matteoveroni.wordsremember.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.matteoveroni.wordsremember.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.DictionaryManagementActivity;
import com.matteoveroni.wordsremember.main_menu.factory.MainMenuPresenterFactory;
import com.matteoveroni.wordsremember.main_menu.interfaces.MainMenuPresenter;
import com.matteoveroni.wordsremember.main_menu.interfaces.MainMenuView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main Menu Activity
 */
public class MainMenuActivity extends AppCompatActivity
        implements MainMenuView, LoaderManager.LoaderCallbacks<MainMenuPresenter> {

    private static final int PRESENTER_LOADER_ID = 1;

    private MainMenuPresenter presenter;

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
        presenter.onViewAttached(this);
    }

    @Override
    protected void onStop() {
        presenter.onViewDetached();
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
        Intent intentStartDictionary = new Intent(getBaseContext(), DictionaryManagementActivity.class);
        startActivity(intentStartDictionary);
    }
}

