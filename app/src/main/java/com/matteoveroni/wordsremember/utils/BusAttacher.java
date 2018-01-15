package com.matteoveroni.wordsremember.utils;

import android.util.Log;

import com.matteoveroni.androidtaggenerator.TagGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

/**
 * @author Matteo Veroni
 */

public class BusAttacher {

    public static final EventBus EVENT_BUS = EventBus.getDefault();

    private static final String TAG = TagGenerator.tag(BusAttacher.class);

    public static void register(Object subscriber) {
        if (!EVENT_BUS.isRegistered(subscriber)) {
            try {
                EVENT_BUS.register(subscriber);
            } catch (EventBusException ex) {
                String warnMessage = "Object (class " + subscriber.getClass() + ") doesn\'t need to be attached to event bus and so it won\'t";
                Log.w(TAG, warnMessage);
            }
        }
    }

    public static void unregister(Object subscriber) {
        if (EVENT_BUS.isRegistered(subscriber)) {
            EVENT_BUS.unregister(subscriber);
        }
    }
}
