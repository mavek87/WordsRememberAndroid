package com.matteoveroni.wordsremember.quizgame.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.quizgame.pojos.Quiz;

/**
 * @author Matteo Veroni
 */

public class QuizResultDialog extends DialogFragment {

    public static final String TAG = "QuizResultDialog";

    private Quiz.FinalResult result;
    private static final String DIALOG_RESULT_KEY = "dialog_result";

    private String message;
    private static final String DIALOG_MESSAGE_KEY = "dialog_message";

    public interface QuizResultDialogListener {
        void quizResultDialogConfirm();
    }

    public static QuizResultDialog newInstance(Quiz.FinalResult result, String message) {
        QuizResultDialog dialogFragment = new QuizResultDialog();

        Bundle args = new Bundle();
        args.putSerializable(DIALOG_RESULT_KEY, result);
        args.putString(DIALOG_MESSAGE_KEY, message);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: check if setRetainInstanceIsNeeded
        setRetainInstance(true);
        result = (Quiz.FinalResult) getArguments().getSerializable(DIALOG_RESULT_KEY);
        message = getArguments().getString(DIALOG_MESSAGE_KEY);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Drawable img_alertDialog;
        String title;
        switch (result) {
            case CORRECT:
                title = getString(R.string.correctAnswer);
                img_alertDialog = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_correct, null);
                break;
            case WRONG:
                title = getString(R.string.wrongAnswer);
                img_alertDialog = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_wrong, null);
                break;
            default:
                throw new RuntimeException("Unknown quiz result");
        }

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setIcon(img_alertDialog)
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

    // TODO: check if this method is needed
    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
