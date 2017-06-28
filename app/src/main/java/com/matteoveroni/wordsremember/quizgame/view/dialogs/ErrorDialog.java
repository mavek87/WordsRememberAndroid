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

public class ErrorDialog extends DialogFragment {

    public static final String TAG = ErrorDialog.class.getSimpleName();

    private String title;
    private static final String DIALOG_TITLE_KEY = "title";

    private String message;
    private static final String DIALOG_MESSAGE_KEY = "message";

    public interface ErrorDialogListener {
        void confirmErrorDialogAction();
    }

    public static ErrorDialog newInstance(String title, String message) {
        ErrorDialog errorDialogFragment = new ErrorDialog();

        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE_KEY, title);
        args.putString(DIALOG_MESSAGE_KEY, message);
        errorDialogFragment.setArguments(args);

        return errorDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: check if setRetainInstanceIsNeeded
        setRetainInstance(true);
        title = getArguments().getString(DIALOG_TITLE_KEY);
        message = getArguments().getString(DIALOG_MESSAGE_KEY);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ErrorDialogListener) getActivity()).confirmErrorDialogAction();
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
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
