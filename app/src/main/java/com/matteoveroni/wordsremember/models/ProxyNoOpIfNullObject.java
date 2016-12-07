package com.matteoveroni.wordsremember.models;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyNoOpIfNullObject implements InvocationHandler {

    private WeakReference<Object> object;

    public ProxyNoOpIfNullObject(Object object) {
        this.object = new WeakReference<>(object);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if (object == null || object.get() == null) {
            return null;
        }

        return method.invoke(object.get(), args);
    }
}