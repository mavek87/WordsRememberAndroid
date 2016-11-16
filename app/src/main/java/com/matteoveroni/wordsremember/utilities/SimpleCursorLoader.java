package com.matteoveroni.wordsremember.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * A CursorLoader that doesn't need a ContentProvider.
 *
 * @author Christophe Beyls
 *
 */
public abstract class SimpleCursorLoader extends AsyncTaskLoader<Cursor> {
    private final ForceLoadContentObserver observer;
    private Cursor cursor;

    public SimpleCursorLoader(Context context) {
        super(context);
        observer = new ForceLoadContentObserver();
    }

    /* Runs on a worker thread */
    @Override
    public Cursor loadInBackground() {
        Cursor cursor = getCursor();
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
            cursor.registerContentObserver(observer);
        }
        return cursor;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = this.cursor;
        this.cursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    /**
     * Starts an asynchronous load of the data. When the result is ready the callbacks will be called on the UI thread. If a previous load has been completed
     * and is still valid the result may be passed to the callbacks immediately.
     * <p>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (cursor != null) {
            deliverResult(cursor);
        }
        if (takeContentChanged() || cursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        // Retry a refresh the next time the loader is started
        onContentChanged();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = null;
    }

    protected abstract Cursor getCursor();
}