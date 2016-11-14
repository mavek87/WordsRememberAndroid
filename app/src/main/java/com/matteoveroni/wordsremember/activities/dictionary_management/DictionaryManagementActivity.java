package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.model.Word;

import java.util.List;

public class DictionaryManagementActivity extends AppCompatActivity {

    private DictionaryManagementFragment dictionaryManagementFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_management);
        dictionaryManagementFragment = new DictionaryManagementFragment();
        loadDictionaryManagementFragment();
    }

    private void loadDictionaryManagementFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.dictionaryManagementContainer, dictionaryManagementFragment, DictionaryManagementFragment.TAG)
                .commit();
    }

    @Override
    public Loader<List<Word>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Word>> loader, List<Word> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Word>> loader) {

    }
}
