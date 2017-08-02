package com.matteoveroni.wordsremember.login;

import com.matteoveroni.wordsremember.interfaces.view.View;

/**
 * @author Matteo Veroni
 */

public interface LoginView extends View {

    void showSuccessfulMessage(String message);

    void showErrorMessage(String errorMessage);
}
