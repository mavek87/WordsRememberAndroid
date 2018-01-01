package com.matteoveroni.wordsremember.scene_quizgame.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question.QuestionCompleted;

/**
 * @author Matteo Veroni
 */

public class QuestionResultDialog extends DialogFragment {

    public static final String TAG = QuestionResultDialog.class.getSimpleName();

    private QuestionCompleted.AnswerResult result;
    private static final String DIALOG_RESULT_KEY = "dialog_result";

    private String message;
    private static final String DIALOG_MESSAGE_KEY = "dialog_message";

    public interface QuizResultDialogListener {
        void confirmQuizResultDialogAction();
    }

    public static QuestionResultDialog newInstance(QuestionCompleted.AnswerResult result, String message) {
        QuestionResultDialog dialogFragment = new QuestionResultDialog();

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
        result = (QuestionCompleted.AnswerResult) getArguments().getSerializable(DIALOG_RESULT_KEY);
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
                        ((QuizResultDialogListener) getActivity()).confirmQuizResultDialogAction();
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
