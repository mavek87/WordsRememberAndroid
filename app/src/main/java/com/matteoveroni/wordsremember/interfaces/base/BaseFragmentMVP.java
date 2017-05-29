package com.matteoveroni.wordsremember.interfaces.base;

import android.widget.Toast;

import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.view.View;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;

/**
 * @author Matteo Veroni
 */

public abstract class BaseFragmentMVP extends BasePresenterFragment implements View {
    private LocaleTranslator translator;

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), localize(message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(FormattedString formattedLocaleMessage) {
        Toast.makeText(getActivity().getApplicationContext(), localize(formattedLocaleMessage), Toast.LENGTH_SHORT).show();
    }

    public String localize(String localeStringKey) {
        return getTranslator().localize(localeStringKey);
    }

    public String localize(FormattedString formattedLocaleString) {
        return getTranslator().localize(formattedLocaleString);
    }

    protected LocaleTranslator getTranslator() {
        if (translator == null)
            translator = WordsRemember.getLocaleTranslator(getActivity().getApplicationContext());
        return translator;
    }
}
