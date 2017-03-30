package com.matteoveroni.wordsremember.interfaces;

/**
 * Created by Matteo Veroni
 */

public interface PojoManipulable<T> {

    T getPojoUsed();

    void setPojoUsed(T pojo);
}
