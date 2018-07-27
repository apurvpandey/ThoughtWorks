package com.thoughtworks.apurvpandey.thoughtworks.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.thoughtworks.apurvpandey.thoughtworks.R;
import com.thoughtworks.apurvpandey.thoughtworks.homeScreen.HomeScreenContract;


public class DialogUtil {

    public static void prepareDialog(Context context, String title, String message, String positiveButtonMessage, final HomeScreenContract.OnDialogSelectionListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setPositiveButton(positiveButtonMessage,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositiveButtonSelection();
                    }
                });
        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        listener.onNegativeButtonSelectionListener();
                    }
                });

        alertDialog.show();
    }

    public static void prepareDialogWithText(Context context, String title, String message, String hintMessage, String positiveButtonMessage, final HomeScreenContract.OnDialogSelectionListener listener) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 00, 50, 00);
        input.setLayoutParams(lp);
        input.setGravity(Gravity.CENTER);
        input.setHint(hintMessage);
        input.setFocusable(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);

        alertDialog.setPositiveButton(positiveButtonMessage,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String filterField = input.getText().toString();
                        listener.onPositiveButtonSelection(filterField);
                    }
                });

        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        listener.onNegativeButtonSelectionListener();
                    }
                });

        alertDialog.show();
    }
}
