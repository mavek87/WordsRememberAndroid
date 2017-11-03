package com.matteoveroni.wordsremember.scene_quizgame.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * @author Matteo Veroni
 */

public class ErrorDialog extends BaseTitleMessageDialog {

    public static final String TAG = ErrorDialog.class.getSimpleName();

    public interface ErrorDialogListener {
        void confirmErrorDialogAction();
    }

    public static ErrorDialog newInstance(String title, String message) {
        ErrorDialog errorDialogFragment = new ErrorDialog();
        initDialogData(title, message, errorDialogFragment);
        return errorDialogFragment;
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
}
