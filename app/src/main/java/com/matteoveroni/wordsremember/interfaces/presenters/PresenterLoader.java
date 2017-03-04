package com.matteoveroni.wordsremember.interfaces.presenters;

import android.content.Context;
import android.support.v4.content.Loader;

public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private PresenterFactory<T> presenterFactory;
    private T presenter;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     * @param presenterFactory
     */
    public PresenterLoader(Context context, PresenterFactory<T> presenterFactory) {
        super(context);
        this.presenterFactory = presenterFactory;
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = presenterFactory.create();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.destroy();
        presenter = null;
    }
}