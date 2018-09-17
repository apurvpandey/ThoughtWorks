package com.thoughtworks.apurvpandey.thoughtworks.homeScreen;

import com.android.volley.VolleyError;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;

import java.util.List;

public class HomeScreenPresenter implements HomeScreenInteractor.OnApiCallFinishedListener {

    private HomeScreenContract contract;
    private HomeScreenInteractor interactor;

    public HomeScreenPresenter(HomeScreenContract contract, HomeScreenInteractor interactor) {
        this.contract = contract;
        this.interactor = interactor;
    }

    public void fetchBeerList(VolleySingleton instance) {
        if (contract != null) {
            contract.showProgress();
        }

        interactor.makeApiCall(this, instance);
    }

    public void onDestroy() {
        contract = null;
    }

    public void showDialog(HomeScreenActivity.DialogType type) {
        if (contract != null) {
            contract.showDialog(type);
        }
    }

    @Override
    public void onSuccess(List<Beer> beerList) {
        if (contract != null) {
            contract.hideProgress();
            contract.showBeerList(beerList);
        }
    }

    @Override
    public void onFailure(VolleyError error) {
        if (contract != null) {
            contract.hideProgress();
            contract.showError(error);
        }
    }

    public void sortBeerList(List<Beer> beerList, String filterField, String filterValue) {
        List<Beer> list = interactor.sortList(beerList, filterField, filterValue);
        contract.showBeerList(list);
        if (contract != null) {
            contract.hideProgress();
        }
    }

    public void showProgress() {
        if (contract != null) {
            contract.showProgress();
        }
    }

    public void performSearch(String query, List<Beer> beerList) {
        List<Beer> list = interactor.filterListByName(beerList, query);
        contract.showBeerList(list);
        if (contract != null) {
            contract.hideProgress();
        }
    }

}
