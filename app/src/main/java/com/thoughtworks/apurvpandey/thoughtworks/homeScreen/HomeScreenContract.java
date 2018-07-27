package com.thoughtworks.apurvpandey.thoughtworks.homeScreen;

import com.android.volley.VolleyError;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;

import java.util.ArrayList;

public interface HomeScreenContract {

    void showProgress();

    void hideProgress();

    void showBeerList(ArrayList<Beer> beerList);

    void showError(VolleyError error);

    void showDialog(HomeScreenActivity.DialogType type);

    interface OnDialogSelectionListener {
        void onPositiveButtonSelection();

        void onPositiveButtonSelection(String filterField);

        void onNegativeButtonSelectionListener();
    }
}
