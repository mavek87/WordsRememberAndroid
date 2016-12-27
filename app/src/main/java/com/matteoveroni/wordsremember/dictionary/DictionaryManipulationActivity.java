package com.matteoveroni.wordsremember.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.matteoveroni.wordsremember.MyApp;
import com.matteoveroni.wordsremember.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.events.EventResetDictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.factories.DictionaryManipulationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManipulationView;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.utilities.Json;

import org.greenrobot.eventbus.EventBus;

public class DictionaryManipulationActivity extends AppCompatActivity
        implements DictionaryManipulationView, LoaderManager.LoaderCallbacks<DictionaryManipulationActivityPresenter> {

    private DictionaryManipulationActivityPresenter presenter;
    private static final int PRESENTER_ID = 1;

    private EventBus eventBus;

    private Toolbar toolbar;

    DictionaryManipulationFragment manipulationFragment;

    @Override
    public Loader<DictionaryManipulationActivityPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new DictionaryManipulationPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<DictionaryManipulationActivityPresenter> loader, DictionaryManipulationActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<DictionaryManipulationActivityPresenter> loader) {
        presenter = null;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void returnToPreviousView() {
        onBackPressed();
    }

    @Override
    public void populateViewForVocable(Word vocable) {
        manipulationFragment.populateViewForVocable(vocable);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            eventBus.postSticky(new EventResetDictionaryManagementView());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_manipulation);
        getSupportLoaderManager().initLoader(PRESENTER_ID, null, this);
        manipulationFragment = (DictionaryManipulationFragment) getSupportFragmentManager().findFragmentById(R.id.activity_dictionary_manipulation_fragment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(MyApp.NAME + " - Details");
        setSupportActionBar(toolbar);

        eventBus = EventBus.getDefault();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
        presenter.onVocableToManipulateRetrieved(retrieveVocableToManipulate());
    }

    @Override
    protected void onStop() {
        presenter.onViewDetached();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_management_editing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                presenter.onSaveVocableRequest(manipulationFragment.getVocable());
                return true;
        }
        return false;
    }

    private Word retrieveVocableToManipulate() {
        Intent starterIntent = getIntent();
        if (starterIntent.hasExtra(Extras.VOCABLE_TO_MANIPULATE)) {
            String str_vocableToManipulate = starterIntent.getStringExtra(Extras.VOCABLE_TO_MANIPULATE);
            if (!str_vocableToManipulate.trim().isEmpty())
                return Json.getInstance().fromJson(str_vocableToManipulate, Word.class);
        }
        return null;
    }
}
