package com.matteoveroni.wordsremember.utils;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

/**
 * @author Matteo Veroni
 */

public class BusAttacher {

    private static final String TAG = TagGenerator.tag(BusAttacher.class);
    private final EventBus eventBus;

    public BusAttacher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void registerToEventBus(Object object) {
        if (!eventBus.isRegistered(object)) {
            try {
                eventBus.register(object);
            } catch (EventBusException ex) {
                String warnMessage = "Object (class " + object.getClass() + ") doesn\'t need to be attached to event bus and so it won\'t";
                Log.w(TAG, warnMessage);
            }
        }
    }

    public void unregisterToEventBus(Object object) {
        if (eventBus.isRegistered(object)) {
            eventBus.unregister(object);
        }
    }
}
