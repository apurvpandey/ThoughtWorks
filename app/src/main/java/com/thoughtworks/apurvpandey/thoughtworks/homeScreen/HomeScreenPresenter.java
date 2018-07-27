package com.thoughtworks.apurvpandey.thoughtworks.homeScreen;

import android.support.annotation.WorkerThread;

import com.android.volley.VolleyError;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;

import java.util.ArrayList;

public class HomeScreenPresenter implements HomeScreenInteractor.OnApiCallFinishedListener {

    private HomeScreenContract contract;
    private HomeScreenInteractor interactor;

    public HomeScreenPresenter(HomeScreenContract contract, HomeScreenInteractor interactor) {
        this.contract = contract;
        this.interactor = interactor;
    }

    public void fetchBeerList() {
        if (contract != null) {
            contract.showProgress();
        }

        interactor.makeApiCall(this);
    }

    public void onDestroy() {
        contract = null;
    }

    @WorkerThread
    public void showDialog(HomeScreenActivity.DialogType type) {
        if (contract != null) {
            contract.showDialog(type);
        }
    }

    @Override
    public void onSuccess(ArrayList<Beer> beerList) {
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

    public void sortBeerList(ArrayList<Beer> beerList) {
        ArrayList<Beer> list = interactor.sortList(beerList);
        contract.showBeerList(list);
        if (contract != null) {
            contract.hideProgress();
        }
    }

    public void filterBeerList(ArrayList<Beer> beerList, String filterField) {
        ArrayList<Beer> list = interactor.filterListByAbvContent(beerList, filterField);
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

    public void performSearch(String query, ArrayList<Beer> beerList) {
        ArrayList<Beer> list = interactor.filterListByName(beerList, query);
        contract.showBeerList(list);
        if (contract != null) {
            contract.hideProgress();
        }
    }
}
