package com.matteoveroni.wordsremember.interfaces.view;

/**
 * @author Matteo Veroni
 */

public interface View {

    void showMessage(String message);

    void showLocalizedMessage(String localizableMessage);

    void showLocalizedMessage(String messageFormattedToLocalize, Object... args);
}
