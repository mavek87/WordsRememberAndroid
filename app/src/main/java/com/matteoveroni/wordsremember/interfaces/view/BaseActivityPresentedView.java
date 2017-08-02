package com.matteoveroni.wordsremember.interfaces.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.myutils.FormattedString;
import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.WordsRemember;
import com.matteoveroni.wordsremember.interfaces.presenter.Presenter;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterFactory;
import com.matteoveroni.wordsremember.interfaces.presenter.PresenterLoader;
import com.matteoveroni.wordsremember.localization.LocaleTranslator;

/**
 * Useful resources: https://github.com/czyrux/MvpLoaderSample/blob/master/app/src/main/java/de/czyrux/mvploadersample/base/BasePresenterActivity.java
 */

public abstract class BaseActivityPresentedView<V, P extends Presenter<V>> extends AppCompatActivity implements View {

    private static final String TAG = TagGenerator.tag(BaseActivityPresentedView.class);
    private static final int PRESENTER_LOADER_ID = 1;
    private P presenter;
    private LocaleTranslator translator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Loader<P> loader = getSupportLoaderManager().getLoader(loaderId());
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((PresenterLoader<P>) loader).getPresenter();
            onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader() {
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        getSupportLoaderManager().initLoader(loaderId(), null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                Log.d(TAG, "onCreateLoader");
                return new PresenterLoader<>(BaseActivityPresentedView.this, getPresenterFactory());
            }

            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                Log.d(TAG, "onLoadFinished");
                BaseActivityPresentedView.this.presenter = presenter;
                onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<P> loader) {
                Log.d(TAG, "onLoaderReset");
                BaseActivityPresentedView.this.presenter = null;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(getPresenterView());
    }

    @Override
    protected void onStop() {
        presenter.detachView();
        super.onStop();
    }

    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Hook for subclasses that deliver the {@link Presenter} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onPresenterCreatedOrRestored(P presenter);

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
        Toast.makeText(getApplicationContext(), localize(message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(FormattedString formattedLocaleMessage) {
        Toast.makeText(getApplicationContext(), localize(formattedLocaleMessage), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void switchView(View.Name viewName) {
        startActivity(new Intent(getApplicationContext(), viewName.getViewClass()));
    }

    public String localize(String localeStringKey) {
        return getTranslator().localize(localeStringKey);
    }

    public String localize(FormattedString formattedLocaleString) {
        return getTranslator().localize(formattedLocaleString);
    }

    public void showAndroidKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideAndroidKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    protected LocaleTranslator getTranslator() {
        if (translator == null)
            translator = WordsRemember.getLocaleTranslator(getApplicationContext());
        return translator;
    }

    protected void setupAndShowToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(Str.concat(WordsRemember.ABBREVIATED_NAME, " - ", title));
        }
        setSupportActionBar(toolbar);
    }
}