package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.translation.EventAsyncSaveTranslationCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncSaveVocableCompleted;
import com.matteoveroni.wordsremember.dictionary.events.vocable_translations.EventAsyncSaveVocableTranslationCompleted;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncInsertCommand extends AsyncCommand {

    private final Uri commandTargetUri;
    private final ContentValues values;
    private final Object nextCommand;

    private Uri insertCompleteUri;

    public AsyncInsertCommand(ContentResolver contentResolver, Uri commandTargetUri, ContentValues values) {
        this(contentResolver, commandTargetUri, values, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncInsertCommand(ContentResolver contentResolver, Uri commandTargetUri, ContentValues values, Object nextCommand) {
        super(contentResolver);
        this.commandTargetUri = commandTargetUri;
        this.values = values;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startInsert(0, null, commandTargetUri, values);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        insertCompleteUri = uri;
        dispatchCompletionEvent();
        executeCommand((AsyncCommand) nextCommand);
    }

    @Override
    public void dispatchCompletionEvent() {
        long id = Long.valueOf(insertCompleteUri.getLastPathSegment());

        if (commandTargetUri.equals(VocablesContract.CONTENT_URI)) {
            EventBus.getDefault().postSticky(new EventAsyncSaveVocableCompleted(id));

        } else if (commandTargetUri.equals(TranslationsContract.CONTENT_URI)) {
            EventBus.getDefault().postSticky(new EventAsyncSaveTranslationCompleted(id));

        } else if (commandTargetUri.equals(VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI)) {
            EventBus.getDefault().postSticky(new EventAsyncSaveVocableTranslationCompleted(1,1));
        }
    }

    private void executeCommand(AsyncCommand command) {
        command.execute();
    }
}
