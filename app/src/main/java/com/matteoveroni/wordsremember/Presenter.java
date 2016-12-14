package com.matteoveroni.wordsremember;

import android.app.Activity;

public interface Presenter<V> {
    void onViewAttached(V view);

    void onViewDetached();

    void onViewDestroyed();
}