package com.thoughtworks.apurvpandey.thoughtworks.homeScreen;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;
import com.thoughtworks.apurvpandey.thoughtworks.network.VolleySingleton;
import com.thoughtworks.apurvpandey.thoughtworks.utils.MessageUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeScreenInteractor {

    public void makeApiCall(final OnApiCallFinishedListener listener) {
        final String BASE_URL = "http://starlord.hackerearth.com/beercraft";
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();

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

    //Below compaartor can be modified according to requirement
    public ArrayList<Beer> sortList(ArrayList<Beer> list) {
        Collections.sort(list, new Comparator<Beer>() {
            String nameLeft, nameRight;

            @Override
            public int compare(Beer beer1, Beer beer2) {
                nameLeft = beer1.getName();
                nameRight = beer2.getName();
                return nameLeft.compareTo(nameRight);

            }

        });

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

    public ArrayList<Beer> filterListByName(ArrayList<Beer> beerList, String query) {
        final ArrayList<Beer> filteredList = new ArrayList<>();
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
        void onSuccess(ArrayList<Beer> beerList);

        void onFailure(VolleyError error);
    }
}
