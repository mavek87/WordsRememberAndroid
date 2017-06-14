package com.matteoveroni.wordsremember.interfaces.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();
}