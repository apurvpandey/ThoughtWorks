package com.thoughtworks.apurvpandey.thoughtworks.utils;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

public class MessageUtility {

    public static void showSnackBar(View layout, String message) {
        Snackbar snackbar = Snackbar
                .make(layout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static boolean isNotNullOrEmpty(final String stringValue) {
        return TextUtils.isEmpty(stringValue.trim());
    }
}
