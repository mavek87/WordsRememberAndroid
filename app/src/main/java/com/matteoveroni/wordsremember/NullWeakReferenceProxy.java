package com.matteoveroni.wordsremember;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class NullWeakReferenceProxy implements InvocationHandler {

    private WeakReference<Object> object;

    public NullWeakReferenceProxy(Object object) {
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