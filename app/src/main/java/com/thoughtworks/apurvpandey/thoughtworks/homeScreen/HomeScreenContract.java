package com.thoughtworks.apurvpandey.thoughtworks.homeScreen;

import com.android.volley.VolleyError;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;

import java.util.List;

public interface HomeScreenContract {

    void showProgress();

    void hideProgress();

    void showBeerList(List<Beer> beerList);

    void showError(VolleyError error);

    void showDialog(HomeScreenActivity.DialogType type);

    interface OnDialogSelectionListener {
        //void onPositiveButtonSelection();

        void onPositiveButtonSelection(String filterField, String filterValue);

        void onNegativeButtonSelectionListener();
    }


}
