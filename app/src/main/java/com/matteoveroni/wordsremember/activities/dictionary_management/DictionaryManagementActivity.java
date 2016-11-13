package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matteoveroni.wordsremember.R;

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
}
