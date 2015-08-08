package com.cert.eli.gigigoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.cert.eli.gigigoapp.Utils.Utility;

/**
 * Created by eli on 06/08/15.
 */
public class DialogFilter extends DialogFragment {

    public static boolean[] checked = new boolean[1];
    DialogInterfaceFilter mCallback;

    interface DialogInterfaceFilter{
        void onDialogOk(boolean check);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallback = (DialogInterfaceFilter)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        boolean optionViral = Utility.getViralOption(getActivity());
            checked[0] = optionViral;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.dialog_title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.dialog_option, checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if(isChecked){
                                    checked[0]=true;}
                                else{ checked[0]=false;}
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        mCallback.onDialogOk(checked[0]);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    public void onDestroy(){
        super.onDestroy();
        mCallback=null;
    }

}
