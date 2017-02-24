package com.matteoveroni.wordsremember.dictionary.model.commands;

import android.content.ContentResolver;

import com.matteoveroni.wordsremember.dictionary.events.EventAsyncVocableDeletionComplete;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class AsyncDeleteCommand extends AsyncCommand {

    private final CommandTarget target;
    private final String selection;
    private final String selectonArgs[];
    private final Object nextCommand;

    public AsyncDeleteCommand(ContentResolver contentResolver, CommandTarget target, String selection, String[] selectonArgs) {
        this(contentResolver, target, selection, selectonArgs, new AsyncNoOperationCommand(contentResolver));
    }

    public AsyncDeleteCommand(ContentResolver contentResolver, CommandTarget target, String selection, String[] selectonArgs, Object nextCommand) {
        super(contentResolver);
        this.target = target;
        this.selection = selection;
        this.selectonArgs = selectonArgs;
        this.nextCommand = nextCommand;
    }

    @Override
    public void execute() {
        startDelete(0, nextCommand, target.getContentUri(), selection, selectonArgs);
    }

    @Override
    protected void onDeleteComplete(int token, Object nextCommand, int result) {
        dispatchCompletionEvent(result);
        executeNextCommand();
    }

    private void executeNextCommand() {
        ((AsyncCommand) nextCommand).execute();
    }

    private void dispatchCompletionEvent(int result) {
        EventBus.getDefault().postSticky(new EventAsyncVocableDeletionComplete(result));
    }
}
