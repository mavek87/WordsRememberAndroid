package com.matteoveroni.wordsremember.interfaces.view;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.matteoveroni.wordsremember.interfaces.view.View;

import static com.matteoveroni.wordsremember.WordsRemember.LOCALE_TRANSLATOR;

/**
 * @author Matteo Veroni
 */

public abstract class ActivityView extends AppCompatActivity implements View {

    @Override
    public void showMessage(String localizableMessage) {
        showToastWithMessage(localizeMessage(localizableMessage));
    }

    private void showToastWithMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String localizeMessage(String localizableMessage) {
        return LOCALE_TRANSLATOR.localize(localizableMessage);
    }

    @Override
    public void showMessage(String messageFormattedToLocalize, Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                args[i] = localizeMessage((String) args[i]);
            }
        }

        final String formattedLocalizedMessage = String.format(messageFormattedToLocalize, args);
        showToastWithMessage(formattedLocalizedMessage);
    }
}
