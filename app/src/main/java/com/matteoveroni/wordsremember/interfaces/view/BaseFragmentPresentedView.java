package com.matteoveroni.wordsremember.interfaces.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterLoader;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;

/**
 * Useful resources: https://github.com/czyrux/MvpLoaderSample/blob/master/app/src/main/java/de/czyrux/mvploadersample/base/BasePresenterFragment.java
 */

public abstract class BaseFragmentPresentedView<P extends Presenter<V>, V> extends Fragment implements View {

    public static final String TAG = TagGenerator.tag(BaseFragmentPresentedView.class);

    private static final int PRESENTER_LOADER_ID = 1;
    private P presenter;
    private LocaleTranslator translator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        Loader<P> loader = getLoaderManager().getLoader(loaderId());
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((PresenterLoader<P>) loader).getPresenter();
            onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader() {
        // LoaderCallbacks as an object, so no hint regarding loader will be leak to the subclasses.
        getLoaderManager().initLoader(loaderId(), null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                Log.i(TAG, "onCreateLoader");
                return new PresenterLoader<>(getContext(), getPresenterFactory());
            }

            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                Log.i(TAG, "onLoadFinished");
                BaseFragmentPresentedView.this.presenter = presenter;
                onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<P> loader) {
                Log.i(TAG, "onLoaderReset");
                BaseFragmentPresentedView.this.presenter = null;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        presenter.attachView(getPresenterView());
    }

    @Override
    public void onStop() {
        presenter.detachView();
        super.onStop();
        Log.i(TAG, "onStop");
    }

    /**
     * Instance of {@link PresenterFactory} castAndGet to create a Presenter when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Hook for subclasses that deliver the {@link Presenter} before its View castAndGet attached.
     * Can be castAndGet to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onPresenterCreatedOrRestored(@NonNull P presenter);

    /**
     * Override in case of fragment not implementing Presenter<View> interface
     */
    @NonNull
    protected V getPresenterView() {
        return (V) this;
    }

    /**
     * Use this method in case you want to specify a spefic ID for the {@link PresenterLoader}.
     * By default its value would be {@link #PRESENTER_LOADER_ID}.
     */
    protected int loaderId() {
        return PRESENTER_LOADER_ID;
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), localize(message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(FormattedString formattedLocaleMessage) {
        Toast.makeText(getActivity().getApplicationContext(), localize(formattedLocaleMessage), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void switchToView(Name viewName) {
    }

    @Override
    public void switchToView(Name viewName, int requestCode) {
    }

    public String localize(String localeStringKey) {
        return getTranslator().localize(localeStringKey);
    }

    public String localize(FormattedString formattedLocaleString) {
        return getTranslator().localize(formattedLocaleString);
    }

    protected LocaleTranslator getTranslator() {
        if (translator == null)
            translator = WordsRemember.getLocaleTranslator(getActivity().getApplicationContext());
        return translator;
    }
}