package com.thoughtworks.apurvpandey.thoughtworks.homeScreen;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;
import com.thoughtworks.apurvpandey.thoughtworks.utils.MessageUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeScreenInteractor {

    public void makeApiCall(final OnApiCallFinishedListener listener, VolleySingleton instance) {
        final String BASE_URL = "http://starlord.hackerearth.com/beercraft";
        RequestQueue requestQueue = instance.getRequestQueue();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, BASE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseResponse(response, listener);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onFailure(error);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void parseResponse(JSONArray response, OnApiCallFinishedListener listener) {

        ArrayList<Beer> beerList = new ArrayList<>();
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

        listener.onSuccess(beerList);
    }

    //Below compartor can be modified according to requirement
    public List<Beer> sortList(List<Beer> list, final String filterField, String filterValue) {

        if (filterField.contains("Sort Alphabetically?")) {
            Collections.sort(list, new Comparator<Beer>() {
                String nameLeft, nameRight;

                @Override
                public int compare(Beer beer1, Beer beer2) {
                    nameLeft = beer1.getName();
                    nameRight = beer2.getName();
                    return nameLeft.compareTo(nameRight);
                }
            });

        } else if (filterField.contains("Sort By Alcohol content")) {
            Collections.sort(list, new Comparator<Beer>() {
                String nameLeft, nameRight;

                @Override
                public int compare(Beer beer1, Beer beer2) {
                    nameLeft = beer1.getAbv();
                    nameRight = beer2.getAbv();
                    if (filterField.contains("Sort By Alcohol content (Ascending)"))
                        return nameLeft.compareTo(nameRight);
                    else return nameRight.compareTo(nameLeft);
                }

            });
        } else if (filterField.contains("Beer Style")) {
            List<Beer> filteredList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (MessageUtility.isNotNullOrEmpty(list.get(i).getStyle())) {
                    if (list.get(i).getStyle().contains(filterValue))
                        filteredList.add(list.get(i));
                }
            }
        } else if (filterField.contains("IBU Content")) {
            List<Beer> filteredList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (MessageUtility.isNotNullOrEmpty(list.get(i).getIbu())) {
                    final int ibu = Integer.parseInt(list.get(i).getIbu());
                    final int filteredContent = Integer.parseInt(filterValue);
                    if (ibu == filteredContent)
                        filteredList.add(list.get(i));
                }
            }
        } else if (filterField.contains("Ounces")) {
            List<Beer> filteredList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                final double ounces = list.get(i).getOunces();
                final double filterContent = Double.parseDouble(filterValue);
                int result = Double.compare(ounces, filterContent);
                if (result == 0) {
                    filteredList.add(list.get(i));
                }

            }
        }
        return list;
    }

    public ArrayList<Beer> filterListByAbvContent(ArrayList<Beer> beerList, String filterField) {
        final ArrayList<Beer> filteredList = new ArrayList<>();
        for (int i = 0; i < beerList.size(); i++) {
            if (MessageUtility.isNotNullOrEmpty(beerList.get(i).getAbv())) {
                final double abvContent = Double.parseDouble(beerList.get(i).getAbv());
                final double filterContent = Double.parseDouble(filterField);
                int result = Double.compare(abvContent, filterContent);
                if (result >= 0) {
                    filteredList.add(beerList.get(i));
                }
            }
        }
        return filteredList;
    }

    public List<Beer> filterListByName(List<Beer> beerList, String query) {
        final List<Beer> filteredList = new ArrayList<>();
        for (int i = 0; i < beerList.size(); i++) {
            if (MessageUtility.isNotNullOrEmpty(beerList.get(i).getName())) {
                final String beerName = beerList.get(i).getName().toLowerCase();
                if (beerName.contains(query)) {
                    filteredList.add(beerList.get(i));
                }
            }
        }
        return filteredList;
    }

    interface OnApiCallFinishedListener {
        void onSuccess(List<Beer> beerList);

        void onFailure(VolleyError error);
    }
}
