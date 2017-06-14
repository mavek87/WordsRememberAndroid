package com.matteoveroni.wordsremember.interfaces.presenter;

public interface PresenterFactory<T extends Presenter> {
    T create();
}