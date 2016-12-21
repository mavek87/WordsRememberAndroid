package com.matteoveroni.wordsremember.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.matteoveroni.wordsremember.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.factories.DictionaryManipulationPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.utilities.Json;

public class DictionaryManipulationActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<DictionaryManipulationPresenter> {

    private DictionaryManipulationPresenter presenter;
    private static final int PRESENTER_ID = 1;

    @Override
    public Loader<DictionaryManipulationPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new DictionaryManipulationPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<DictionaryManipulationPresenter> loader, DictionaryManipulationPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<DictionaryManipulationPresenter> loader) {
        presenter = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manipulation);

        getSupportLoaderManager().initLoader(PRESENTER_ID, null, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Word vocableToManipulate = detectVocableToManipulate();
        presenter.setVocableToManipulate(vocableToManipulate);
    }

    private Word detectVocableToManipulate() {
        Intent starterIntent = getIntent();
        if (starterIntent.hasExtra(Extras.VOCABLE_TO_MANIPULATE)) {
            String str_vocableToManipulate = starterIntent.getStringExtra(Extras.VOCABLE_TO_MANIPULATE);
            if (!str_vocableToManipulate.trim().isEmpty())
                return Json.getInstance().fromJson(str_vocableToManipulate, Word.class);
        }
        return null;
    }
}
