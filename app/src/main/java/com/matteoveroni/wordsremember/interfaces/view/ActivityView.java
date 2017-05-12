package com.matteoveroni.wordsremember.interfaces.view;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;
import com.matteoveroni.wordsremember.localization.FormattedLocaleString;

/**
 * @author Matteo Veroni
 */

public abstract class ActivityView extends AppCompatActivity implements View {

    protected LocaleTranslator translator;

    @Override
    public void showMessage(String message) {
        showToastWithMessage(message);
    }

    @Override
    public void showLocalizedMessage(String localizableMessage) {
        showToastWithMessage(localize(localizableMessage));
    }

    private void showToastWithMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public String localize(String string) {
        if (translator == null)
            translator = WordsRemember.getLocaleTranslator(getApplicationContext());

        return translator.localize(string);
    }

    public String localize(FormattedLocaleString formattedLocaleString) {
        if (translator == null)
            translator = WordsRemember.getLocaleTranslator(getApplicationContext());

        return translator.localize(formattedLocaleString);
    }


}
