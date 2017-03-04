package com.matteoveroni.wordsremember.presenters;

public interface PresenterFactory<T extends Presenter> {
    T create();
}