package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.events.vocable.EventAsyncVocableDeletionComplete;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncDeleteCommand extends AsyncCommand {

    private final Uri commandTargetUri;
    private final String selection;
    private final String selectonArgs[];
    private final Object nextCommand;

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
    protected void onDeleteComplete(int token, Object nextCommand, int result) {
        dispatchCompletionEvent(result);
        executeCommand((AsyncCommand) nextCommand);
    }

    private void executeCommand(AsyncCommand command) {
        command.execute();
    }

    private void dispatchCompletionEvent(int result) {
        EventBus.getDefault().postSticky(new EventAsyncVocableDeletionComplete(result));
    }
}
