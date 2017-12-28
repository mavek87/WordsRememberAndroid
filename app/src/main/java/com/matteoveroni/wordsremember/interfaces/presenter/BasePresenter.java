package com.matteoveroni.wordsremember.interfaces.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.utils.BusAttacher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

/**
 * @author Matteo Veroni
 */

public abstract class BasePresenter<V> implements Presenter<V> {

    private static final String TAG = TagGenerator.tag(BasePresenter.class);

    protected V view;
    protected static final EventBus EVENT_BUS = EventBus.getDefault();

    private final BusAttacher busAttacher = new BusAttacher(EVENT_BUS);

    @Override
    public void attachView(V view) {
        this.view = view;
        busAttacher.registerToEventBus(this);
    }

    @Override
    public void detachView() {
        busAttacher.unregisterToEventBus(this);
        this.view = null;
    }
}
