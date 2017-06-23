package com.matteoveroni.wordsremember.quizgame.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * @author Matteo Veroni
 */

public class QuizResultDialog extends DialogFragment {

    private String title;
    private static final String DIALOG_TITLE_KEY = "dialog_title";

    private String message;
    private static final String DIALOG_MESSAGE_KEY = "dialog_message";

    public interface QuizResultDialogListener {
        void quizResultDialogConfirm();
    }

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static QuizResultDialog newInstance(String title, String message) {
        QuizResultDialog dialogFragment = new QuizResultDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE_KEY, title);
        args.putString(DIALOG_MESSAGE_KEY, message);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        title = getArguments().getString(DIALOG_TITLE_KEY);
        message = getArguments().getString(DIALOG_MESSAGE_KEY);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //         TODO: try a way to pass a Drawable to this object
//                .setIcon(img_alertDialog);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((QuizResultDialogListener) getActivity()).quizResultDialogConfirm();
                    }
                })
                .create();
        setCancelable(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
