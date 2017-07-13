package com.matteoveroni.wordsremember.dictionary.commands;

import android.content.ContentResolver;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncDeleteVocableCompleted;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;

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
        dispatchCompletionEvent();
        executeCommand((AsyncCommand) nextCommand);
    }

    @Override
    public void dispatchCompletionEvent() {
        if (commandTargetUri.equals(VocablesContract.CONTENT_URI)) {
            EventBus.getDefault().postSticky(new EventAsyncDeleteVocableCompleted(rowDeletedResult));
        }
    }

    private void executeCommand(AsyncCommand command) {
        command.execute();
    }
}
