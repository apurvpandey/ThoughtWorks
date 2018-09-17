package com.thoughtworks.apurvpandey.thoughtworks.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.thoughtworks.apurvpandey.thoughtworks.R;
import com.thoughtworks.apurvpandey.thoughtworks.homeScreen.HomeScreenContract;

import java.util.ArrayList;
import java.util.List;


public class DialogUtil {

    public static void prepareDialog(Context context, String title, String message, String positiveButtonMessage, final HomeScreenContract.OnDialogSelectionListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);


        alertDialog.setPositiveButton(positiveButtonMessage,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositiveButtonSelection("", "");
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
                        listener.onPositiveButtonSelection(filterField, "");
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

    public static void showSortDialog(Context context, String title, final HomeScreenContract.OnDialogSelectionListener listener) {

        final Dialog dialog = new Dialog(context);
        dialog.setTitle(title);
        dialog.setContentView(R.layout.radio_button_dialog);
        List<String> stringList = new ArrayList<>();
        stringList.add(context.getResources().getString(R.string.sort_beer_alphabetically));
        stringList.add(context.getResources().getString(R.string.sort_by_ascending_alcohol));
        stringList.add(context.getResources().getString(R.string.sort_by_descending_alcohol));

        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(stringList.get(i));
            radioGroup.addView(radioButton);
        }

        dialog.show();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        String selection = btn.getText().toString();
                        listener.onPositiveButtonSelection(selection, "");
                        dialog.hide();
                    }
                }
            }
        });

    }

    public static void showFilterDialog(final Context context, String title, final HomeScreenContract.OnDialogSelectionListener listener) {

        final Dialog dialog = new Dialog(context);
        dialog.setTitle(title);
        dialog.setContentView(R.layout.radio_button_dialog);
        List<String> stringList = new ArrayList<>();
        stringList.add(context.getResources().getString(R.string.beer_style));
        stringList.add(context.getResources().getString(R.string.ounces));
        stringList.add(context.getResources().getString(R.string.ibu));

        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(stringList.get(i));
            radioGroup.addView(radioButton);
        }

        dialog.show();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        String selection = btn.getText().toString();
                        //listener.onPositiveButtonSelection(selection);
                        openFilterOptions(context, "Filter", "Enter value", "Enter value here", "Filter", listener, selection);
                        dialog.hide();
                    }
                }
            }
        });

    }


    private static void openFilterOptions(Context context, String title, String message, String hintMessage, String positiveButtonMessage, final HomeScreenContract.OnDialogSelectionListener listener, final String selection) {

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
                        String filterValue = input.getText().toString();
                        listener.onPositiveButtonSelection(selection, filterValue);
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
