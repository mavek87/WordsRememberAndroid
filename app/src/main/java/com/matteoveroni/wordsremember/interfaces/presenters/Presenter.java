package com.matteoveroni.wordsremember.interfaces.presenters;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}