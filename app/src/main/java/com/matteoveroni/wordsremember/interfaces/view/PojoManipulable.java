package com.matteoveroni.wordsremember.interfaces.view;

/**
 * Created by Matteo Veroni
 */

public interface PojoManipulable<T> {

    T getPojoUsedByView();

    void setPojoUsedInView(T pojo);
}
