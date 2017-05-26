package com.matteoveroni.wordsremember.interfaces.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;

/**
 * @author Matteo Veroni
 */

public abstract class ActivityView extends AppCompatActivity implements View {

    private LocaleTranslator translator;

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), localize(message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(FormattedString formattedLocaleMessage) {
        Toast.makeText(getApplicationContext(), localize(formattedLocaleMessage), Toast.LENGTH_SHORT).show();
    }

    public String localize(String localeStringKey) {
        return getTranslator().localize(localeStringKey);
    }

    public String localize(FormattedString formattedLocaleString) {
        return getTranslator().localize(formattedLocaleString);
    }

    protected LocaleTranslator getTranslator() {
        if (translator == null)
            translator = WordsRemember.getLocaleTranslator(getApplicationContext());
        return translator;
    }

    protected void setupAndShowToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(Str.concat(WordsRemember.ABBREVIATED_NAME, " ", WordsRemember.VERSION, " - ", title));
        }
        setSupportActionBar(toolbar);
    }

}
