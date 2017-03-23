package com.matteoveroni.wordsremember.interfaces.view;

/**
 * Created by Matteo Veroni
 */

public interface PojoManipulableView<T> {

    T getPojoUsedByView();

    void setPojoUsedByView(T pojo);
}
