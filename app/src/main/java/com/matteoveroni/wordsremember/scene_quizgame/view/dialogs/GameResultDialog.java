package com.matteoveroni.wordsremember.scene_quizgame.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * @author Matteo Veroni
 */

public class GameResultDialog extends BaseTitleMessageDialog {

    public static final String TAG = GameResultDialog.class.getSimpleName();

    public interface GameResultDialogListener {
        void confirmGameResultDialogAction();
    }

    public static GameResultDialog newInstance(String title, String message) {
        GameResultDialog dialogFragment = new GameResultDialog();
        initDialogData(title, message, dialogFragment);
        return dialogFragment;
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
                        ((GameResultDialogListener) getActivity()).confirmGameResultDialogAction();
                    }
                })
                .create();
        setCancelable(false);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
