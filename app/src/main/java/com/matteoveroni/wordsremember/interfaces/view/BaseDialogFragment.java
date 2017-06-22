package com.matteoveroni.wordsremember.interfaces.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import com.matteoveroni.wordsremember.R;

/**
 * @author Matteo Veroni
 */

public class BaseDialogFragment extends DialogFragment {

//    public interface YesNoListener {
//        void onConfirm();
//
//        void onCancel();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (!(context instanceof YesNoListener)) {
//            throw new ClassCastException(context.toString() + " must implement YesNoListener");
//        }
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new AlertDialog.Builder(getActivity())
//                .setTitle("TITOLO")
//                .setMessage("MESSAGE")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((YesNoListener) getActivity()).onConfirm();
//                    }
//                })
//                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((YesNoListener) getActivity()).onCancel();
//                    }
//                })
//                .setCancelable(false)
//                .create();
//    }
//
////public class BaseDialogFragment extends DialogFragment {
////    /**
////     * The system calls this to get the DialogFragment's layout, regardless
////     * of whether it's being displayed as a dialog or an embedded fragment.
////     */
////
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        // Inflate the layout to use as dialog or embedded fragment
////        return inflater.inflate(R.layout.purchase_items, container, false);
////    }
////
////    /**
////     * The system calls this only when creating the layout in a dialog.
////     */
////    @Override
////    public Dialog onCreateDialog(Bundle savedInstanceState) {
////        // The only reason you might override this method when using onCreateView() is
////        // to modify any dialog characteristics. For example, the dialog includes a
////        // title by default, but your custom layout might not need it. So here you can
////        // remove the dialog title, but you must call the superclass to get the Dialog.
////        Dialog dialog = super.onCreateDialog(savedInstanceState);
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        return dialog;
////    }
////}
//

}
