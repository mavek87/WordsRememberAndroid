package com.matteoveroni.wordsremember.presenters;

public interface Presenter<V> {

    void attachView(V view);

    void destroy();
}