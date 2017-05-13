package com.matteoveroni.wordsremember.interfaces.view;

import com.matteoveroni.wordsremember.FormattedString;

/**
 * @author Matteo Veroni
 */

public interface View {

    void showMessage(String message);

    void showMessage(FormattedString formattedString);
}
