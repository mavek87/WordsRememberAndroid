package com.matteoveroni.wordsremember;

public interface PresenterFactory<T extends Presenter> {
    T create();
}