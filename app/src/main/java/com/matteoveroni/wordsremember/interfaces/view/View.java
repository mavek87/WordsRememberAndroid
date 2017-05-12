package com.matteoveroni.wordsremember.interfaces.view;

import com.matteoveroni.wordsremember.localization.FormattedLocaleString;

/**
 * @author Matteo Veroni
 */

public interface View {

    void showMessage(String message);

    void showLocalizedMessage(String localizableMessage);

    void showLocalizedMessage(FormattedLocaleString formattedLocaleString);
}
