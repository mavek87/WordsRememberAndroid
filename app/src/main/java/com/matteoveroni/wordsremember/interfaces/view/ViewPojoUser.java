package com.matteoveroni.wordsremember.interfaces.view;

/**
 * Created by Matteo Veroni
 */

public interface ViewPojoUser<T> {

    T getPojoUsedByView();

    void setPojoUsedInView(T pojo);
}
