package com.thoughtworks.apurvpandey.thoughtworks.homeScreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.thoughtworks.apurvpandey.thoughtworks.R;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;
import com.thoughtworks.apurvpandey.thoughtworks.utils.DialogUtil;
import com.thoughtworks.apurvpandey.thoughtworks.utils.MessageUtility;

import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements HomeScreenContract, HomeScreenContract.OnDialogSelectionListener, MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MaterialSearchView materialSearchView;
    private List<Beer> beerList;
    private HomeScreenPresenter homeScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        homeScreenPresenter = new HomeScreenPresenter(this, new HomeScreenInteractor());
        homeScreenPresenter.fetchBeerList(VolleySingleton.getInstance(this));
    }


    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressbar);

        materialSearchView = findViewById(R.id.search_view);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initListener() {
        materialSearchView.setOnQueryTextListener(this);
        materialSearchView.setOnSearchViewListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.ic_filter:
                homeScreenPresenter.showDialog(DialogType.FILTER);
                break;

            case R.id.ic_sort:
                homeScreenPresenter.showDialog(DialogType.SORT);
                break;

            case R.id.ic_search:
                materialSearchView.showSearch();
                break;
            case R.id.ic_portfolio:
//                homeScreenPresenter.fetchBeerList(VolleySingleton.getInstance(this));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showBeerList(List<Beer> beerList) {
        HomeScreenListAdapter homeScreenListAdapter = new HomeScreenListAdapter(this, beerList);
        recyclerView.setAdapter(homeScreenListAdapter);
        homeScreenListAdapter.notifyDataSetChanged();
        this.beerList = beerList;
    }

    @Override
    public void showError(VolleyError error) {
        MessageUtility.showSnackBar(recyclerView, error.getMessage());
    }

    @Override
    public void showDialog(DialogType type) {
        switch (type) {
            case SORT:
                String title = getString(R.string.sort_beer);
                String message = getString(R.string.sort_beer_alphabetically);
                String positiveButtonMessage = getString(R.string.sort);
                //DialogUtil.prepareDialog(this, title, message, positiveButtonMessage, this);
                DialogUtil.showSortDialog(this, title, this);
                break;
            case FILTER:
                String titleFilter = getString(R.string.filter_beer);
                String messageFilter = getString(R.string.filter_by);
                String positiveButtonMessageFilter = getString(R.string.filter);
                String hint = getString(R.string.abv_content);
                //DialogUtil.prepareDialogWithText(this, titleFilter, messageFilter, hint, positiveButtonMessageFilter, this);
                DialogUtil.showFilterDialog(this, titleFilter, this);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeScreenPresenter.onDestroy();
    }

    @Override
    public void onPositiveButtonSelection(String filterField, String filterValue) {
        homeScreenPresenter.showProgress();
        if (MessageUtility.isNotNullOrEmpty(filterField)) {
            homeScreenPresenter.sortBeerList(beerList, filterField, filterValue);
            //perform filter below
        }
    }

    @Override
    public void onNegativeButtonSelectionListener() {
        String message = getString(R.string.action_cancelled);
        MessageUtility.showSnackBar(recyclerView, message);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        materialSearchView.hideKeyboard(recyclerView);
        homeScreenPresenter.showProgress();
        homeScreenPresenter.performSearch(query.toLowerCase(), beerList);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {
//        homeScreenPresenter.fetchBeerList(VolleySingleton.getInstance(this));
    }

    public enum DialogType {
        SORT,
        FILTER
    }

}
