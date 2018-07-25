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

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class HomeScreenActivity extends AppCompatActivity implements HomeScreenContract, HomeScreenContract.OnDialogSelectionListener{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HomeScreenListAdapter homeScreenListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SmoothProgressBar smoothProgressBar;
    private MaterialSearchView materialSearchView;
    private ArrayList<Beer> beerList;
    private HomeScreenPresenter homeScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        homeScreenPresenter = new HomeScreenPresenter(this, new HomeScreenInteractor());
        homeScreenPresenter.fetchBeerList();
    }


    private void initView() {

        recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressbar);
        smoothProgressBar =  findViewById(R.id.smoothProgress);
        materialSearchView = findViewById(R.id.search_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        setSearchViewComponents();
    }

    //This is under refractoring process
    private void setSearchViewComponents() {

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchedResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!MessageUtility.isNotNullOrEmpty(newText)){
                    /*homeScreenPresenter.showProgress();
                    homeScreenPresenter.updateView(beerList);*/
                }
                return true;

            }
        });

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                homeScreenListAdapter = new HomeScreenListAdapter(HomeScreenActivity.this, beerList);
                recyclerView.setAdapter(homeScreenListAdapter);
                homeScreenListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //This is under refractoring process
    private void getSearchedResult(String query) {

        materialSearchView.hideKeyboard(recyclerView);
        query = query.toLowerCase();
        final ArrayList<Beer> filteredList = new ArrayList<>();
        for (int i = 0; i < beerList.size(); i++) {
            final String beerName = beerList.get(i).getName().toLowerCase();
            if (beerName.contains(query)) {
                filteredList.add(beerList.get(i));
            }
        }

        homeScreenListAdapter = new HomeScreenListAdapter(this, filteredList);
        recyclerView.setAdapter(homeScreenListAdapter);
        homeScreenListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);

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
                homeScreenPresenter.fetchBeerList();
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
    public void showBeerList(ArrayList<Beer> beerList) {
        homeScreenListAdapter = new HomeScreenListAdapter(this, beerList);
        recyclerView.setAdapter(homeScreenListAdapter);
        homeScreenListAdapter.notifyDataSetChanged();
        this.beerList = (ArrayList<Beer>) beerList.clone();
    }

    @Override
    public void showError(VolleyError error) {
        MessageUtility.showSnackBar(recyclerView, error.getMessage());
    }

    @Override
    public void showDialog(DialogType type) {
        switch (type){
            case SORT:
                String title = getString(R.string.sort_beer);
                String message = getString(R.string.sort_beer_alphabetically);
                String positiveButtonMessage = getString(R.string.sort);
                DialogUtil.prepareDialog(this, title, message, positiveButtonMessage, this);
                break;
            case FILTER:
                String titleFilter = getString(R.string.filter_beer);
                String messageFilter = getString(R.string.filter_by);
                String positiveButtonMessageFilter = getString(R.string.filter);
                String hint =  getString(R.string.abv_content);
                DialogUtil.prepareDialogWithText(this, titleFilter, messageFilter, hint, positiveButtonMessageFilter, this);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeScreenPresenter.onDestroy();
    }

    @Override
    public void onPositiveButtonSelection() {
        homeScreenPresenter.showProgress();
        homeScreenPresenter.sortBeerList(beerList);
    }

    @Override
    public void onPositiveButtonSelection(String filterField) {
        //Can below code be refractored more?
        homeScreenPresenter.showProgress();
        if (MessageUtility.isNotNullOrEmpty(filterField)) {
            homeScreenPresenter.filterBeerList(beerList, filterField);
        }
    }

    @Override
    public void onNegativeButtonSelectionListener() {
        String message = getString(R.string.action_cancelled);
        MessageUtility.showSnackBar(recyclerView, message);
    }

    //Is using enum a better approach
    public enum DialogType {
        SORT,
        FILTER
    }
}
