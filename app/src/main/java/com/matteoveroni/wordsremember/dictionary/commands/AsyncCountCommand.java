package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

/**
 * @author Matteo Veroni
 */

public class AsyncCountCommand extends AsyncQueryCommand {

    public AsyncCountCommand(ContentResolver contentResolver, Uri uri, String columnToCount) {
        this(contentResolver, uri, columnToCount, null, null, null, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncCountCommand(ContentResolver contentResolver, Uri uri, String columnToCount, String selection, String[] selectionArgs, String orderBy, Object nextCommand) {
        super(
                contentResolver,
                uri,
                new String[]{"COUNT (" + columnToCount + ")"},
                selection,
                selectionArgs,
                orderBy,
                nextCommand
        );
    }

    @Override
    public void dispatchCompletionEvent() {
        queryCompleteCursor.moveToFirst();
        int count = queryCompleteCursor.getInt(0);
        Log.i("AAAAAAA", count + "");
//        EventBus.getDefault().postSticky(event);
    }
}
