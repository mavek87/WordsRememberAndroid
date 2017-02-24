package com.matteoveroni.wordsremember;

public interface Presenter<V> {

    void attachView(V view);

    void destroy();
}