package com.matteoveroni.wordsremember.persistency.commands;

import android.content.ContentResolver;
import android.net.Uri;

import com.matteoveroni.wordsremember.scene_dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.scene_dictionary.events.vocable_translations.EventAsyncDeleteVocableTranslationCompleted;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncDeleteCommand extends AsyncCommand {

    private final Uri commandTargetUri;
    private final String selection;
    private final String selectonArgs[];
    private final Object nextCommand;

    private int rowDeletedResult;

    public AsyncDeleteCommand(ContentResolver contentResolver, Uri commandTargetUri, String selection, String[] selectonArgs) {
        this(contentResolver, commandTargetUri, selection, selectonArgs, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncDeleteCommand(ContentResolver contentResolver, Uri commandTargetUri, String selection, String[] selectonArgs, Object nextCommand) {
        super(contentResolver);
        this.commandTargetUri = commandTargetUri;
        this.selection = selection;
        this.selectonArgs = selectonArgs;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startDelete(0, nextCommand, commandTargetUri, selection, selectonArgs);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        this.rowDeletedResult = result;
        executeCommand((AsyncCommand) nextCommand);
        dispatchCompletionEvent();
    }

    @Override
    public void dispatchCompletionEvent() {
        EventBus eventbus = EventBus.getDefault();

        if (commandTargetUri.equals(VocablesContract.CONTENT_URI)) {
            eventbus.postSticky(new EventAsyncDeleteVocableCompleted(rowDeletedResult));
        } else if (commandTargetUri.equals(VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI)) {
            eventbus.postSticky(new EventAsyncDeleteVocableTranslationCompleted(rowDeletedResult));
        }
    }

    private void executeCommand(AsyncCommand command) {
        command.execute();
    }
}
