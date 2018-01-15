package com.matteoveroni.wordsremember.interfaces.presenter;

import com.matteoveroni.wordsremember.utils.BusAttacher;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public abstract class BasePresenter<V> implements Presenter<V> {

    protected V view;
    protected static final EventBus EVENT_BUS = EventBus.getDefault();

    @Override
    public void attachView(V view) {
        this.view = view;
        BusAttacher.register(this);
    }

    @Override
    public void detachView() {
        BusAttacher.register(this);
        this.view = null;
    }
}
