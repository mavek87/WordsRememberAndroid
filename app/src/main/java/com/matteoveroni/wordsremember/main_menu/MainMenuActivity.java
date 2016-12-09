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
import com.matteoveroni.wordsremember.dictionary.management.DictionaryManagementActivity;
import com.matteoveroni.wordsremember.main_menu.factory.MainMenuActivityPresenterFactory;
import com.matteoveroni.wordsremember.main_menu.interfaces.MainMenuActivityPresenter;
import com.matteoveroni.wordsremember.main_menu.interfaces.MainMenuActivityView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that handles the Main Menu.
 *
 * @author Matteo Veroni
 * @version 0.0.5
 */
public class MainMenuActivity extends AppCompatActivity
        implements MainMenuActivityView, LoaderManager.LoaderCallbacks<MainMenuActivityPresenter> {

    private static final int PRESENTER_LOADER_ID = 1;
    private MainMenuActivityPresenter presenter;

    @BindView(R.id.main_menu_btn_start)
    Button btn_start;

    @BindView(R.id.main_menu_btn_manage_dictionary)
    Button btn_manage_dictionary;

    @BindView(R.id.main_menu_btn_settings)
    Button btn_settings;

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
    public Loader<MainMenuActivityPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new MainMenuActivityPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<MainMenuActivityPresenter> loader, MainMenuActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<MainMenuActivityPresenter> loader) {
        presenter = null;
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.main_menu_btn_manage_dictionary)
    public void onButtonManageDictionaryClicked() {
        presenter.onButtonManageDictionaryClicked();
    }

    @Override
    public void startDictionaryManagement() {
        Intent intentStartDictionary = new Intent(getBaseContext(), DictionaryManagementActivity.class);
        startActivity(intentStartDictionary);
    }
}

