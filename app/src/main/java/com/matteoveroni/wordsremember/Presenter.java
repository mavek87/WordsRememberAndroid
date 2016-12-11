package com.matteoveroni.wordsremember;

public interface Presenter<V> {
    void onViewAttached(V view);

    void onViewDetached();

    void onViewDestroyed();
}