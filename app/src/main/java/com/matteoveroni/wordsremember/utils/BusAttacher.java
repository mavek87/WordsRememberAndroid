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

    public static void register(Object object) {
        if (!EVENT_BUS.isRegistered(object)) {
            try {
                EVENT_BUS.register(object);
            } catch (EventBusException ex) {
                String warnMessage = "Object (class " + object.getClass() + ") doesn\'t need to be attached to event bus and so it won\'t";
                Log.w(TAG, warnMessage);
            }
        }
    }

    public static void unregister(Object object) {
        if (EVENT_BUS.isRegistered(object)) {
            EVENT_BUS.unregister(object);
        }
    }
}
