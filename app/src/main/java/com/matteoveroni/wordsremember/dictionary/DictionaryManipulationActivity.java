package com.matteoveroni.wordsremember.dictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManipulationPresenter;
import com.matteoveroni.wordsremember.pojo.Word;

public class DictionaryManipulationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manipulation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
