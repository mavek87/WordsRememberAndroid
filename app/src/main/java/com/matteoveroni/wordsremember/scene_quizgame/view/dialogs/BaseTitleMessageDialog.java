package com.matteoveroni.wordsremember.scene_quizgame.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Matteo Veroni
 */

public class BaseTitleMessageDialog extends DialogFragment {

    protected String title;
    protected static final String DIALOG_TITLE_KEY = "title";

    protected String message;
    protected static final String DIALOG_MESSAGE_KEY = "message";

    public static void initDialogData(String title, String message, DialogFragment dialogFragment) {
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE_KEY, title);
        args.putString(DIALOG_MESSAGE_KEY, message);
        dialogFragment.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        title = getArguments().getString(DIALOG_TITLE_KEY);
        message = getArguments().getString(DIALOG_MESSAGE_KEY);
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
