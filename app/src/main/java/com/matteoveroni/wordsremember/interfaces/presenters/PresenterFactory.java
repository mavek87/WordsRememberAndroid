package com.matteoveroni.wordsremember.interfaces.presenters;

public interface PresenterFactory<T extends Presenter> {
    T create();
}