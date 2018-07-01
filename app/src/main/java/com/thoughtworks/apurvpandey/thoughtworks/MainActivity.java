package com.thoughtworks.apurvpandey.thoughtworks;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.thoughtworks.apurvpandey.thoughtworks.adapter.BeerListAdapter;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;
import com.thoughtworks.apurvpandey.thoughtworks.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "http://starlord.hackerearth.com/beercraft";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private BeerListAdapter beerListAdapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager linearLayoutManager;
    private SmoothProgressBar smoothProgressBar;
    private MaterialSearchView materialSearchView;
    private ArrayList<Beer> beerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        getBeerList();

    }


    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.smoothProgress);
        materialSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item - " + totalItemCount);
                            //smoothProgressBar.setVisibility(View.VISIBLE);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //restart loader to fetch more values
                                            //for first 20 items
                                            Bundle args = new Bundle();
                                            args.putInt("start", 0);
                                            args.putInt("end", totalItemCount + 19);
                                        }
                                    }, 2000);
                                }
                            });
                        }
                    }
                }
            }
        });


        setSearchViewComponents();
    }


    private void getBeerList() {

        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BASE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseBeerList(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void parseBeerList(JSONArray response) {

        beerList = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {

            try {
                Beer beer = new Beer();
                JSONObject jsonProject = response.getJSONObject(i);

                beer.setAbv(jsonProject.getString("abv"));
                beer.setIbu(jsonProject.getString("ibu"));
                beer.setId(jsonProject.getInt("id"));
                beer.setName(jsonProject.getString("name"));
                beer.setStyle(jsonProject.getString("style"));
                beer.setOunces(jsonProject.getDouble("ounces"));

                beerList.add(beer);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        showBeerList();
    }

    private void showBeerList() {
        beerListAdapter = new BeerListAdapter(this, beerList);
        recyclerView.setAdapter(beerListAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void setSearchViewComponents() {

        materialSearchView.setVoiceSearch(true);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                getSearchedResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;

            }
        });

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                beerListAdapter = new BeerListAdapter(MainActivity.this, beerList);
                recyclerView.setAdapter(beerListAdapter);
                beerListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

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
        beerListAdapter = new BeerListAdapter(this, filteredList);
        recyclerView.setAdapter(beerListAdapter);
        beerListAdapter.notifyDataSetChanged();
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
                filter();
                break;

            case R.id.ic_sort:
                sort();
                break;

            case R.id.ic_search:
                materialSearchView.showSearch();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void sort() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sort Beers");
        alertDialog.setMessage("Sort Alphabetically?");

        alertDialog.setPositiveButton("Sort",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sortBeerList(beerList);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    private void filter() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Filter Beers");
        alertDialog.setMessage("Filter by");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 00, 50, 00);
        input.setLayoutParams(lp);
        input.setGravity(Gravity.CENTER);
        input.setHint(getString(R.string.filter_hint));
        input.setFocusable(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Filter",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String filterField = input.getText().toString();

                        if (!filterField.equals("")) {
                           }
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    private void sortBeerList(ArrayList<Beer> beerList) {
        Collections.sort(beerList, new Comparator<Beer>() {
            String nameLeft, nameRight;
            @Override
            public int compare(Beer beer1, Beer beer2) {
                nameLeft = beer1.getName();
                nameRight = beer2.getName();
                return nameLeft.compareTo(nameRight);
            }

        });

        setSortedList(beerList);
    }

    private void setSortedList(ArrayList<Beer> beerList) {
        beerListAdapter = new BeerListAdapter(MainActivity.this, beerList);
        recyclerView.setAdapter(beerListAdapter);
        beerListAdapter.notifyDataSetChanged();
    }


}
