package com.matteoveroni.wordsremember.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.ui.activities.DictionaryManagementActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that handles the Main Menu.
 *
 * @author Matteo Veroni
 * @version 0.0.5
 */
public class MainMenuActivity extends AppCompatActivity {
    @BindView(R.id.main_menu_btn_start)
    Button btn_start;

    @BindView(R.id.main_menu_btn_manage_dictionary)
    Button btn_manage_dictionary;

    @BindView(R.id.main_menu_btn_settings)
    Button btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.main_menu_btn_manage_dictionary)
    public void onButtonManageDictionaryClicked(){
        Intent intentStartDictionary = new Intent(getBaseContext(), DictionaryManagementActivity.class);
        startActivity(intentStartDictionary);
    }
}
