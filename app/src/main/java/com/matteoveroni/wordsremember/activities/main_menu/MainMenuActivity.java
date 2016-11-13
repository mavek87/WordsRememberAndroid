package com.matteoveroni.wordsremember.activities.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.DictionaryManagementActivity;

public class MainMenuActivity extends AppCompatActivity {

    private Button btn_start;
    private Button btn_manage_dictionary;
    private Button btn_settings;
    private Button btn_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_manage_dictionary = (Button) findViewById(R.id.btn_manage_dictionary);
        btn_manage_dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStartDictionary = new Intent(getBaseContext(), DictionaryManagementActivity.class);
                startActivity(intentStartDictionary);
            }
        });
    }
}
