package com.matteoveroni.wordsremember.interfaces.presenter;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public abstract class BasePresenter<V> implements Presenter<V> {

    private static final String TAG = TagGenerator.tag(BasePresenter.class);

    protected V view;
    protected static final EventBus EVENT_BUS = EventBus.getDefault();

    @Override
    public void attachView(V view) {
        this.view = view;
        registerToEventBus();
    }

    @Override
    public void detachView() {
        unregisterToEventBus();
        this.view = null;
    }

    private void registerToEventBus() {
        if (!EVENT_BUS.isRegistered(this)) {
            try {
                EVENT_BUS.register(this);
            } catch (Exception ex) {
                String warnMessage = ex.getMessage() + ". Class not attached with the event bus.";
                Log.w(TAG, warnMessage);
            }
        }
    }

    private void unregisterToEventBus() {
        if (EVENT_BUS.isRegistered(this)) {
            EVENT_BUS.unregister(this);
        }
    }
}
