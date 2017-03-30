package com.matteoveroni.wordsremember.interfaces.view;

/**
 * @author Matteo Veroni
 */

public interface View {

    void showMessage(String localizableMessage);

    void showMessage(String messageFormattedToLocalize, Object... args);
}
